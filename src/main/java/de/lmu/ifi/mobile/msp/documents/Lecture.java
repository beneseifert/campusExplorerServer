package de.lmu.ifi.mobile.msp.documents;

import de.lmu.ifi.mobile.msp.dto.RoomFinderBuilding;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "lecture")
public class Lecture {

    @Id
    private String id;
    private String name;
    private List<Event> events;
    private String department;
    private String link;

    public Lecture() {}
    public Lecture(String id, String name, List<Event> events, String department, String link) {
        this.id = id;
        this.name = name;
        this.events = events;
        this.department = department;
        this.link = link;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the events
     */
    public List<Event> getEvents() {
        return events;
    }

    /**
     * @param events the events to set
     */
    public void setEvents(List<Event> events) {
        this.events = events;
    }

    /**
     * @return the department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * @param department the department to set
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * @return the link
     */
    public String getLink() {
        return link;
    }

    /**
     * @param link the link to set
     */
    public void setLink(String link) {
        this.link = link;
    }

}
