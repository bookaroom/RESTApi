package com.comeet.data;

import com.comeet.MetroBuildingList;
import com.comeet.Room;
import com.comeet.utilities.ApiLogger;
import com.mysql.cj.jdbc.MysqlDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Manages connections to and retrieves data from the database.
 * @author Dairai
 *
 */
public class DataRepository {

    private static final String CONNECTION_PROPERTIES_PATH = "/com/comeet/properties/config.properties";
    
    private Connection sqlConnection;

    private void setupConnection() throws SQLException {

        String propPath = CONNECTION_PROPERTIES_PATH;
        Properties prop = new Properties();
        InputStream input =
                        getClass().getResourceAsStream(propPath);
        
        try {
            prop.load(input);
        } catch (IOException ioe) {
            String err = String.format("Failed to load SQL connection properties from path {0}", propPath);
            ApiLogger.logger.severe(err);
            throw new RuntimeException(err, ioe);
        }

        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUser(prop.getProperty("dbuser"));
        dataSource.setPassword(prop.getProperty("dbpassword"));
        dataSource.setServerName(prop.getProperty("dbendpoint"));
        dataSource.setDatabaseName(prop.getProperty("dbname"));

        sqlConnection = dataSource.getConnection();
    }

    private void closeConnection() throws SQLException {
        sqlConnection.close();
    }

    /**
     * Retrieves room metadata from the comeet database.
     * 
     * @param email The email address for the room.
     * @return Room metadata.
     */
    public Room retrieveRoomMetadata(String email) throws SQLException {

        Room roomMetadata = null;

        setupConnection();

        if (sqlConnection == null || sqlConnection.isClosed()) {
            throw new SQLException("No connection to established to the database.");
        }

        String stmtStr = "select * from Rooms where email = ?";
        PreparedStatement stmt = sqlConnection.prepareStatement(stmtStr);
        stmt.setString(1, email);
        stmt.execute();
        ResultSet rs = stmt.getResultSet();
        rs.beforeFirst();
        
        if (rs.isBeforeFirst()) {
            if (rs.next()) {
                roomMetadata = new Room();
                roomMetadata.setCountry(rs.getString(rs.findColumn("Country")));
                roomMetadata.setMetroarea(rs.getString(rs.findColumn("Metro")));
                roomMetadata.setLatitude(Float.toString(rs.getFloat(rs.findColumn("Latitude"))));
                roomMetadata.setLongitude(Float.toString(rs.getFloat(rs.findColumn("Longitude"))));
                roomMetadata.setAddress(rs.getString(rs.findColumn("address")));
                roomMetadata.setNavigationMap(rs.getString(rs.findColumn("navigation")));
                roomMetadata.setCapacity(rs.getInt(rs.findColumn("Longitude")));
                roomMetadata.setRoomPic(rs.getString(rs.findColumn("roomPic")));
                roomMetadata.setState(rs.getString(rs.findColumn("state")));
            }
        }

        closeConnection();

        return roomMetadata;
    }
    
    /**
     * Retrieves the search criteria from the database based on the domain
     * @param domain The domain of organization to search for
     * @return The list of metro areas and their room list names
     * @throws Exception On an unexpected error.
     */ 
    public List<MetroBuildingList> retrieveSearchCriteria(String domain) throws Exception {

        setupConnection();

        if (sqlConnection == null || sqlConnection.isClosed()) {
            throw new SQLException("No connection to established to the database.");
        }

        String stmtStr = "select email, name, Metroarea from Roomlist where domain = ?;";
        PreparedStatement stmt = sqlConnection.prepareStatement(stmtStr);
        stmt.setString(1, domain);
        stmt.execute();
        
        ResultSet rs = stmt.getResultSet();
        rs.beforeFirst();

        Map<String, MetroBuildingList> searchFields = new HashMap<>();
        ArrayList<MetroBuildingList> result = null;
        
        if (rs.isBeforeFirst()) {
            
            while (rs.next()) {                
                
                String buildingName = rs.getString(rs.findColumn("name"));
                String metroName = rs.getString(rs.findColumn("Metroarea"));
                String buildingEmail = rs.getString(rs.findColumn("email"));
                
                if (searchFields.get(metroName) == null) {
                    MetroBuildingList newList = new MetroBuildingList();
                    newList.setMetro(metroName);
                    newList.setRoomlist(buildingEmail, buildingName);
                    searchFields.put(metroName, newList);
                } else {
                    MetroBuildingList roomLists = searchFields.get(metroName);
                    roomLists.setRoomlist(buildingEmail, metroName);;
                } 
            }
        
            result = new ArrayList<>(searchFields.values());
        }
        
        closeConnection();
        return result;
    }
}
