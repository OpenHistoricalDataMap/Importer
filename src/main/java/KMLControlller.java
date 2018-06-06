import java.sql.*;

public class KMLControlller {

    static String host = "ohm.f4.htw-berlin.de";
    static String db = "ohdm_test";
    static String schema = "sose18";
    static String user = "geoserver";
    static String pass = "ohdm4ever!";

    static String geometray = "<LineString>\n<coordinates>-23.02,32.43\n-71.16,42.23</coordinates>\n</LineString>";

    boolean showSQL=true;

    public static int addKMLGeoObject(String name, Connection conn)
    {
        if (true) {
            //System.out.println("INSERT INTO " + schema + ".s0559289_GO(name, source_user_id) VALUES ('" + name + "',' " + userID + "');");
        //DECLARE geometrie geometry;\nBEGIN geometrie := ST_GeomFromKML("+geometray+")\n
        }
        System.out.println("INSERT INTO "+schema+".\"s0559289_GO\" (name, geom)\n"
                + "VALUES ('" + "test1" + "'," + " ST_GeomFromKML('"+geometray+"'));");
        try (PreparedStatement statement = conn.prepareStatement("INSERT INTO "+schema+".\"s0559289_GO\" (name, geom)\n"
                + "VALUES ('" + "test1" + "'," + " ST_GeomFromKML('" + geometray + "'));", Statement.RETURN_GENERATED_KEYS);) {

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    //return generatedKeys.getInt("id");
                } else {
                    throw new SQLException("Creating failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
