
import java.sql.*;
import java.util.*;

/**
 *
 * @author Smady91
 */
public class GisConn {

    static String host = "ohm.f4.htw-berlin.de";
    static String db = "ohdm_integration";
    static String schema = "Berlin";
    static String user = "geoserver";
    static String pass = "ohdm4ever!";

    public static Connection conn = null;

    public static void main(String[] args) throws SQLException {
        GisConn gisConn = new GisConn();
        gisConn.setConn();
        System.out.println(gisConn.getClassificationID("shop", "alcohol"));

    }

    public void setConn() throws SQLException {
        String url = "jdbc:postgresql://ohm.f4.htw-berlin.de/ohdm_integration";
        Properties props = new Properties();
        props.setProperty("user", user);
        props.setProperty("password", pass);
        props.setProperty("currentSchema", schema);
        //   props.setProperty("ssl", "true");
        conn = DriverManager.getConnection(url, props);
    }

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

    public int add(int geoobjectID, String geoobjectName, String type, String coordinates, String valid_since, String valid_until, String userID, String osm_feature_name, String osm_feature_value) throws SQLException, Exception {
        try {

            int classificationID = getClassificationID(osm_feature_name, osm_feature_value);
            int targetID = -1;
            int targetTypeID = -1;

            conn.setAutoCommit(false);

            if (geoobjectID == -1) {
                geoobjectID = addGeoObject(geoobjectName, userID);
            }

            switch (type) {
                case "POINT":
                    targetTypeID = 1;
                    targetID = addPoint(coordinates, userID);
                    break;
                case "LINE":
                    targetTypeID = 2;
                    targetID = addLine(coordinates, userID);
                    break;
                case "POLYGON":
                    targetTypeID = 3;
                    targetID = addPolygon(coordinates, userID);
                    break;
            }

            addGeoobject_Geometry(targetID, targetTypeID, geoobjectID, classificationID, valid_since, valid_until, userID);

            conn.commit();
            return geoobjectID;
        } catch (Exception e) {
            String errorMessage = "An exception occoured while trying to create.";
            try {
                conn.rollback();
            } catch (Exception ee) {
                errorMessage += " A futher exception occoured when trying to rollback the transaction";
            }
            throw new Exception(errorMessage, e);
        }

    }

    public int addGeoObject(String name, String userID) throws SQLException {
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

    public int addPoint(String point, String userID) throws SQLException {
        try (PreparedStatement statement = conn.prepareStatement("INSERT INTO " + schema + ".points(point, source_user_id)\n"
                + "VALUES ('" + point + "', '" + userID + "');", Statement.RETURN_GENERATED_KEYS);) {

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

    public int addLine(String line, String userID) throws SQLException {
        try (PreparedStatement statement = conn.prepareStatement("INSERT INTO " + schema + ".lines(line, source_user_id)\n"
                + "VALUES (" + line + ",' " + userID + "');", Statement.RETURN_GENERATED_KEYS);) {

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

    public int addPolygon(String polygon, String userID) throws SQLException {
        try (PreparedStatement statement = conn.prepareStatement("INSERT INTO " + schema + ".polygons(polygon, source_user_id)\n"
                + "VALUES (" + polygon + ", '" + userID + "');", Statement.RETURN_GENERATED_KEYS);) {

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

    public void addGeoobject_Geometry(int targetID, int targetTypeID, int geoobjectID, int classificationID, String valid_since, String valid_until, String userID) throws SQLException {
        Statement statement = conn.createStatement();
        statement.executeUpdate("INSERT INTO " + schema + ".geoobject_geometry(id_target, type_target, id_geoobject_source, classification_id, valid_since, valid_until, source_user_id)\n"
                + "VALUES (" + targetID + ", " + targetTypeID + ", " + geoobjectID + ", " + classificationID + ", " + valid_since + ", " + valid_until + ", " + userID + ");");
    }

}
