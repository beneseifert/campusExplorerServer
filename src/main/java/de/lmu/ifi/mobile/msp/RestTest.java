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

}