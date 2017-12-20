
import java.sql.SQLException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Smady91
 */
public class test {

    public static void main(String[] args) throws SQLException {
        GisConn gisconn = new GisConn();
        gisconn.setConn();
        int pointID = gisconn.addPoint("ST_GeomFromText('POINT(-71.060316 48.432044)')", "401");
        System.out.println(pointID);
    }

}
