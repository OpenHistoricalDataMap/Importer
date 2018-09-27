import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KMLController {
  
    ImportServlet servlet;
    
    public KMLController(ImportServlet servlet)
    {
        this.servlet=servlet;
    }
    
    public long addKMLGeoObject(String geom) throws SQLException
    {
        long id;
        
        if(geom.contains("Polygon")){
            
            String[] geomSplitOne = geom.split("<Polygon>");
            String[] geomSplitTwo = geomSplitOne[1].split("</Polygon>");
            System.out.println("<Polygon>"+geomSplitTwo[0]+"</Polygon>\n"+geomSplitTwo[0]);
            id=addGeometry("<Polygon>"+geomSplitTwo[0]+"</Polygon>", "polygon");
            servlet.setTypeTarget(3);

        }else if(geom.contains("LineString")){
            
            String[] geomSplitOne = geom.split("<LineString>");
            String[] geomSplitTwo = geomSplitOne[1].split("</LineString>");
            System.out.println("<LineString>"+geomSplitTwo[0]+"</LineString>\n"+geomSplitTwo[0]);
            id=addGeometry("<LineString>"+geomSplitTwo[0]+"</LineString>", "line");
            servlet.setTypeTarget(2);

            
        }else if(geom.contains("Point")){
            
            String[] geomSplitOne = geom.split("<Point>");
            String[] geomSplitTwo = geomSplitOne[1].split("</Point>");
            System.out.println("<Point>"+geomSplitTwo[0]+"</Point>\n"+geomSplitTwo[0]);
            id=addGeometry("<Point>"+geomSplitTwo[0]+"</Point>", "point");
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
                + "VALUES ("+ImportServlet.userID+"," + " ST_GeomFromKML('" + geom + "'));", Statement.RETURN_GENERATED_KEYS);
            
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
