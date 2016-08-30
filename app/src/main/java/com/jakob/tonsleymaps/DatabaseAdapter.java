package com.jakob.tonsleymaps;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* Written by Jakob Pennington\
 *
 * A DatabaseAdapter which processes SQL queries using the database created in DatabaseHelper
 */
public class DatabaseAdapter {
    private final Context mContext;
    private SQLiteDatabase mDatabase;
    private DatabaseHelper mDatabaseHelper;
    private List<SearchResult> searchResults;

    // Components of SQL queries to return data for Location, Floor, Room and Occupant objects
    private static final String LOCATION_QUERY_START = "SELECT locationName, openHours, mapX, mapY from Locations WHERE locationName LIKE '%";
    private static final String LOCATION_QUERY_END = "%' ORDER BY locationName";
    private static final String FLOOR_QUERY_START = "SELECT locationName, floorName FROM Locations l JOIN Floor f ON l.locationId=f.locationId WHERE floorName LIKE '%";
    private static final String FLOOR_QUERY_END = "%' ORDER BY locationName, floorName";
    private static final String ROOM_QUERY_START = "SELECT locationName, floorName, roomNo, roomName, r.mapX, r.mapY FROM Locations l JOIN Floor f ON l.locationId=f.locationId JOIN Room r ON f.floorId=r.floorId WHERE roomNo LIKE '%";
    private static final String ROOM_QUERY_MIDDLE = "%' OR roomName LIKE '%";
    private static final String ROOM_QUERY_END = "%' ORDER BY locationName,  roomNo";
    private static final String OCCUPANT_QUERY_START = "SELECT locationName, floorName, roomNo, roomName, r.mapX, r.mapY, title, name, phone, email FROM Locations l JOIN Floor f ON l.locationId=f.locationId JOIN Room r ON f.floorId=r.floorId JOIN Occupant o ON r.roomId=o.roomId WHERE name LIKE '%";
    private static final String OCCUPANT_QUERY_END = "%' ORDER BY name";

    // Constructed SQL Queries
    private String locationQuery;
    private String floorQuery;
    private String roomQuery;
    private String occupantQuery;

    public DatabaseAdapter(Context context) {
        this.mContext = context;
        mDatabaseHelper = new DatabaseHelper(context);
    }

    public DatabaseAdapter createDatabase() throws SQLException {
        try {
            mDatabaseHelper.createDatabase();
        } catch (IOException mIOException) {
            Log.e("DatabaseAdapter", mIOException.toString() + " UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public DatabaseAdapter open() throws SQLException {
        try {
            mDatabaseHelper.openDatabase();
            mDatabaseHelper.close();
            mDatabase = mDatabaseHelper.getReadableDatabase();
        } catch (SQLException mSQLException){
            Log.e("DatabaseAdapter","open >> "+ mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close(){
        mDatabaseHelper.close();
    }

    public Cursor getCursor(String query){
        try {
            Cursor mCursor = mDatabase.rawQuery(query, null);
            if (mCursor != null){
                mCursor.moveToNext();
            }
            return mCursor;
        } catch (SQLException mSQLException){
            Log.e("DatabaseAdapter", "getQueryData >> " + mSQLException.toString());
            throw mSQLException;
        }
    }

    public List<SearchResult> submitQuery(String query){
        searchResults = new ArrayList<>();

        // Construct queries
        locationQuery = LOCATION_QUERY_START + query + LOCATION_QUERY_END;
        floorQuery = FLOOR_QUERY_START + query + FLOOR_QUERY_END;
        roomQuery = ROOM_QUERY_START + query + ROOM_QUERY_MIDDLE + query + ROOM_QUERY_END;
        occupantQuery = OCCUPANT_QUERY_START + query + OCCUPANT_QUERY_END;

        // Process queries based on query type
        processLocationQuery(locationQuery);
        processFloorQuery(floorQuery);
        processRoomQuery(roomQuery);
        processOccupantQuery(occupantQuery);

        return searchResults;
    }

    private void processLocationQuery(String query){
        // Create a cursor to the results from the query and iterate through, adding objects to the list
        Cursor cursor = getCursor(query);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Location newLocation = new Location();
            newLocation.setLocationName(cursor.getString(0));
            newLocation.setOpenHours(cursor.getString(1));
            newLocation.setMapX(cursor.getInt(2));
            newLocation.setMapY(cursor.getInt(3));
            newLocation.setMapName("precinct");
            searchResults.add(newLocation);
            cursor.moveToNext();
        }
    }

    private void processFloorQuery(String query){
        // Create a cursor to the results from the query and iterate through, adding objects to the list
        Cursor cursor = getCursor(query);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Floor newFloor = new Floor();
            newFloor.setLocationName(cursor.getString(0));
            newFloor.setFloorName(cursor.getString(1));
            newFloor.setMapName(cursor.getString(0), cursor.getString(1));
            searchResults.add(newFloor);
            cursor.moveToNext();
        }
    }

    private void processRoomQuery(String query){
        // Create a cursor to the results from the query and iterate through, adding objects to the list
        Cursor cursor = getCursor(query);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Room newRoom = new Room();
            newRoom.setLocationName(cursor.getString(0));
            newRoom.setFloorName(cursor.getString(1));
            newRoom.setRoomNo(cursor.getString(2));
            newRoom.setRoomName(cursor.getString(3));
            newRoom.setMapX(cursor.getInt(4));
            newRoom.setMapY(cursor.getInt(5));
            newRoom.setMapName(cursor.getString(0), cursor.getString(1));
            newRoom.setResultTitle();
            newRoom.setResultDescription();
            searchResults.add(newRoom);
            cursor.moveToNext();
        }
    }

    private void processOccupantQuery(String query){
        // Create a cursor to the results from the query and iterate through, adding objects to the list
        Cursor cursor = getCursor(query);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Occupant newOccupant = new Occupant();
            newOccupant.setLocationName(cursor.getString(0));
            newOccupant.setFloorName(cursor.getString(1));
            newOccupant.setRoomNo(cursor.getString(2));
            newOccupant.setRoomName(cursor.getString(3));
            newOccupant.setMapX(cursor.getInt(4));
            newOccupant.setMapY(cursor.getInt(5));
            newOccupant.setPrefix(cursor.getString(6));
            newOccupant.setName(cursor.getString(7));
            newOccupant.setPhone(cursor.getString(8));
            newOccupant.setEmail(cursor.getString(9));
            newOccupant.setMapName(cursor.getString(0), cursor.getString(1));
            newOccupant.setResultTitle();
            newOccupant.setResultDescription();
            searchResults.add(newOccupant);
            cursor.moveToNext();
        }
    }


}
