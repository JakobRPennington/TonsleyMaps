package com.jakob.tonsleymaps;

/* Written by Jakob Pennington
 *
 * An object to store data about Locations from the database
 */
public class Location extends SearchResult {
    private String locationName;
    private String openHours;

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
        this.setTitle(locationName);
    }

    public String getOpenHours() {
        return openHours;
    }

    public void setOpenHours(String openHours) {
        this.openHours = openHours;
        this.setDescription("Open Hours: " + openHours);
    }
}
