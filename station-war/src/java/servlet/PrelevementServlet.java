/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Belouh
 */
@WebServlet(name = "PrelevementServlet", urlPatterns = {"/PrelevementServlet"})
public class PrelevementServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder jsonBuffer = new StringBuilder();
        String line;
        try (BufferedReader reader = req.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonBuffer.append(line);
            }
        }

        String jsonString = jsonBuffer.toString();
        System.out.println("Contenue recue depuis mobile effectuer "+ jsonString);
        
        // Préparation d'une réponse JSON
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        // Crée une réponse JSON simple
        PrintWriter out = resp.getWriter();
        out.print("{\"message\": \"Données reçues avec succès\"}");
        out.flush();
    }
}
