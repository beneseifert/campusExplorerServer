package de.lmu.ifi.mobile.msp;

import com.jaunt.JauntException;
import com.jaunt.UserAgent;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestTest {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getAllMessages() {
        try{
            UserAgent userAgent = new UserAgent();                       //create new userAgent (headless browser).
            userAgent.visit("http://oracle.com");                        //visit a url  
            return userAgent.doc.innerHTML();               //print the document as HTML
          }
          catch(JauntException e){         //if an HTTP/connection error occurs, handle JauntException.
            System.err.println(e);
          }
        return "didn't work";
    }

    // @RequestMapping(value = "/sendMessage", method = RequestMethod.POST, consumes = "application/json")
    // public ResponseEntity<HttpStatus> receiveMessage(@RequestBody Message message) {
    //     MessageModel.addMessage(message);
    //     return ResponseEntity.ok(HttpStatus.OK);
    // }
}