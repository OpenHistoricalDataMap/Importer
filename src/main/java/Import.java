
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Smady91
 */
@WebServlet("/Import")
public class Import extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String ObjectName = request.getParameter("ObjectName");
        String UserID = request.getParameter("UserID");
        String validSince = request.getParameter("Valid-Since");
        String validUntil = request.getParameter("Valid-Until");
        String OSM_Feature_Name = request.getParameter("OSM_Feature_Name");
        String OSM_Feature_Value = request.getParameter("OSM_Feature_Value");
        String JSON = request.getParameter("JSON");

        JSONController jSONController = new JSONController();
        try {
            jSONController.parserJSON(JSON);
            out.print("Successfully Imported...");
        } catch (SQLException ex) {
            Logger.getLogger(Import.class.getName()).log(Level.SEVERE, null, ex);
            out.print("Error ..." + ex);
        } catch (Exception ex) {
            Logger.getLogger(Import.class.getName()).log(Level.SEVERE, null, ex);
            out.print("Error ..." + ex);
        }
        /*
        out.print("Successfully Imported...");
        out.print("<br/>ObjectName: " + ObjectName);
        out.print("<br/>UserID: " + UserID);
        out.print("<br/>Valid Since: " + validSince);
        out.print("<br/>Valid Until: " + validUntil);
        out.print("<br/>OSM_Feature_Name: " + OSM_Feature_Name);
        out.print("<br/>OSM_Feature_Value: " + OSM_Feature_Value);
        out.print("<br/>JSON: " + JSON);*/
        out.close();

    }

}
