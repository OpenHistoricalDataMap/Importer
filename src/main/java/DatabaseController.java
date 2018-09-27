
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author David Linke
 */
public class DatabaseController {
    
    public long addGeoObject(String name) throws SQLException
    {
        if(name==null||name.equals(""))
            return 0;
        
        PreparedStatement statement;
        long id;
        
        statement = GisConn.conn.prepareStatement("INSERT INTO "+GisConn.schema+".\"geoobject\" (source_user_id, name)\n"
                + "VALUES ("+ImportServlet.userID+"," + " '"+name+"');", Statement.RETURN_GENERATED_KEYS);
            
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
    
    public long addGeoObjectGeometry(long idTarget, int typeTarget, long idGeoObjectSource, long classificationId, String validSince, String validUntil) throws SQLException
    {      
        PreparedStatement statement;
        long id;
        
        statement = GisConn.conn.prepareStatement("INSERT INTO "+GisConn.schema+".\"geoobject_geometry\" (id_target, type_target, id_geoobject_source, classification_id, valid_since, valid_until, valid_since_offset, valid_until_offset, source_user_id)\n"
                + "VALUES ("+idTarget+","+typeTarget+","+idGeoObjectSource+","+classificationId+",'"+validSince+"','"+validUntil+"',0,0,"+ImportServlet.userID+");", Statement.RETURN_GENERATED_KEYS);
            
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
