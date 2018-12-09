package de.lmu.ifi.mobile.msp.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "room")
public class Room {

    @Id
    private String id;
    private String name;
    private String floor;
    private Integer mapX;
    private Integer mapY;

    public Room() {}

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

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public Integer getMapX() {
        return mapX;
    }

    public void setMapX(Integer mapX) {
        this.mapX = mapX;
    }

    public Integer getMapY() {
        return mapY;
    }

    public void setMapY(Integer mapY) {
        this.mapY = mapY;
    }
}
