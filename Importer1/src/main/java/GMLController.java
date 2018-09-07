import java.sql.*;

public class GMLController {

    static String host = "ohm.f4.htw-berlin.de";
    static String db = "ohdm_test";
    static String schema = "sose18";
    static String user = "geoserver";
    static String pass = "ohdm4ever!";

    static String geometrey = "<gml:LineString srsName=\"EPSG:4269\">\n" +
"			<gml:coordinates>\n" +
"				-71.16028,42.258729 -71.160837,42.259112 -71.161143,42.25932\n" +
"			</gml:coordinates>\n" +
"		</gml:LineString>";

    boolean showSQL=true;

    public static int addGMLGeoObject(String name, Connection conn)
    {
        System.out.println("INSERT INTO "+schema+".\"s0559289_GO\" (name, geom)\n"
                + "VALUES ('" + "test1" + "'," + " ST_GeomFromGML('"+geometrey+"'));");
        try (PreparedStatement statement = conn.prepareStatement("INSERT INTO "+schema+".\"s0559289_GO\" (name, geom)\n"
                + "VALUES ('" + "test1" + "'," + " ST_GeomFromGML('" + geometrey + "'));", Statement.RETURN_GENERATED_KEYS);) {

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
