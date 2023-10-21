package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import bussiness.ContactBussiness;
import entities.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GetContacts extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private class ResponseData {
        Boolean success;
        Boolean auth;
        String data;

        String toJson() {
            StringBuilder sb = new StringBuilder("{");
            sb.append("\"success\":").append(success).append(",");
            sb.append("\"auth\":").append(auth).append(",");
            sb.append("\"data\":").append(data);
            return sb.append("}").toString();
        }

        public String toString() {
            return toJson();
        }
    }

    private ContactBussiness contactBussiness;

    @Override
    public void init() throws UnavailableException {
        try {
            contactBussiness = new ContactBussiness();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new UnavailableException("Servlet Cannot be instantiated");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp);
    }

    private void process(ResponseData rd, HttpServletRequest req, HttpServletResponse resp) {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            rd.auth = false;
            return;
        }
        rd.auth = true;

        try {
            rd.data = contactBussiness.getContactsString(user);
            rd.success = true;
        } catch (ClassNotFoundException|SQLException e) {
            resp.setStatus(500);
            e.printStackTrace();
            return ;
        }
    }
    
    private void processRequest(HttpServletRequest req, HttpServletResponse resp) {
        ResponseData rd = new ResponseData();

        process(rd, req, resp);

        resp.setContentType("text/json");
        try (PrintWriter out = resp.getWriter()) {
            out.println(rd.toJson());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
