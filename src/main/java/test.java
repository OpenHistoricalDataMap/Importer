
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
        //GisConn gisconn = new GisConn();
        //gisconn.setConn();
        //int pointID = gisconn.addPoint("ST_GeomFromText('POINT(13.325719 52.513985)')", "401");
        //  int pointID = gisconn.addPoint("GeomFromEWKT(’SRID=4326;POINT(13.325719 52.513985)’)", "401");
        // int pointID = gisconn.addPoint("GeomFromEWKT('POINT(13.325719 52.513985)')", "401");
        //  int pointID = gisconn.addPoint("ST_MakePoint(13.325719, 52.513985)", "401");
        // System.out.println(pointID);

        String polygon = "[[[50.6373,3.0750],[50.6374,3.0750],[50.6374,3.0749],[50.63,3.07491],[50.6373,3.0750]]]";
        System.out.println(JSONController.jsonToWKT("Polygon", polygon));
        System.out.println(polygon);
        System.out.println("------------------");
        String line = "[[50.6373,3.0750],[50.6374,3.0750],[50.6374,3.0749],[50.63,3.07491],[50.6373,3.0750]]";
        System.out.println(JSONController.jsonToWKT("LineString", line));
        System.out.println(line);
        System.out.println("------------------");

    }
}
