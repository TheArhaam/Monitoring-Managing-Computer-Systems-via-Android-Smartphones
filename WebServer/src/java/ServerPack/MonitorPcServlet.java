/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerPack;

import LibPack.ImageData;
import LibPack.MyImage;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author shree
 */
@WebServlet(name = "MonitorPcServlet", urlPatterns = {"/MonitorPcServlet"})
public class MonitorPcServlet extends HttpServlet {

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
        try {
            String format = "jpg";

            //Accept Client Request
            ObjectInputStream in = new ObjectInputStream(request.getInputStream());
            String pcID = (String) in.readObject();
            in.close();

            //boolean idActive = false;
            int index = -1;
            //Check This PCID present in active PC id list
            for (int i = 0; i < CommonVariable.myPcIDs.size(); i++) {
                if (pcID.equalsIgnoreCase(CommonVariable.myPcIDs.get(i))) {
                    // idActive = true;
                    index = i;
                    break;
                }
            }

            MyImage allActivePcImages = new MyImage();
            if (index != -1) {
                //System.out.println("Index: " + index + "  PcId: " + mySnap.pcID);
                allActivePcImages.imageArray = CommonVariable.myPcImages.get(index);
                allActivePcImages.hostAvailableStatus = 1;
                //CommonVariable.myPcImages.add(index, mySnap.imageArray);
            } else {
                allActivePcImages.hostAvailableStatus = -1;
            }


            //Send Data back to client
            ObjectOutputStream out = new ObjectOutputStream(response.getOutputStream());
            // receive and deserialize the object, note the cast
            out.writeObject(allActivePcImages);
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
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
