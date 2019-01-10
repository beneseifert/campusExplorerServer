package de.lmu.ifi.mobile.msp;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.jaunt.Element;
import com.jaunt.Elements;
import com.jaunt.NotFound;
import com.jaunt.ResponseException;
import com.jaunt.UserAgent;
import com.jaunt.component.Table;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.lmu.ifi.mobile.msp.documents.Event;
import de.lmu.ifi.mobile.msp.documents.Lecture;
import de.lmu.ifi.mobile.msp.repositories.LectureRepository;

@RestController
public class RestTest {

    @Autowired
    LectureRepository lectureRepository;

    /**
     * Returns all the lectures as a json string
     * 
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getAllLectures() {
        try {
            // get a userAgent to play browser
            UserAgent userAgent = new UserAgent();
            // enable cache so we don't have to visit the same 11575 websites again
            userAgent.setCacheEnabled(true);
            userAgent.settings.responseTimeout = 80000;
            // now start with the overview
            userAgent.visit(
                    "https://lsf.verwaltung.uni-muenchen.de/qisserver/rds?state=wtree&search=1&category=veranstaltung.browse&navigationPosition=functions%2Clectureindex&breadcrumb=lectureindex&topitem=locallinks&subitem=lectureindex");

            // get the initial set of top-level links
            List<MetaLink> links = getLinksOfPage(userAgent);
            // set the first top-level link as visited
            links.get(0).setLinkVisited();
            // now from here on, go to all child links and retrieve the last-level links
            List<MetaLink> lastLevelLinks = goOverLinks(userAgent, links);
            // get all the Lecture objects from their links
            // List<Lecture> lectures = getLectures(lastLevelLinks, userAgent);
            getLectures(lastLevelLinks, userAgent);
            // return the lectures object as a JSON (for debugging purposes)
            // Gson gson = new Gson();
            return ""; // gson.toJson(lectures);
        } catch (Exception e) {
            return e.toString();
        }
    }

    /**
     * Returns the lectures of all given lastLevelLinks
     * 
     * @param lastLevelLinks
     * @param userAgent
     * @return
     */
    private void getLectures(List<MetaLink> lastLevelLinks, UserAgent userAgent) {
        // List<Lecture> lectures = new ArrayList<Lecture>();
        for (MetaLink link : lastLevelLinks) {
            System.out
                    .println("looking at link: " + (lastLevelLinks.indexOf(link) + 1) + " of " + lastLevelLinks.size());
            // check if the link was visited already
            if (!link.wasLinkVisited()) {
                try {
                    // visit the current link
                    userAgent.visit(link.getLink());
                    // set the link as visited so we don't visit it again
                    link.setLinkVisited();
                    // get the lecture of the given page
                    // Lecture lecture = getLectureOfPage(userAgent, link);
                    getLectureOfPage(userAgent, link);
                    // lectures.add(lecture);
                } catch (ResponseException e) {
                    e.printStackTrace();
                }
            }
            // break;
        }
        // return lectures;
    }

