
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.OutputStreamOutStream;
import com.vividsolutions.jts.io.WKBWriter;
import com.vividsolutions.jts.io.WKTReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author kal
 */
public class JTS {
    
        
    public static byte[] GeomFromText(String wkt, int srid) throws com.vividsolutions.jts.io.ParseException {
    if ( wkt == null ) {
        return null;
    }
    WKTReader reader = new WKTReader();
    Geometry g = reader.read( wkt );
    g.setSRID(srid);
    return toWKB( g );
}
    
    
       private static byte[] toWKB( Geometry g ) { 
        try { 
            WKBWriter w = new WKBWriter(); 
             
            //write the geometry 
            ByteArrayOutputStream bytes = new ByteArrayOutputStream(); 
            w.write( g , new OutputStreamOutStream( bytes ) ); 
    
            //supplement it with the srid 
            int srid = g.getSRID(); 
            bytes.write( (byte)(srid >>> 24) ); 
            bytes.write( (byte)(srid >> 16 & 0xff) ); 
            bytes.write( (byte)(srid >> 8 & 0xff) ); 
            bytes.write( (byte)(srid & 0xff) ); 
             
            return bytes.toByteArray(); 
        }  
        catch (IOException e) { 
            throw new RuntimeException( e ); 
        } 
    } 
       
         private final  char[] hexArray = "0123456789ABCDEF".toCharArray();

    public  String bytesToHex(byte[] bytes) {
    char[] hexChars = new char[bytes.length * 2];
    for ( int j = 0; j < bytes.length; j++ ) {
        int v = bytes[j] & 0xFF;
        hexChars[j * 2] = hexArray[v >>> 4];
        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
    }
    return new String(hexChars);
}
}
