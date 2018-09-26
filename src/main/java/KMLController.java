import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KMLController {

    static String host = "ohm.f4.htw-berlin.de";
    static String db = "ohdm_test";
    static String schema = GisConn.schema;
    static String user = "geoserver";
    static String pass = "ohdm4ever!";
    
    static String geometrey = "<LineString>\n<coordinates>-23.02,32.43\n-71.16,42.23</coordinates>\n</LineString>";

    boolean showSQL=true;

    public static long addKMLGeoObject(String geom) throws SQLException
    {
        long id;
        
        if(geom.contains("Polygon")){
            id=addGeometry(geom, "polygon");
        }else if(geom.contains("LineString")){
            id=addGeometry(geom, "line");
        }else if(geom.contains("Point")){
            id=addGeometry(geom, "point");
        }else{
            throw new IllegalArgumentException();
        }
        
        System.out.println(id);
        
        
        return id;
    }
    
    
    private static long addGeometry(String geom, String type) throws SQLException
    {
        PreparedStatement statement;
        long id;
        
        statement = GisConn.conn.prepareStatement("INSERT INTO "+schema+".\""+type+"s\" (source_user_id, "+type+")\n"
                + "VALUES (100," + " ST_GeomFromKML('" + geom + "'));", Statement.RETURN_GENERATED_KEYS);
            
        int affectedRows = statement.executeUpdate();

                    
        if (affectedRows == 0) {
            throw new SQLException("Creating user failed, no rows affected.");
        }        
        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                id=generatedKeys.getLong(1);
            }
            else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
        }
        return id;
    }
}
