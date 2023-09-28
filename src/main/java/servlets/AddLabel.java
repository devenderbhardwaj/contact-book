package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import bussiness.LabelBussiness;
import entities.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AddLabel extends HttpServlet{

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        class Response {
            Boolean auth;
            Boolean success;
            String labelJson;

            public String toJson() {
                return "{ \"auth\":" +auth+ ", \"success\":" + success + ", \"label\":" + labelJson + "}";
            }

            @Override
            public String toString() {
                return toJson();
            }
        }
        Response myResponse = new Response();
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            myResponse.auth = false;
        } else {
            myResponse.auth = true;

            String labelText = req.getParameter("text");
    
            try {
                LabelBussiness lBussiness = new LabelBussiness();
                myResponse.labelJson = lBussiness.addLabel(labelText, user.getId());
                myResponse.success = true;
            } catch (Exception e) {
                myResponse.success = false;
                resp.setStatus(500);
                e.printStackTrace();
            }

        }

        try (PrintWriter out = resp.getWriter()) {
            out.println(myResponse.toJson());
        }
    }
    
}
