package com.jakob.tonsleymaps;

/* Written by Jakob Pennington
 *
 * Abstracted class to represent a single result from a query to the database.
 * Enables a list of Locations, Floors, Rooms and Occupants to be created.
 */
abstract public class SearchResult {
    private String title;
    private String description;

    public int getMapX() {
        return mapX;
    }

    public void setMapX(int mapX) {
        this.mapX = mapX;
    }

    public int getMapY() {
        return mapY;
    }

    public void setMapY(int mapY) {
        this.mapY = mapY;
    }

    private int mapX;
    private  int mapY;

    public String getMapName() {
        return mapName;
    }

    // Called if the object is a Location - the mapName is set to precinct
    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    //Based on the object type, set the name of the appropriate map
    public void setMapName(String locationName, String floorName){
        switch (locationName){
            case "Flinders University - T1":
                    switch (floorName){
                        case "Ground Floor":
                            this.mapName = "t1_ground";
                            break;
                        case "First Floor":
                            this.mapName = "t1_first";
                            break;
                        case "Second Floor":
                            this.mapName = "t1_second";
                            break;
                        case "Third Floor":
                            this.mapName = "t1_third";
                            break;
                        case "Forth Floor":
                            this.mapName = "t1_forth";
                            break;
                        case "Fifth Floor":
                            this.mapName = "t1_fifth";
                            break;
                    }
                break;
            case "Flinders University - Pod":
                switch (floorName){
                    case "Ground Floor":
                        this.mapName = "pod_ground";
                }
                break;
        }
    }

    private String mapName;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
