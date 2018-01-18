
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
        out.close();

    }

}
