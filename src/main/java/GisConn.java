    
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 * @author Nabeel smadi
 * @author Khaled Halabieh
 */
public class GisConn {

    static String host = "ohm.f4.htw-berlin.de";
    static String db = "ohdm_test";
    static String schema = "sose18";
    static String user = "geoserver";
    static String pass = "ohdm4ever!";

    public static Connection conn = null;

    public static boolean showSQL = true;

    public static void main(String[] args) throws SQLException {
        GisConn gisConn = new GisConn();
        gisConn.setConn();
        KMLControlller.addKMLGeoObject("hallo", conn);
    }
    
    /**
     * Verbindung mit Datenbank erstellen
     * @throws SQLException 
     * 
     */

    public void setConn() throws SQLException {
        String url = "jdbc:postgresql://ohm.f4.htw-berlin.de/ohdm_test"; //todo zurückändern
        Properties props = new Properties();
        props.setProperty("user", user);
        props.setProperty("password", pass);
        props.setProperty("currentSchema", schema);
        //   props.setProperty("ssl", "true");
        conn = DriverManager.getConnection(url, props);
        System.out.println("Connection Success");
    }
    /**
     * Id von Berlin.classification abholen,
     *
     * @param name
     * @param value
     * @return ID des classification, oder -1 falls es nicht gefunden wurde.
     * @throws SQLException 
     */

    public int getClassificationID(String name, String value) throws SQLException {
        Statement stat = conn.createStatement();
        {
            ResultSet rs = stat.executeQuery("SELECT id FROM " + schema + ".classification where class='" + name + "' and subclassname='" + value + "';");
            if (rs.next()) {
                return Integer.parseInt(rs.getString(1));
            } else {
                return -1;
            }
        }
    }
    
    /**
     * geoobject  erstellen , 
     * 
     * @param geoobjectID
     * @param geoobjectName
     * @param type
     * @param coordinates
     * @param valid_since
     * @param valid_until
     * @param userID
     * @param osm_feature_name
     * @param osm_feature_value
     * @return Id des neuen geoobjects
     * @throws SQLException
     * @throws Exception 
     */

    public int add(int geoobjectID, String geoobjectName, String type, String coordinates, String valid_since, String valid_until, String userID, String osm_feature_name, String osm_feature_value) throws SQLException, Exception {
        try {

            int classificationID = getClassificationID(osm_feature_name, osm_feature_value);
            System.out.println("getClassificationID class=" + osm_feature_name + "' and subclassname='" + osm_feature_value + " ID=" + classificationID);
            int targetID = -1;
            int targetTypeID = -1;

            conn.setAutoCommit(false);

            if (geoobjectID == -1) {
                geoobjectID = addGeoObject(geoobjectName, userID);
                System.out.println("geoobjectID =" + geoobjectID);

            }

            switch (type) {
                case "POINT":
                    targetTypeID = 1;
                    targetID = addPoint(coordinates, userID);
                    System.out.println("POINTID =" + targetID);
                    break;
                case "LINESTRING":
                    targetTypeID = 2;
                    targetID = addLine(coordinates, userID);
                    System.out.println("LINE =" + targetID);
                    break;
                case "POLYGON":
                    targetTypeID = 3;
                    targetID = addPolygon(coordinates, userID);
                    System.out.println("POLYGON =" + targetID);
                    break;
            }

            addGeoobject_Geometry(targetID, targetTypeID, geoobjectID, classificationID, valid_since, valid_until, userID);
            System.out.println("vor commit");
            conn.commit();
            System.out.println("commiting ");

            return geoobjectID;
        } catch (Exception e) {
            String errorMessage = "An exception occoured while trying to create.";
            try {
                conn.rollback();
            } catch (Exception ee) {
                errorMessage += " A further exception occoured when trying to rollback the transaction";
            }
            System.out.println("errorMessage: " + e);
            throw new Exception(errorMessage, e);
        }

    }

