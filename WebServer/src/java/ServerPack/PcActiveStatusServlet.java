/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerPack;

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
@WebServlet(name = "PcActiveStatusServlet", urlPatterns = {"/PcActiveStatusServlet"})
public class PcActiveStatusServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            ObjectInputStream in = new ObjectInputStream(request.getInputStream());
            String pcID = (String) in.readObject();
            in.close();
            System.out.println("Adding Pc ID:#################### " + pcID);
            boolean flag = false;
            for (int i = 0; i < CommonVariable.myPcIDs.size(); i++) {
                if (CommonVariable.myPcIDs.get(i).equalsIgnoreCase(pcID)) {
                    flag = true;
                    break;
                }
            }
            int resp = -1;
            if (!flag) {
                CommonVariable.myPcIDs.add(pcID);
                resp=1;
            } else {
                resp = 2;
            }

           
            System.out.println("Returning Pc ID Index:############### " + resp);
            ObjectOutputStream out = new ObjectOutputStream(response.getOutputStream());
            // receive and deserialize the object, note the cast
            out.writeObject(resp);
            out.close();
        } catch (Exception e) {
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
