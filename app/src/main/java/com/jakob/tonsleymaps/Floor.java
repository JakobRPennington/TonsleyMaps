package com.jakob.tonsleymaps;

/* Written by Jakob Pennington
 *
 * An object to represent floors in the database.
 */
public class Floor extends SearchResult {
    private String locationName;
    private String floorName;

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
        this.setDescription(locationName);
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
        this.setTitle(floorName);
    }
}
