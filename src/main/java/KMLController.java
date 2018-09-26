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
            
            String[] geomSplitOne = geom.split("<Polygon>");
            String[] geomSplitTwo = geomSplitOne[1].split("</Polygon>");
            System.out.println("<Polygon>"+geomSplitTwo[0]+"</Polygon>\n"+geomSplitTwo[0]);
            id=addGeometry("<Polygon>"+geomSplitTwo[0]+"</Polygon>", "polygon");

        }else if(geom.contains("LineString")){
            
            String[] geomSplitOne = geom.split("<LineString>");
            String[] geomSplitTwo = geomSplitOne[1].split("</LineString>");
            System.out.println("<LineString>"+geomSplitTwo[0]+"</LineString>\n"+geomSplitTwo[0]);
            id=addGeometry("<LineString>"+geomSplitTwo[0]+"</LineString>", "line");
            
        }else if(geom.contains("Point")){
            
            String[] geomSplitOne = geom.split("<Point>");
            String[] geomSplitTwo = geomSplitOne[1].split("</Point>");
            System.out.println("<Point>"+geomSplitTwo[0]+"</Point>\n"+geomSplitTwo[0]);
            id=addGeometry("<Point>"+geomSplitTwo[0]+"</Point>", "point");
            
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
