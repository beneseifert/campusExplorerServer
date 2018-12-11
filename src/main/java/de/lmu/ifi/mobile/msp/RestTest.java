package de.lmu.ifi.mobile.msp;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.jaunt.Element;
import com.jaunt.Elements;
import com.jaunt.JauntException;
import com.jaunt.NotFound;
import com.jaunt.ResponseException;
import com.jaunt.UserAgent;
import com.jaunt.component.Table;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestTest {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getAllMessages() {
        try {
            UserAgent userAgent = new UserAgent(); // create new userAgent (headless browser).
            userAgent.visit("http://oracle.com");
            return userAgent.doc.innerHTML(); // print the document as HTML
        } catch (JauntException e) { // if an HTTP/connection error occurs, handle JauntException.
            System.err.println(e);
        }
        return "didn't work";
    }

    @RequestMapping(value = "/1", method = RequestMethod.GET)
    public String getCancellations() {
        try {
            UserAgent userAgent = new UserAgent();
            userAgent.visit(
                    "https://lsf.verwaltung.uni-muenchen.de/qisserver/rds?state=currentLectures&type=1&next=CurrentLectures.vm&nextdir=ressourcenManager&navigationPosition=functions%2CcanceledLectures&breadcrumb=canceledLectures&topitem=locallinks&subitem=canceledLectures&asi=");
            Elements courses = userAgent.doc.findEvery("<div class=content>").findEvery("<a>");

            Element date = userAgent.doc.findFirst("<h2>");

            if (courses.size() == 0) {
                return date.innerHTML() + " - Keine";
            } else {
                return date.innerHTML() + courses.innerHTML();
            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return "nope";
    }

    @RequestMapping(value = "/2", method = RequestMethod.GET)
    public String getLectureDirectory() {
        try {
            UserAgent userAgent = new UserAgent();
            userAgent.visit(
                    "https://lsf.verwaltung.uni-muenchen.de/qisserver/rds?state=wtree&search=1&category=veranstaltung.browse&navigationPosition=functions%2Clectureindex&breadcrumb=lectureindex&topitem=locallinks&subitem=lectureindex");

            Elements links = userAgent.doc.findEvery("<a class=ueb>");

            return links.innerHTML();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return "nope";
    }

    @RequestMapping(value = "/3", method = RequestMethod.GET)
    public String getSpecificEvent() {
        try {
            UserAgent userAgent = new UserAgent();
            userAgent.setCacheEnabled(true);
            userAgent.visit(
                    "https://lsf.verwaltung.uni-muenchen.de/qisserver/rds?state=wtree&search=1&category=veranstaltung.browse&navigationPosition=functions%2Clectureindex&breadcrumb=lectureindex&topitem=locallinks&subitem=lectureindex");

            Elements firstLevel = userAgent.doc.findEach("<a class=ueb>");
            List<MetaLink> links = firstLevel.toList().stream().map(el -> {
                try {
                    return new MetaLink(false, el.getAt("href").replace("&amp;", "&"));
                } catch (NotFound e) {
                    e.printStackTrace();
                    return null;
                }
            }).collect(Collectors.toList());
            links.get(0).setLinkVisited();

            List<MetaLink> lastLevelLinks = goOverLinksNonRecursive(userAgent, links);

            return String.join("<br>", lastLevelLinks.stream().map(el -> el.getLink()).collect(Collectors.toList()));

            /*
             * String firstLink =
             * userAgent.doc.findFirst("<a>Statistik").getAt("href").replace("&amp;", "&");
             * 
             * userAgent.visit(firstLink);
             * 
             * // https://lsf.verwaltung.uni-muenchen.de/qisserver/rds;jsessionid=
             * 1B716CE0D8F720E3718985A2C5327271.lsf4?state=wtree&search=1&trex=step&
             * root120182=1%7C357439&P.vx=kurz
             * 
             * // String result = "";
             * 
             * String secondLink =
             * userAgent.doc.findFirst("<a>2. Informatik").getAt("href").replace("&amp;",
             * "&");
             * 
             * userAgent.visit(secondLink);
             * 
             * String thirdLink =
             * userAgent.doc.findFirst("<a>Lehrveranstaltungen im Bachelor").getAt("href").
             * replace("&amp;", "&");
             * 
             * userAgent.visit(thirdLink);
             * 
             * // String fourthLink =
             * userAgent.doc.findFirst("<a>Digitale Medien").getAt("href").replace("&amp;",
             * "&");
             * 
             * Elements veranstaltungen =
             * userAgent.doc.findFirst("<table summary>").findEach("<a class=regular>");
             * List<String> titles = getTitleForElements(veranstaltungen, userAgent);
             * 
             * 
             * // userAgent.visit(fourthLink);
             * 
             * // String title = userAgent.doc.findFirst("<title>").innerHTML();
             * 
             * // return "hm " + title; // return "1: " + firstLink + "<br> 2: " +
             * secondLink +"<br> 3: " + thirdLink + "<br> 4: " + fourthLink + "<br> 5: " +
             * title; return String.join("<br>", titles);
             */
        } catch (Exception e) {
            return e.toString();
            // System.out.println(e.toString());
        }
        // return "nope";
    }

    private String formatLinks(List<MetaLink> links) {
        return String.join("\n", links.stream().map(el -> el.getLink()).collect(Collectors.toList()));
    }

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

    private List<MetaLink> getLectureLinksOfPage(UserAgent userAgent) {
        List<MetaLink> allLectureLinks = new ArrayList<MetaLink>();
        try {
            Element tableElement;
            tableElement = userAgent.doc.findFirst("<table summary>");
            // System.out.println("found!");
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
            // e1.printStackTrace();
            return allLectureLinks;
        }
    }

    private List<MetaLink> goOverLinks(UserAgent userAgent, List<MetaLink> links, int i) {
        if (i < links.size()) {
            MetaLink link = links.get(i);
            if (!link.wasLinkVisited()) {
                System.out.println("now visiting link " + i + " of " + links.size());
                try {
                    // visit the current link
                    userAgent.visit(link.getLink());
                    // set the link as visited so we don't visit it again
                    link.setLinkVisited();
                    // check if we're on a lecture page
                    if (userAgent.doc.findFirst("<title>").getTextContent().contains("Vorlesungsverzeichnis")) {
                        // get the links of the given page
                        List<MetaLink> linksOfLink = getLinksOfPage(userAgent);
                        System.out.println("before merge: " + links.size() + " new links: " + linksOfLink.size());
                        // add the new links to the existing links (if they are new)
                        links = mergeLists(links, linksOfLink);
                        System.out.println("after merge: " + links.size());
                        // now go over the new list
                    }
                    return goOverLinks(userAgent, links, i + 1);
                } catch (ResponseException e) {
                    e.printStackTrace();
                } catch (NotFound e) {
                    e.printStackTrace();
                }
            } else {
                return goOverLinks(userAgent, links, i + 1);
            }
        }
        return links;
    }

    private List<MetaLink> goOverLinksNonRecursive(UserAgent userAgent, List<MetaLink> links) {
        List<MetaLink> lastLevelLinks = new ArrayList<MetaLink>();
        for (int i = 0; i < links.size(); i++) {
            MetaLink link = links.get(i);
            if (!link.wasLinkVisited()) {
                System.out.println("now visiting link " + i + " of " + links.size() + " | " + String.format("%.0f%%",(100 * (float)i) / (float) links.size()));
                try {
                    // visit the current link
                    userAgent.visit(link.getLink());
                    // set the link as visited so we don't visit it again
                    link.setLinkVisited();
                    // check if we're on a lecture page
                    if (userAgent.doc.findFirst("<title>").getTextContent().contains("Vorlesungsverzeichnis")) {
                        // get the links of the given page
                        List<MetaLink> linksOfLink = getLinksOfPage(userAgent);
                        // System.out.println("before merge: " + links.size() + " new links: " +
                        // linksOfLink.size());
                        List<MetaLink> lectureLinks = getLectureLinksOfPage(userAgent);
                        lastLevelLinks = mergeLists(lastLevelLinks, lectureLinks);
                        System.out.println("lastLevelLinks: " + lastLevelLinks.size());
                        // add the new links to the existing links (if they are new)
                        links = mergeLists(links, linksOfLink);
                        // System.out.println("after merge: " + links.size());
                        // now go over the new list
                    } else {
                        System.out.println("adding to last level links");
                    }
                } catch (ResponseException e) {
                    e.printStackTrace();
                } catch (NotFound e) {
                    e.printStackTrace();
                }
            }
        }
        return lastLevelLinks;
    }

    private List<MetaLink> mergeLists(List<MetaLink> links, List<MetaLink> linksOfLink) {
        linksOfLink.forEach(link -> {
            if (!hasLink(links, link)) {
                links.add(link);
            }
        });
        return links;
    }

    private List<MetaLink> addToList(List<MetaLink> links, MetaLink link) {
        if (!hasLink(links, link)) {
            links.add(link);
        }
        return links;
    }

    private boolean hasLink(List<MetaLink> links, MetaLink link) {
        return links.stream().anyMatch(el -> el.getLink().equals(link.getLink()));
    }

    private List<String> getTitleForElements(Elements veranstaltungen, UserAgent userAgent) {
        return veranstaltungen.toList().stream().map(el -> {
            try {
                String link = el.getAt("href").replace("&amp;", "&");
                userAgent.visit(link);
                System.out.println("visiting link " + (veranstaltungen.toList().indexOf(el) + 1) + " of "
                        + veranstaltungen.size() + ": " + link);
                return userAgent.doc.findFirst("<title>").innerHTML();
            } catch (NotFound e) {
                e.printStackTrace();
                return null;
            } catch (ResponseException e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());
    }

    // @RequestMapping(value = "/sendMessage", method = RequestMethod.POST, consumes
    // = "application/json")
    // public ResponseEntity<HttpStatus> receiveMessage(@RequestBody Message
    // message) {
    // MessageModel.addMessage(message);
    // return ResponseEntity.ok(HttpStatus.OK);
    // }
}