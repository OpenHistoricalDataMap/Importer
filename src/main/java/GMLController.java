import java.sql.*;

public class GMLController {

    ImportServlet servlet;
    
    public GMLController(ImportServlet servlet)
    {
        this.servlet=servlet;
    }
    public long addGMLGeoObject(String geom) throws SQLException
    {
        long id;
        
        if(geom.matches("(?s).*<gml.Polygon[^>]*>.*")){           
            String[] geomSplitOne = geom.split("<gml:Polygon[^>]*>");
            String[] geomSplitTwo = geomSplitOne[1].split("</gml:Polygon[^>]*>");
            System.out.println("<gml:Polygon>"+geomSplitTwo[0]+"</gml:Polygon>\n"+geomSplitTwo[0]);
            id=addGeometry("<gml:Polygon>"+geomSplitTwo[0]+"</gml:Polygon>", "polygon");
            servlet.setTypeTarget(3);
        
        }else if(geom.matches("(?s).*<gml:LineString[^>]*>.*")){
            String[] geomSplitOne = geom.split("<gml:LineString[^>]*>");
            String[] geomSplitTwo = geomSplitOne[1].split("</gml:LineString[^>]*>");
            System.out.println("<gml:LineString>"+geomSplitTwo[0]+"</gml:LineString>\n"+geomSplitTwo[0]);
            id=addGeometry("<gml:LineString>"+geomSplitTwo[0]+"</gml:LineString>", "line");
            servlet.setTypeTarget(2);
        
        }else if(geom.matches("(?s).*<gml:Point[^>]*>.*")){       
            String[] geomSplitOne = geom.split("<gml:Point[^>]*>");
            String[] geomSplitTwo = geomSplitOne[1].split("</gml:Point[^>]*>");
            System.out.println("<gml:Point>"+geomSplitTwo[0]+"</gml:Point>\n"+geomSplitTwo[0]);
            id=addGeometry("<gml:Point>"+geomSplitTwo[0]+"</gml:Point>", "point");
            servlet.setTypeTarget(1);
            
        }else{
            throw new IllegalArgumentException();
        }             
        return id;
    }
    
    
    private long addGeometry(String geom, String type) throws SQLException
    {
        PreparedStatement statement;
        long id;
        
        statement = GisConn.conn.prepareStatement("INSERT INTO "+GisConn.schema+".\""+type+"s\" (source_user_id, "+type+")\n"
                + "VALUES ("+ImportServlet.userID+"," + " ST_GeomFromGML('" + geom + "'));", Statement.RETURN_GENERATED_KEYS);
            
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
