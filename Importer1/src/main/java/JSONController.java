
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import org.json.simple.JSONArray;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
/**
 * 
 * @author Khaled Halabieh
 * @author Nabeel Smadi
 */


public class JSONController {

    private String type = null;
    private String coordinates = null;
    private int geom_id = -1;
    private String name = null;
    private String userId = null;
    private String valid_since = null;
    private String valid_until = null;
    private String osm_feature_name = null;
    private String osm_feature_value = null;

    private int lastGeoobjectID = -2;
    private String lastGeoobjectName = "";

    
    /**
     * json text zu jsonArray  umwandelen .
     * @param jsonText
     * @throws ParseException
     * @throws FileNotFoundException
     * @throws IOException
     * @throws SQLException
     * @throws Exception 
     */
    void parserJSON(String jsonText) throws ParseException, FileNotFoundException, IOException, SQLException, Exception {
        try {
            GisConn gisconn = new GisConn();
            gisconn.setConn();

            // read the json file
            // FileReader reader = new FileReader("GEO.json");
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonText);

            // get an array from the JSON object
            JSONArray List = (JSONArray) jsonObject.get("features");
            for (int i = 0; i < List.size(); i++) {
                JSONObject feature = (JSONObject) List.get(i);

                JSONObject geometry = (JSONObject) feature.get("geometry");
                JSONObject properties = (JSONObject) feature.get("properties");
                type = geometry.get("type").toString().toUpperCase();
                coordinates = geometry.get("coordinates").toString();
                geom_id = Integer.parseInt(properties.get("geom_id").toString());
                userId = properties.get("userId").toString();
                name = properties.get("name").toString();
                valid_since = properties.get("valid_since").toString();
                valid_until = properties.get("valid_until").toString();
                osm_feature_name = properties.get("osm_feature_name").toString();
                osm_feature_value = properties.get("osm_feature_value").toString();
                if (lastGeoobjectID != -2 && name.equals(lastGeoobjectName)) {
                    lastGeoobjectName = name;
                    //lastGeoobjectID = gisconn.add(lastGeoobjectID, name, type, toWKBFinal(type, coordinates), valid_since, valid_until, userId, osm_feature_name, osm_feature_value);
                } else {
                    lastGeoobjectName = name;
                    //lastGeoobjectID = gisconn.add(geom_id, name, type, toWKBFinal(type, coordinates), valid_since, valid_until, userId, osm_feature_name, osm_feature_value);
                }
            }

        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    /**
     *  koordinaten zu Well-known text umwandelen .
     * @param type
     * @param json
     * @return Well-known text
     */
    public static String jsonToWKT(String type, String json) {
        String[] jsonParts = json.split("\\]\\,\\[");
        for (int i = 0; i < jsonParts.length; i++) {
            jsonParts[i] = jsonParts[i].replaceAll("\\[", "");
            jsonParts[i] = jsonParts[i].replaceAll("\\]", "");
            jsonParts[i] = jsonParts[i].replaceAll(",", " ");
        }
        
        String jsonJoined = String.join(",", jsonParts);
        String result = "";
    if (type.equalsIgnoreCase("Polygon")) {
            result =  type + "((" + jsonJoined + "))";
        } else {
            result =  type + "(" + jsonJoined + ")";
        }
        return result;
    }
    /**
     * WKB Richtig formatieren 
     * 
     * @param type
     * @param coordinates
     * @return 
     * @throws com.vividsolutions.jts.io.ParseException 
     */
    public static String toWKBFinal(String type , String coordinates) throws com.vividsolutions.jts.io.ParseException {
        JTS jts = new JTS();
        
           byte [] poi= jts.GeomFromText(jsonToWKT(type, coordinates), 0);    

                StringBuilder sb = new StringBuilder();
                sb.append("'");
                sb.append(jts.bytesToHex(poi));
                sb.append("'");
                        
                return sb.toString();
                
    }

}
