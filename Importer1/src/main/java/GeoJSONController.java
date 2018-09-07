import java.sql.*;

public class GeoJSONController {

    static String host = "ohm.f4.htw-berlin.de";
    static String db = "ohdm_test";
    static String schema = "sose18";
    static String user = "geoserver";
    static String pass = "ohdm4ever!";

    static String geometrey = "{\"type\":\"Point\",\"coordinates\":[-48.23456,20.12345]}";

    boolean showSQL=true;

    public static int addGeoJSONGeoObject(String name, Connection conn)
    {
        System.out.println("INSERT INTO "+schema+".\"s0559289_GO\" (name, geom)\n"
                + "VALUES ('" + "test1" + "'," + " ST_GeomFromGeoJSON('"+geometrey+"'));");
        try (PreparedStatement statement = conn.prepareStatement("INSERT INTO "+schema+".\"s0559289_GO\" (name, geom)\n"
                + "VALUES ('" + "test1" + "'," + " ST_GeomFromGeoJSON('" + geometrey + "'));", Statement.RETURN_GENERATED_KEYS);) {

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
}