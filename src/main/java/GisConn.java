import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Nabeel smadi
 * @author Khaled Halabieh
 */
public class GisConn {

    static String host = "ohm.f4.htw-berlin.de";
    //static String host = "ohm.f4.htw-berlin.de";
    static String db = "ohdm_public";
    //static String db = "ohdm_test";
    static String schema = "sose2018";
    //static String schema = "sose18";
    static String user = "geoserver";
    //static String user = "geoserver";
    static String pass = "ohdm4ever!";
    //static String pass = "ohdm4ever!";

    public static Connection conn = null;

    public static boolean showSQL = true;

    public static void main(String[] args)
    {
        try {
            setConn();
<<<<<<< HEAD
            KMLController.addKMLGeoObject("testsoda", "<coordinates>-71.1663,42.2614 \n" +
                    "<LineString>\n" +
                    "-71.1667,42.2616</coordinates>\n" +
                    "</LineString>");
=======
            KMLController.addKMLGeoObject("testsoda", "<LineString>\n" +
                    "			<coordinates>-71.1663,42.2614 \n" +
                    "				-71.1667,42.2616</coordinates>\n" +
                    "		</LineString>");
>>>>>>> 287cc27df40a95035ff01874612c14064165caf8
        } catch (SQLException ex) {
            Logger.getLogger(GisConn.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    /**
     * Verbindung mit Datenbank erstellen
     * @throws SQLException 
     * 
     */

    public static void setConn() throws SQLException{
        
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://ohm.f4.htw-berlin.de/ohdm_test"; //todo zurückändern
            Properties props = new Properties();
            props.setProperty("user", user);
            props.setProperty("password", pass);
            props.setProperty("currentSchema", schema);
            //   props.setProperty("ssl", "true");
            conn = DriverManager.getConnection(url, props);
            System.out.println("Connection Success");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GisConn.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void closeConn() throws SQLException{
        conn.close();
    }
    
    /**
     * Id von Berlin.classification abholen,
     *
     * @param name
     * @param value
     * @return ID des classification, oder -1 falls es nicht gefunden wurde.
     * @throws SQLException 
     */

    public int getClassificationID(String name, String value) throws SQLException {
        Statement stat = conn.createStatement();
        {
            ResultSet rs = stat.executeQuery("SELECT id FROM " + schema + ".classification where class='" + name + "' and subclassname='" + value + "';");
            if (rs.next()) {
                return Integer.parseInt(rs.getString(1));
            } else {
                return -1;
            }
        }
    }
    
    /**
     * geoobject  erstellen , 
     * 
     * @param geoobjectID
     * @param geoobjectName
     * @param type
     * @param coordinates
     * @param valid_since
     * @param valid_until
     * @param userID
     * @param osm_feature_name
     * @param osm_feature_value
     * @return Id des neuen geoobjects
     * @throws SQLException
     * @throws Exception 
     */

   
}
