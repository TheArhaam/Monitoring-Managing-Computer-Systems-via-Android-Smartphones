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
@WebServlet(name = "ReadFeed", urlPatterns = {"/ReadFeed"})
public class ReadFeed extends HttpServlet {

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
            MyImage cmdForServer;

            //Accept Client Request
            ObjectInputStream in = new ObjectInputStream(request.getInputStream());
            String req = (String) in.readObject();
            in.close();

            MyImage allActivePcImages = new MyImage();
            allActivePcImages.allImages = new ArrayList<>();
            allActivePcImages.allPcIds = new ArrayList<>();

            //Check Active Servers
            String fileName = "";
            for (int i = 0; i < CommonVariable.myPcIDs.size(); i++) {
                ImageData id = new ImageData();
                allActivePcImages.allPcIds.add("" + CommonVariable.myPcIDs.get(i));
                id.mybyteArray = CommonVariable.myPcImages.get(i);
                allActivePcImages.allImages.add(id);
                

//                fileName = "D:\\ProjectData\\DesktopSnap\\" + CommonVariable.myPcIDs.get(i) + "." + format;
//                File f1 = new File(fileName);
//                if (f1.exists()) {
//                    BufferedImage img = ImageIO.read(new File(fileName));
//                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                    ImageIO.write(img, "jpg", baos);
//                    ImageData id = new ImageData();
//                    id.mybyteArray = baos.toByteArray();
//                    baos.flush();
//                    baos.close();
//                    
//                }
            }

//            String cmdFile = "D:\\ProjectData\\DesktopSnap\\" + cmdForServer.pcID + ".txt";
//            String op = "";
//            if ((cmdForServer.x == -1) && (cmdForServer.y == -1) && (cmdForServer.clickCmd == -1)) {
//                op += "-1";
//            } else if (cmdForServer.clickCmd != -1) {
//                //op += "coor##" + cmdForServer.x + "##" + cmdForServer.y;
//                op += "cmd##" + cmdForServer.clickCmd;
//            } else {
//                op += "coor##" + cmdForServer.x + "##" + cmdForServer.y;
//            }
//            PrintWriter out1 = new PrintWriter(cmdFile);
//            out1.println(op);
//            out1.close();
//
//            BufferedImage img = ImageIO.read(new File(fileName));
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            ImageIO.write(img, "jpg", baos);
//            MyImage myData = new MyImage();
            // myData.mybyteArray = baos.toByteArray();
            //Send Data back to client
            ObjectOutputStream out = new ObjectOutputStream(response.getOutputStream());
            // receive and deserialize the object, note the cast
            out.writeObject(allActivePcImages);
            out.close();

        } catch (Exception e) {
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
