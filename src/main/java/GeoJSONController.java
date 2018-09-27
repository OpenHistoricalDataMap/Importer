import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeoJSONController {

    ImportServlet servlet;
    
    public GeoJSONController(ImportServlet servlet)
    {
        this.servlet=servlet;
    }
    public long addGeoJSONGeoObject(String geom) throws SQLException
    {
        long id;
        
        if(geom.matches("(?s).*\"type\": \"Polygon\".*")){  
            
            final String regex = "(?s)\\\"type\\\":\\s*\\\"Polygon\\\",\\s*\"coordinates\":\\s*\\[(\\s*\\[[^\\]]*],?)*\\s*]\\s*]";
            final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
            final Matcher matcher = pattern.matcher(geom);

            if (matcher.find()) {
                geom = matcher.group(0);
            }
            System.out.println("{"+geom+"}");
            id=addGeometry("{"+geom+"}", "polygon");
            servlet.setTypeTarget(3);
        
        }else if(geom.matches("(?s).*\"type\": \"LineString\".*")){
            final String regex = "(?s)\\\"type\\\":\\s*\\\"LineString\\\",\\s*\"coordinates\":\\s*\\[(\\s*\\[[^\\]]*],?)*\\s*]";
            final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
            final Matcher matcher = pattern.matcher(geom);

            if (matcher.find()) {
                geom = matcher.group(0);
            }
            System.out.println("{"+geom+"}");
            id=addGeometry("{"+geom+"}", "point");
            servlet.setTypeTarget(2);
        
        }else if(geom.matches("(?s).*\"type\": \"Point\".*")){       
            final String regex = "(?s)\"type\": \"Point\"[^\\]]*]";
            final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
            final Matcher matcher = pattern.matcher(geom);

            if (matcher.find()) {
                geom = matcher.group(0);
            }
            id=addGeometry("{"+geom+"}", "point");
            servlet.setTypeTarget(1);

            
        }else{
            throw new IllegalArgumentException();
        }
        
        System.out.println(id);
        
        
        return id;
    }
    
    
    private long addGeometry(String geom, String type) throws SQLException
    {
        PreparedStatement statement;
        long id;
        
        statement = GisConn.conn.prepareStatement("INSERT INTO "+GisConn.schema+".\""+type+"s\" (source_user_id, "+type+")\n"
                + "VALUES ("+ImportServlet.userID+"," + " ST_GeomFromGeoJSON('" + geom + "'));", Statement.RETURN_GENERATED_KEYS);
            
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