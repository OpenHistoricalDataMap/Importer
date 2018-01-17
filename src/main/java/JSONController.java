
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
                    lastGeoobjectID = gisconn.add(lastGeoobjectID, name, type, jsonToWKT(type, coordinates), valid_since, valid_until, userId, osm_feature_name, osm_feature_value);
                } else {
                    lastGeoobjectName = name;
                    lastGeoobjectID = gisconn.add(geom_id, name, type, jsonToWKT(type, coordinates), valid_since, valid_until, userId, osm_feature_name, osm_feature_value);
                }
            }

        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    public static String jsonToWKT(String type, String json) {
        //str = str.replaceAll("[\\[\\]]", "");
        String[] jsonParts = json.split("\\]\\,\\[");
        for (int i = 0; i < jsonParts.length; i++) {
            jsonParts[i] = jsonParts[i].replaceAll("\\[", "");
            jsonParts[i] = jsonParts[i].replaceAll("\\]", "");
            jsonParts[i] = jsonParts[i].replaceAll(",", " ");
        }
        String jsonJoined = String.join(",", jsonParts);
        return "GeomFromEWKT('" + type + "((" + jsonJoined + "))')";
    }

}
