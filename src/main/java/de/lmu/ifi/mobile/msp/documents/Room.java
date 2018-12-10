package de.lmu.ifi.mobile.msp.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

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

    public static Room fromArbitraryMap(Map<String, String> mapRoom, String id) {
        if (mapRoom.containsKey("rName")
                && mapRoom.containsKey("floorCode")
                && mapRoom.containsKey("pX")
                && mapRoom.containsKey("pY")) {
            Room room = new Room();
            room.setId(id);
            room.setName(mapRoom.get("rName"));
            room.setFloor(mapRoom.get("floorCode"));
            room.setMapX(Integer.parseInt(mapRoom.get("pX")));
            room.setMapY(Integer.parseInt(mapRoom.get("pY")));
            return room;
        }
        return null;
    }
}
