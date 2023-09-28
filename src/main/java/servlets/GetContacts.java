package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import bussiness.ContactBussiness;
import entities.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GetContacts extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        String response = "[]";
        if (user == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            try {
                ContactBussiness bussiness = new ContactBussiness();
                resp.setContentType("text/json");
                response = bussiness.getContacts(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try (PrintWriter out = resp.getWriter()) {
                out.println(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
