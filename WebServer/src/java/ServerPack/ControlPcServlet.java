/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerPack;

import LibPack.MyImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author shree
 */
@WebServlet(name = "ControlPcServlet", urlPatterns = {"/ControlPcServlet"})
public class ControlPcServlet extends HttpServlet {

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
            MyImage androidData = (MyImage) in.readObject();
            in.close();

            CommonVariable.mobileWidth = androidData.mobileWidth;
            CommonVariable.mobileHeight = androidData.mobileHeight;
            //boolean idActive = false;
            int index = -1;
            //Check This PCID present in active PC id list
            for (int i = 0; i < CommonVariable.myPcIDs.size(); i++) {
                if (androidData.pcID.equalsIgnoreCase(CommonVariable.myPcIDs.get(i))) {
                    // idActive = true;
                    index = i;
                    break;
                }
            }

            MyImage allActivePcImages = new MyImage();
            if (index != -1) {
                allActivePcImages.imageArray = CommonVariable.myPcImages.get(index);
                allActivePcImages.hostAvailableStatus = 1;//Notify Android Host is available
                if (androidData.clickCmd == 1) {
                    CommonVariable.pcCmd.set(index, "Move##" + androidData.x + "##" + androidData.y);
                } else if (androidData.clickCmd == 2) {
                    CommonVariable.pcCmd.set(index, "Click##2");
                } else if (androidData.clickCmd == 3) {
                    CommonVariable.pcCmd.set(index, "Click##3");
                } else if (androidData.clickCmd == 4) {
                    CommonVariable.pcCmd.set(index, "Click##4");
                } else {
                    CommonVariable.pcCmd.set(index, "-1");
                }

            } else {
                allActivePcImages.hostAvailableStatus = -1;//Notify Android Host is unavailable
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
