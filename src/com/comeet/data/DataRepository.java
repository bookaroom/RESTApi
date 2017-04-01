package com.comeet.data;

import com.comeet.MetroBuildingList;
import com.comeet.Room;
import com.comeet.utilities.ApiLogger;
import com.mysql.cj.jdbc.MysqlDataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;


public class DataRepository {

    private Connection sqlConnection;

    private void setupConn() throws Exception {

        Properties prop = new Properties();
        InputStream input =
                        getClass().getResourceAsStream("/com/comeet/Properties/config.properties");
        prop.load(input);

        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUser(prop.getProperty("dbuser"));
        dataSource.setPassword(prop.getProperty("dbpassword"));
        dataSource.setServerName(prop.getProperty("dbendpoint"));
        dataSource.setDatabaseName(prop.getProperty("dbname"));

        sqlConnection = dataSource.getConnection();

    }

    private void closeConnection() throws Exception {
        sqlConnection.close();
    }

    private void getRooms() throws Exception {
        try {

            setupConn();

            Statement stmt = sqlConnection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from Rooms");

            // TODO - process result set to appropriate result

        } catch (Exception e) {
            //ApiLogger.logger.log(Level.SEVERE, "Error getting rooms from database database", e);
            throw e;
        }

    }

    /**
     * Retrieves room metadata from the comeet database.
     * 
     * @param email The email address for the room.
     * @return Room metadata.
     */
    public Room retrieveRoomMetadata(String email) throws Exception {

        Room roomMetadata = null;

        try {

            setupConn();

            if (sqlConnection == null || sqlConnection.isClosed()) {
                System.out.println("no connection to database");
                return null;
            }

            String stmtStr = "select * from Rooms where email = ?";
            PreparedStatement stmt = sqlConnection.prepareStatement(stmtStr);
            stmt.setString(1, email);
            stmt.execute();
            ResultSet rs = stmt.getResultSet();
            rs.beforeFirst();

            if (rs.next()) {
                roomMetadata = new Room();
                roomMetadata.setCountry(rs.getString(rs.findColumn("Country")));
                roomMetadata.setMetroarea(rs.getString(rs.findColumn("Metro")));
                roomMetadata.setBuilding(rs.getString(rs.findColumn("Building")));
                roomMetadata.setLatitude(Float.toString(rs.getFloat(rs.findColumn("Latitude"))));
                roomMetadata.setLongitude(Float.toString(rs.getFloat(rs.findColumn("Longitude"))));
                roomMetadata.setAddress(rs.getString(rs.findColumn("address")));
                roomMetadata.setNavigationMap(rs.getString(rs.findColumn("navigation")));
                roomMetadata.setCapacity(rs.getInt(rs.findColumn("Longitude")));
                roomMetadata.setRoomPic(rs.getString(rs.findColumn("roomPic")));
                roomMetadata.setState(rs.getString(rs.findColumn("state")));
            }

            closeConnection();

        } catch (Exception e) {
            // e.printStackTrace();
            //ApiLogger.logger.log(Level.SEVERE, "Error getting rooms from database database", e);
            throw e;
        }

        return roomMetadata;
    }
    
    /**
     * Retrieves the search criteria from the database based on the domain
     * @param domain The domain of organization to search for
     * @return The list of metro areas and their room list names
     * @throws Exception On an unexpected error.
     */ 
    public List<MetroBuildingList> retrieveSearchCriteria(String domain) throws Exception {

        Map<String, MetroBuildingList> searchFields = null;
        try {

            setupConn();

            if (sqlConnection == null || sqlConnection.isClosed()) {
                System.out.println("no connection to database");
                return null;
            }

            String stmtStr = "select email, name, Metroarea from Roomlist where domain = ?;";
            PreparedStatement stmt = sqlConnection.prepareStatement(stmtStr);
            stmt.setString(1, domain);
            stmt.execute();
            
            ResultSet rs = stmt.getResultSet();
            rs.beforeFirst();

            searchFields = new HashMap();
            
            while (rs.next()) {                
                
                String buildingName = rs.getString(rs.findColumn("name"));
                String metroName = rs.getString(rs.findColumn("Metroarea"));
                String buildingEmail = rs.getString(rs.findColumn("email"));
                
                if (searchFields.get(metroName) == null) {
                    MetroBuildingList newList = new MetroBuildingList();
                    newList.setMetro(metroName);
                    newList.setBuilding(buildingEmail, metroName);
                    searchFields.put(metroName, newList);
                } else {
                    MetroBuildingList roomLists = searchFields.get(metroName);
                    roomLists.setBuilding(buildingEmail, metroName);;
                } 
            }
            
            closeConnection();
            
            return new ArrayList(searchFields.values());

        } catch (Exception e) {
            // e.printStackTrace();
            //ApiLogger.logger.log(Level.SEVERE, "Error getting rooms from database database", e);
            throw e;
        }
    }
}
