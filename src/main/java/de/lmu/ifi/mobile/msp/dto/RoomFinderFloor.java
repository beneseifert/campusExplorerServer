package de.lmu.ifi.mobile.msp.dto;

public class RoomFinderFloor {

    private String bCode;
    private String displayName;
    private String buildingPart;
    private String mapUri;
    private String level;
    private String fName;
    private String mapSizeX;
    private String mapSizeY;
    private String address;

    public RoomFinderFloor(String bCode) {}

    public String getbCode() {
        return bCode;
    }

    public void setbCode(String bCode) {
        this.bCode = bCode;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getBuildingPart() {
        return buildingPart;
    }

    public void setBuildingPart(String buildingPart) {
        this.buildingPart = buildingPart;
    }

    public String getMapUri() {
        return mapUri;
    }

    public void setMapUri(String mapUri) {
        this.mapUri = mapUri;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getMapSizeX() {
        return mapSizeX;
    }

    public void setMapSizeX(String mapSizeX) {
        this.mapSizeX = mapSizeX;
    }

    public String getMapSizeY() {
        return mapSizeY;
    }

    public void setMapSizeY(String mapSizeY) {
        this.mapSizeY = mapSizeY;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
