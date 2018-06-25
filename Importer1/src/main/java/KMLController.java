import java.io.PrintWriter;
import java.sql.*;

public class KMLController {

    static String schema = "sose2018";

    public static void addKMLGeoObject(String name, String geom)
    {
        System.out.println("INSERT INTO "+GisConn.schema+".\"importtest\" (name, geom)\n"
                + "VALUES ('" + name + "'," + " ST_GeomFromKML('"+geom+"'));");
        try (PreparedStatement statement = GisConn.conn.prepareStatement("INSERT INTO "+schema+".\"importtest\" (name, geom)\n"
                + "VALUES ('" + name+ "'," + " ST_GeomFromKML('" + geom + "'));", Statement.RETURN_GENERATED_KEYS);) {
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating failed, no rows affected.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
