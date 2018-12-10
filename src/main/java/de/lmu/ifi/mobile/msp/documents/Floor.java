package de.lmu.ifi.mobile.msp.documents;

import de.lmu.ifi.mobile.msp.dto.RoomFinderFloor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "floor")
public class Floor {

    @Id
    private String id;
    private String building;
    private String mapFileName;
    private String level;
    private Integer mapWidth;
    private Integer mapHeight;
    private String address;

    public Floor() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getMapFileName() {
        return mapFileName;
    }

    public void setMapFileName(String mapFileName) {
        this.mapFileName = mapFileName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getMapWidth() {
        return mapWidth;
    }

    public void setMapWidth(Integer mapWidth) {
        this.mapWidth = mapWidth;
    }

    public Integer getMapHeight() {
        return mapHeight;
    }

    public void setMapHeight(Integer mapHeight) {
        this.mapHeight = mapHeight;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static Floor fromArbitraryMap(Map<String, String> mapFloor, String id) {
        if (mapFloor.containsKey("bCode")
                && mapFloor.containsKey("mapUri")
                && mapFloor.containsKey("level")
                && mapFloor.containsKey("mapSizeX")
                && mapFloor.containsKey("mapSizeY")
                && mapFloor.containsKey("address")) {
            Floor floor = new Floor();
            floor.setId(id);
            floor.setBuilding(mapFloor.get("bCode"));
            floor.setMapFileName(mapFloor.get("mapUri"));
            floor.setLevel(mapFloor.get("level"));
            floor.setMapWidth(Integer.parseInt(mapFloor.get("mapSizeX")));
            floor.setMapHeight(Integer.parseInt(mapFloor.get("mapSizeY")));
            floor.setAddress(mapFloor.get("address"));
            return floor;
        }
        return null;
    }
}
