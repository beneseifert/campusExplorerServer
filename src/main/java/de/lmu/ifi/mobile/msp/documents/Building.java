package de.lmu.ifi.mobile.msp.documents;

import de.lmu.ifi.mobile.msp.dto.RoomFinderBuilding;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "building")
public class Building {

    @Id
    private String id;
    private String name;
    private Double lat;
    private Double lng;
    private String city;

    public Building() {}

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

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public static Building fromRoomFinderBuilding(RoomFinderBuilding roomFinderBuilding) {
        Building building = new Building();
        building.setCity(roomFinderBuilding.getCity());
        building.setId(roomFinderBuilding.getCode());
        building.setLat(Double.parseDouble(roomFinderBuilding.getLat()));
        building.setLng(Double.parseDouble(roomFinderBuilding.getLng()));
        building.setName(roomFinderBuilding.getDisplayName());
        return building;
    }
}
