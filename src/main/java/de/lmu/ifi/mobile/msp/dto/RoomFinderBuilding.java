package de.lmu.ifi.mobile.msp.dto;

public class RoomFinderBuilding {

    private String code;
    private String displayName;
    private String lat;
    private String lng;
    private String city;
    private String hasImage;

    public RoomFinderBuilding() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getHasImage() {
        return hasImage;
    }

    public void setHasImage(String hasImage) {
        this.hasImage = hasImage;
    }
}
