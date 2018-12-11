package de.lmu.ifi.mobile.msp;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.jaunt.Element;
import com.jaunt.Elements;
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
    public String getSpecificEvent() {
        try {
            // get a userAgent to play browser
            UserAgent userAgent = new UserAgent();
            // enable cache so we don't have to visit the same 11575 websites again
            userAgent.setCacheEnabled(true);
            // now start with the overview
            userAgent.visit(
                    "https://lsf.verwaltung.uni-muenchen.de/qisserver/rds?state=wtree&search=1&category=veranstaltung.browse&navigationPosition=functions%2Clectureindex&breadcrumb=lectureindex&topitem=locallinks&subitem=lectureindex");

            // get the initial set of top-level links
            List<MetaLink> links = getLinksOfPage(userAgent);
            // set the first top-level link as visited
            links.get(0).setLinkVisited();
            // now from here on, go to all child links and retrieve the last-level links
            List<MetaLink> lastLevelLinks = goOverLinks(userAgent, links);
            // return the last-level links as an html page
            return String.join("<br>", lastLevelLinks.stream().map(el -> el.getLink()).collect(Collectors.toList()));
        } catch (Exception e) {
            return e.toString();
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

    // @RequestMapping(value = "/sendMessage", method = RequestMethod.POST, consumes
    // = "application/json")
    // public ResponseEntity<HttpStatus> receiveMessage(@RequestBody Message
    // message) {
    // MessageModel.addMessage(message);
    // return ResponseEntity.ok(HttpStatus.OK);
    // }
}