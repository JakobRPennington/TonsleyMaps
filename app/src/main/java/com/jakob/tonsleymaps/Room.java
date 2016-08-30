package com.jakob.tonsleymaps;

/* Written by Jakob Pennington
 *
 * An object to represent a Room in the database.
 */
public class Room extends SearchResult {
    private String locationName;
    private String floorName;
    private String roomNo;
    private String roomName;

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorNo) {
        this.floorName = floorNo;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    //setResultTitle and setResultDescription methods required to set superclass values after
    //subclass is fully created, enabling superclass fields to contain values from multiple sublcass fields
    public void setResultTitle() {
        String tempTitle = "";
        if (this.roomNo != null){
            tempTitle += this.roomNo;
        }
        if (this.roomNo != null && this.roomName != null){
            tempTitle += " - ";
        }
        if (this.roomName != null){
            tempTitle += this.roomName;
        }
        this.setTitle(tempTitle);
    }

    public void setResultDescription() {
        this.setDescription(this.floorName + " - " +this.locationName);
    }
}