    /**
     * neues GeoObject in der Tabelle Berlin.geoobject erstellen 
     * 
     * @param name
     * @param userID
     * @return die neue ID
     * @throws SQLException 
     */
    public int addGeoObject(String name, String userID) throws SQLException {
        if (showSQL) {
            System.out.println("INSERT INTO " + schema + ".geoobject(name, source_user_id) VALUES ('" + name + "',' " + userID + "');");
        }
        try (PreparedStatement statement = conn.prepareStatement("INSERT INTO " + schema + ".geoobject(name, source_user_id)\n"
                + "VALUES ('" + name + "',' " + userID + "');", Statement.RETURN_GENERATED_KEYS);) {

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt("id");
                } else {
                    throw new SQLException("Creating failed, no ID obtained.");
                }
            }
        }

    }

/**
 * neuen punkt erstellen
 * 
 * @param point
 * @param userID
 * @return ID des neuen Punkts
 * @throws SQLException falls ein fehler beim erstellen ginbt
 */    

    public int addPoint(String point, String userID) throws SQLException {
        String sqlStatement = "INSERT INTO " + schema + ".points(point, source_user_id) VALUES (" + point + ", '" + userID + "');";
        //  String sqlStatement = "INSERT INTO " + schema + ".points(source_user_id) VALUES ('9999');";
        if (showSQL) {
//            System.out.println("INSERT INTO " + schema + ".points(point, source_user_id) VALUES (" + point + ", '" + userID + "');");
            System.out.println(sqlStatement);
        }

        try (PreparedStatement statement = conn.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);) {

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt("id");
                } else {
                    throw new SQLException("Creating failed, no ID obtained.");
                }
            }
        }
    }

    /**
     * neue Line erstellen
     * @param line
     * @param userID
     * @return Id der linie
     * @throws SQLException 
     */
    public int addLine(String line, String userID) throws SQLException {
        String sqlStatement = "INSERT INTO " + schema + ".lines(line, source_user_id) VALUES (" + line + ",' " + userID + "');";
        try (PreparedStatement statement = conn.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);) {
            if (showSQL) {
//            System.out.println("INSERT INTO " + schema + ".points(point, source_user_id) VALUES (" + point + ", '" + userID + "');");
                System.out.println(sqlStatement);
            }
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt("id");
                } else {
                    throw new SQLException("Creating failed, no ID obtained.");
                }
            }
        }
    }
    
    /**
     * Neue Poligon erstellen
     * @param polygon
     * @param userID
     * @return Id des Polygons
     * @throws SQLException 
     */

    public int addPolygon(String polygon, String userID) throws SQLException {
        String sqlStatement = "INSERT INTO " + schema + ".polygons(polygon, source_user_id) VALUES (" + polygon + ", '" + userID + "');";

        try (PreparedStatement statement = conn.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);) {

            if (showSQL) {
//            System.out.println("INSERT INTO " + schema + ".points(point, source_user_id) VALUES (" + point + ", '" + userID + "');");
                System.out.println(sqlStatement);
            }

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt("id");
                } else {
                    throw new SQLException("Creating failed, no ID obtained.");
                }
            }
        }
    }
    /**
     * 
     * @param targetID
     * @param targetTypeID
     * @param geoobjectID
     * @param classificationID
     * @param valid_since
     * @param valid_until
     * @param userID
     * @throws SQLException
     * @throws ParseException 
     */

    public void addGeoobject_Geometry(int targetID, int targetTypeID, int geoobjectID, int classificationID, String valid_since, String valid_until, String userID) throws SQLException, ParseException {
        String sqlStatement = "INSERT INTO " + schema + ".geoobject_geometry(id_target, type_target, id_geoobject_source, classification_id, valid_since, valid_until, source_user_id) VALUES (" + targetID + ", " + targetTypeID + ", " + geoobjectID + ", " + classificationID + ", '"+valid_since+"', '"+valid_until+"', " + userID + ");";
        if (showSQL) {
           System.out.println(sqlStatement);
        }
        PreparedStatement statement = conn.prepareStatement(sqlStatement);
        statement.executeUpdate();

    }
}
