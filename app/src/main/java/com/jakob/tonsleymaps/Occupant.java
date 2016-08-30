package com.jakob.tonsleymaps;

/* Written by Jakob Pennington
 *
 * An object to represent the occupant of a room in the database.
 */
public class Occupant extends SearchResult {
    private String locationName;
    private String floorName;
    private String roomNo;
    private String roomName;
    private String prefix;
    private String name;
    private String phone;
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    //setResultTitle and setResultDescription methods required to set superclass values after
    //subclass is fully created, enabling superclass fields to contain multiple sublcass fields
    public void setResultTitle() {
        if (this.prefix != null) {
            this.setTitle(this.prefix + " " + this.name);
        } else {
            this.setTitle(this.name);
        }
    }

    public void setResultDescription() {
        this.setDescription(this.roomNo + " - " + this.floorName + " - " + this.locationName);
    }
}
