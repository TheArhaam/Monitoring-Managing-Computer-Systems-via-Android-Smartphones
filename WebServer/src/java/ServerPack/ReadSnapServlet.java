/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerPack;

import LibPack.MyImage;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;
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
@WebServlet(name = "ReadSnapServlet", urlPatterns = {"/ReadSnapServlet"})
public class ReadSnapServlet extends HttpServlet {

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
        MyImage commandFromAndroid = new MyImage();
        try {

            if (CommonVariable.firstTime) {
                CommonVariable.firstTime = false;
                byte[] temparray = null;
                CommonVariable.myPcImages.add(temparray);
                CommonVariable.myPcImages.add(temparray);
                CommonVariable.myPcImages.add(temparray);
                CommonVariable.myPcImages.add(temparray);
                CommonVariable.myPcImages.add(temparray);

                CommonVariable.pcCmd.add("-1");
                CommonVariable.pcCmd.add("-1");
                CommonVariable.pcCmd.add("-1");
                CommonVariable.pcCmd.add("-1");
                CommonVariable.pcCmd.add("-1");
            }

            ObjectInputStream in = new ObjectInputStream(request.getInputStream());

            MyImage mySnap = (MyImage) in.readObject();
            in.close();

            commandFromAndroid.mobileWidth = CommonVariable.mobileWidth;
            commandFromAndroid.mobileHeight = CommonVariable.mobileHeight;
            String format = "jpg";
            String fileName = "D:\\ProjectData\\DesktopSnap\\" + mySnap.pcID + "." + format;
            //
            int index = -1;
            for (int i = 0; i < CommonVariable.myPcIDs.size(); i++) {
                if (CommonVariable.myPcIDs.get(i).equalsIgnoreCase(mySnap.pcID)) {
                    index = i;
                    break;
                }
            }
            String cmd = "";

            if (index != -1) {
                // System.out.println("Image Length: "+mySnap.imageArray.length);
                CommonVariable.myPcImages.set(index, mySnap.imageArray);
                cmd = CommonVariable.pcCmd.get(index);
                CommonVariable.pcCmd.set(index, "-1");
            }

            if (cmd.equals("-1")) {
                commandFromAndroid.clickCmd = -1;
            } else {
                String[] tempCmd = cmd.split("##");
                //Check Whether Command is move cursor
                if (tempCmd[0].equalsIgnoreCase("Move")) {
                    commandFromAndroid.clickCmd = 1;
                    commandFromAndroid.x = Float.parseFloat(tempCmd[1]);
                    commandFromAndroid.y = Float.parseFloat(tempCmd[2]);

                } else if (tempCmd[0].equalsIgnoreCase("Click")) {
                    System.out.println("Got Command: " + tempCmd[0]);
                    if (tempCmd[1].equals("2")) {
                        commandFromAndroid.clickCmd = 2;
                    } else if (tempCmd[1].equals("3")) {
                        commandFromAndroid.clickCmd = 3;
                    } else if (tempCmd[1].equals("4")) {
                        commandFromAndroid.clickCmd = 4;
                    }
                }
            }

//            // define a new ObjectInputStream on the input stream
            ObjectOutputStream out = new ObjectOutputStream(response.getOutputStream());
            // receive and deserialize the object, note the cast
            out.writeObject(commandFromAndroid);
            out.close();

        } catch (Exception e) {
            ObjectOutputStream out = new ObjectOutputStream(response.getOutputStream());
            // receive and deserialize the object, note the cast
            out.writeObject(commandFromAndroid);
            out.close();
            // e.printStackTrace();
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
