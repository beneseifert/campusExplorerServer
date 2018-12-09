package de.lmu.ifi.mobile.msp;

import com.jaunt.*;

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
        userAgent.visit("https://lsf.verwaltung.uni-muenchen.de/qisserver/rds?state=currentLectures&type=1&next=CurrentLectures.vm&nextdir=ressourcenManager&navigationPosition=functions%2CcanceledLectures&breadcrumb=canceledLectures&topitem=locallinks&subitem=canceledLectures&asi=");
        Elements courses = userAgent.doc.findEvery("<div class=content>").findEvery("<a>");

        Element date = userAgent.doc.findFirst("<h2>");
        
        if (courses.size() == 0){
            return date.innerHTML() + " - Keine";
        }else{
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
        userAgent.visit("https://lsf.verwaltung.uni-muenchen.de/qisserver/rds?state=wtree&search=1&category=veranstaltung.browse&navigationPosition=functions%2Clectureindex&breadcrumb=lectureindex&topitem=locallinks&subitem=lectureindex");
        
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
        userAgent.visit("https://lsf.verwaltung.uni-muenchen.de/qisserver/rds?state=wtree&search=1&category=veranstaltung.browse&navigationPosition=functions%2Clectureindex&breadcrumb=lectureindex&topitem=locallinks&subitem=lectureindex");
        
        String firstLink = userAgent.doc.findFirst("<a>Statistik").getAt("href").replace("&amp;", "&");

        userAgent.visit(firstLink);

        // https://lsf.verwaltung.uni-muenchen.de/qisserver/rds;jsessionid=1B716CE0D8F720E3718985A2C5327271.lsf4?state=wtree&search=1&trex=step&root120182=1%7C357439&P.vx=kurz

        // String result = "";

        String secondLink = userAgent.doc.findFirst("<a>2. Informatik").getAt("href").replace("&amp;", "&");
        
        userAgent.visit(secondLink);
        
        String thirdLink = userAgent.doc.findFirst("<a>Lehrveranstaltungen im Bachelor").getAt("href").replace("&amp;", "&");
        
        userAgent.visit(thirdLink);
        
        String fourthLink = userAgent.doc.findFirst("<a>Digitale Medien").getAt("href").replace("&amp;", "&");
        
        userAgent.visit(fourthLink);
        
        String title = userAgent.doc.findFirst("<title>").innerHTML();
        
        // return "hm " + title;
        return "1: " + firstLink + "<br> 2: " + secondLink +"<br> 3: " +  thirdLink + "<br> 4: " + fourthLink + "<br> 5: " + title;
        } catch (Exception e) {
            return e.toString();
            // System.out.println(e.toString());
        }
        // return "nope";
    }



    // @RequestMapping(value = "/sendMessage", method = RequestMethod.POST, consumes
    // = "application/json")
    // public ResponseEntity<HttpStatus> receiveMessage(@RequestBody Message
    // message) {
    // MessageModel.addMessage(message);
    // return ResponseEntity.ok(HttpStatus.OK);
    // }
}