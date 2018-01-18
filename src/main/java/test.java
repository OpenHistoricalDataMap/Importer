
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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

    public static void main(String[] args) throws SQLException, IOException, FileNotFoundException, Exception {
        GisConn gisconn = new GisConn();
        gisconn.setConn();
        String content = new String(Files.readAllBytes(Paths.get("GEO.json")));

        JSONController jSONController = new JSONController();
        jSONController.parserJSON(content);
   
    }
    
    
}
