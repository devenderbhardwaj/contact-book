package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import bussiness.LabelBussiness;
import entities.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GetLabels extends HttpServlet{
    

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.setStatus(401);
            return ;
        }
        String resposne = "";
        try {
            LabelBussiness lbBussiness = new LabelBussiness();
            resposne = lbBussiness.getLabels(user);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (PrintWriter out = resp.getWriter()) {
            out.println(resposne);
        }
    }
    
}
