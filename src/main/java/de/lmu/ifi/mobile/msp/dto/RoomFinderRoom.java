package de.lmu.ifi.mobile.msp.dto;

public class RoomFinderRoom {

    private String rName;
    private String floorCode;
    private String pX;
    private String pY;

    public RoomFinderRoom() {}

    public String getrName() {
        return rName;
    }

    public void setrName(String rName) {
        this.rName = rName;
    }

    public String getFloorCode() {
        return floorCode;
    }

    public void setFloorCode(String floorCode) {
        this.floorCode = floorCode;
    }

    public String getpX() {
        return pX;
    }

    public void setpX(String pX) {
        this.pX = pX;
    }

    public String getpY() {
        return pY;
    }

    public void setpY(String pY) {
        this.pY = pY;
    }
}
