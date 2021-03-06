package de.lmu.ifi.mobile.msp.documents;

// @Document(collection = "lecture")
public class Event {

    private String room;
    private String time;
    private String date;
    private String dayOfWeek;
    private String cycle;

    public Event() {
    }

    /**
     * 
     * @param room
     * @param time
     * @param date
     * @param cycle
     * @param dayOfWeek
     */
    public Event(String room, String time, String date, String cycle, String dayOfWeek) {
        this.room = room;
        this.time = time;
        this.date = date;
        this.dayOfWeek = dayOfWeek;
        this.cycle = cycle;
    }

    /**
     * @return the room
     */
    public String getRoom() {
        return room;
    }

    /**
     * @param room the room to set
     */
    public void setRoom(String room) {
        this.room = room;
    }

    /**
     * @return the time
     */
    public String getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return the dayOfWeek
     */
    public String getDayOfWeek() {
        return dayOfWeek;
    }

    /**
     * @param dayOfWeek the dayOfWeek to set
     */
    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    /**
     * @return the cycle
     */
    public String getCycle() {
        return cycle;
    }

    /**
     * @param cycle the cycle to set
     */
    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

}
