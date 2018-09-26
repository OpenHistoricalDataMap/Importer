/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author s0558521
 */
public class ImportServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            String geom = request.getParameter("geom");
            String format = request.getParameter("formats");
            String name = request.getParameter("name");
            String validSince = request.getParameter("begindate");
            String validUntil = request.getParameter("enddate");
            
            try {
                GisConn.setConn();
            } catch (SQLException ex) {
                ex.printStackTrace();
                Logger.getLogger(ImportServlet.class.getName()).log(Level.SEVERE, null, ex);
            } catch(Exception e){
                    e.printStackTrace();
                    }
            
            long id=-1;
            
            try {
            switch (format) {
                case "kml":
            {
                
                    id=KMLController.addKMLGeoObject(geom);
                
            }
                    break;
                case "gml":
                    //GMLController.addGMLGeoObject(geom);
                    break;
                case "json":
                    //GeoJSONController.addGeoJSONGeoObject(geom);
                    break;
            } 
            } catch (SQLException ex) {
                    Logger.getLogger(ImportServlet.class.getName()).log(Level.SEVERE, null, ex);
                                GisConn.closeConn();

                }
            GisConn.closeConn();
            out.print(id);
            //response.sendRedirect("index.html");
        }
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