    /**
     * Returns the {@link Lecture} of the current page.
     * 
     * @param userAgent
     * @param link
     * @return
     */
    private void getLectureOfPage(UserAgent userAgent, MetaLink link) {
        Lecture lecture = new Lecture();
        try {
            // get the Table for the "Grunddaten"
            Table grundDaten = new Table(userAgent.doc.findFirst("<table summary='Grunddaten zur Veranstaltung'>"));
            String type = grundDaten.getRow("Veranstaltungsart").findFirst("<td>").getTextContent();
            String id = grundDaten.getRow("Veranstaltungsnummer").findFirst("<td>").getTextContent();
            String name = userAgent.doc.findFirst("<h1>").getTextContent().replaceAll("[\n\t]*", "")
                    .replace("- Einzelansicht", "").trim();
            // now get the event tables via one of the table headers
            List<Table> allEventTables = userAgent.doc.findEvery("<th scope='col' class='mod'>Tag").toList().stream()
                    .map(element -> {
                        try {
                            return new Table(element.getParent().getParent());
                        } catch (NotFound e1) {
                            e1.printStackTrace();
                            return null;
                        }
                    }).collect(Collectors.toList());
            ArrayList<Event> allEvents = new ArrayList<Event>();
            // check if we have any events
            if (allEventTables.size() > 0) {
                // go over all group tables
                for (Table currentEventTable : allEventTables) {
                    // check if we have any events in this group (might be empty)
                    if (currentEventTable.getCol(0).size() > 1) {
                        // now go over all events in this currentEventTable
                        for (int currentIndex = 0; currentIndex < currentEventTable.getCol(0).size()
                                - 1; currentIndex++) {
                            allEvents
                                    .add(new Event(getFormattedPropertyOfTable(currentEventTable, "Raum", currentIndex),
                                            getFormattedPropertyOfTable(currentEventTable, "Zeit", currentIndex),
                                            getFormattedPropertyOfTable(currentEventTable, "Dauer", currentIndex),
                                            getFormattedPropertyOfTable(currentEventTable, "Rhythmus", currentIndex),
                                            getFormattedPropertyOfTable(currentEventTable, "Tag", currentIndex)));

                        }
                    }
                }

            }
            // get the department via the department element which we find through it's
            // caption
            Element departmentElement = userAgent.doc.findFirst("<caption>Zuordnung zu Einrichtungen").getParent();
            String department = departmentElement.findFirst("<a class='regular'>").getTextContent()
                    .replaceAll("[\n\t]*", "");
            String departmentlink = departmentElement.findFirst("<a class='regular'>").getAt("href").replace("&amp;",
                    "&");
            userAgent.visit(departmentlink);
            System.out.println("departmentlink: " + departmentlink);

            if (userAgent.doc.findEvery("<a>Fakult").toList().size() > 0) {
                String faculty = userAgent.doc.findFirst("<a>Fakult").getTextContent().replaceAll("[\n\t]*", "");
                System.out.println("faculty: " + faculty);

                // add the information as an object
                lecture = new Lecture(id, name, allEvents, department, type, faculty, link.getLink());
                // für dich bene zum löschen gibts auch ne REST resource
                lectureRepository.save(lecture);
            }

        } catch (NotFound e) {
            e.printStackTrace();
        }
        // return lecture;
        catch (ResponseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the formatted string (without unnecessary whitespaces etc.) of the
     * given propertyName in the given currentEventTable at the currentIndex
     * 
     * @param currentEventTable
     * @param propertyName
     * @param currentIndex
     * @return
     */
    private String getFormattedPropertyOfTable(Table currentEventTable, String propertyName, int currentIndex) {
        try {
            return currentEventTable.getColBelow(propertyName).toList().get(currentIndex).getTextContent()
                    .replaceAll("Geschossplan", "").replaceAll("-\\.", "").replaceAll("[\n\t]*", "").replaceAll("&nbsp;", " ").trim();
        } catch (NotFound e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns all overview links from the current page
     * 
     * @param userAgent
     * @return
     */
    private List<MetaLink> getLinksOfPage(UserAgent userAgent) {
        Elements firstLevel = userAgent.doc.findEach("<a class=ueb>");
        return firstLevel.toList().stream().map(el -> {
            try {
                return new MetaLink(false, el.getAt("href").replace("&amp;", "&"));
            } catch (NotFound e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());
    }

    /**
     * Returns all the lecture links from the current page
     * 
     * @param userAgent
     * @return
     */
    private List<MetaLink> getLectureLinksOfPage(UserAgent userAgent) {
        List<MetaLink> allLectureLinks = new ArrayList<MetaLink>();
        try {
            Element tableElement;
            tableElement = userAgent.doc.findFirst("<table summary>");
            Table table = new Table(tableElement);
            Elements allLectureElements = table.getColBelow("Veranstaltung");
            return allLectureElements.toList().stream().map(el -> {
                try {
                    return new MetaLink(false, el.getFirst("<a class=regular>").getAt("href").replace("&amp;", "&"));
                } catch (NotFound e) {
                    e.printStackTrace();
                    return null;
                }
            }).collect(Collectors.toList());
        } catch (NotFound e1) {
            return allLectureLinks;
        }
    }

    /**
     * Retrieve all lecture MetaLinks
     * 
     * @param userAgent
     * @param allOverviewWebsites
     * @return
     */
    private List<MetaLink> goOverLinks(UserAgent userAgent, List<MetaLink> allOverviewWebsites) {
        List<MetaLink> allLectureLinks = new ArrayList<MetaLink>();
        // this runs until we have viewed all overview websites
        for (int i = 0; i < allOverviewWebsites.size(); i++) {
            // for (int i = 0; i < 100; i++) {
            MetaLink link = allOverviewWebsites.get(i);
            // check if the link was visited already
            if (!link.wasLinkVisited()) {
                System.out.println("now visiting link " + i + " of " + allOverviewWebsites.size() + " | "
                        + String.format("%.0f%%", (100 * (float) i) / (float) allOverviewWebsites.size()));
                try {
                    // visit the current link
                    userAgent.visit(link.getLink());
                    // set the link as visited so we don't visit it again
                    link.setLinkVisited();
                    // check if we're on a lecture page
                    if (userAgent.doc.findFirst("<title>").getTextContent().contains("Vorlesungsverzeichnis")) {
                        // get the links of the given page
                        List<MetaLink> linksOfLink = getLinksOfPage(userAgent);
                        // get all the lecture pages from this page
                        List<MetaLink> lectureLinks = getLectureLinksOfPage(userAgent);
                        allLectureLinks = mergeLists(allLectureLinks, lectureLinks);
                        System.out.println("lastLevelLinks: " + allLectureLinks.size());
                        // add the new links to the existing links (if they are new)
                        allOverviewWebsites = mergeLists(allOverviewWebsites, linksOfLink);
                        // now go over the new list
                    }
                } catch (ResponseException e) {
                    e.printStackTrace();
                } catch (NotFound e) {
                    e.printStackTrace();
                }
            }
        }
        return allLectureLinks;
    }

    /**
     * Merge two lists of MetaLinks
     * 
     * @param links
     * @param linksOfLink
     * @return the inner join the of two lists
     */
    private List<MetaLink> mergeLists(List<MetaLink> links, List<MetaLink> linksOfLink) {
        linksOfLink.forEach(link -> {
            if (!hasLink(links, link)) {
                links.add(link);
            }
        });
        return links;
    }

    /**
     * Check whether we have this exact MetaLink in this list of MetaLinks
     * 
     * @param links
     * @param link
     * @return
     */
    private boolean hasLink(List<MetaLink> links, MetaLink link) {
        return links.stream().anyMatch(el -> el.getLink().equals(link.getLink()));
    }

    // @RequestMapping(value = "/getBuilding", method = RequestMethod.POST/*,
    // consumes = "application/json"*/)
    // public ResponseEntity<String> receiveMessage(@RequestBody String building) {
    // System.out.println("building: " + building);
    // List<Lecture> lectures =
    // lectureRepository.findByNameLike("Projektmanagement");
    // Gson gson = new Gson();
    // return new ResponseEntity<String>(gson.toJson(lectures), HttpStatus.OK);
    // }

    @RequestMapping(value = "/getBuilding", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> receiveMessageNew(@RequestBody String query) {
        // get the requested building from the query
        System.out.println("query: " + query);
        String building = query.replace("{\"building\":", "").replace("}", "").replaceAll("\"", "").replace(" ", "");
        System.out.println("building: " + building);
        List<Lecture> lectures = lectureRepository.findByEvents_Room(building);
        Gson gson = new Gson();
        return new ResponseEntity<String>(gson.toJson(lectures), HttpStatus.OK);
    }

    @RequestMapping(value = "/deleteLectureCollection", method = RequestMethod.GET)
    private void deleteLectures() {
        lectureRepository.deleteAll();
    }
}