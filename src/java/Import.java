
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 *
 * @author Smady91
 */
@WebServlet("/Import")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 10, // 10 MB 
        maxFileSize = 1024 * 1024 * 50, // 50 MB
        maxRequestSize = 1024 * 1024 * 100)   	// 100 MB
public class Import extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String type = request.getParameter("Type");
        String objectID = request.getParameter("ObjectID");
        String validSince = request.getParameter("Valid-Since");
        String validUntil = request.getParameter("Valid-Until");
        String URL = request.getParameter("URL");
         String JSON = request.getParameter("JSON");

        System.out.println(type);
        System.out.println(objectID);
        System.out.println(validSince);
        System.out.println(validUntil);
        if (type.equals("url")) {
            System.out.println(URL);
        } else if (type.equals("Content")) {
            for (Part part : request.getParts()) {
                part.write(objectID);
            }
        } else {
         System.out.println(JSON);
        }

        out.print("Successfully Imported...");
        out.print("<br/>Type: " + type);
        out.print("<br/>ObjectID: " + objectID);
        out.print("<br/>Valid Since: " + validSince);
        out.print("<br/>Valid Until: " + validUntil);
        if (type.equals("url")) {
            out.print("<br/>URL: " + URL);
        }
        if (type.equals("Geometry")) {
            out.print("<br/>JSON: " + JSON);
        }
        out.close();

    }

}
