package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import bussiness.ContactBussiness;
import entities.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DeleteContact extends HttpServlet {
    private class ResponseData {
        Boolean authenticate;
        Boolean autherize;
        Boolean valid;
        Boolean success;

        String toJson() {
            StringBuilder sb = new StringBuilder("{");
            sb.append("\"authenticate\":").append(authenticate).append(",");
            sb.append("\"autherize\":").append(autherize).append(",");
            sb.append("\"valid\":").append(valid).append(",");
            sb.append(("\"success\":")).append(success);
            sb.append("}");
            return sb.toString();
        }

        @Override
        public String toString() {
            return toJson();
        }
    }

    /**
     * 
     */
    private static final long serialVersionUID = -5774239806694409089L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResponseData rd = new ResponseData();
        process(rd, req, resp);

        resp.setContentType("text/json");
        try (PrintWriter out = resp.getWriter()) {
            out.println(rd.toJson());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void process(ResponseData rd, HttpServletRequest req, HttpServletResponse resp) {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            rd.authenticate = false;
            return;
        }
        rd.authenticate = true;

        Long id;
        try {
            id = Long.parseLong(req.getParameter("contact_id"));
            rd.valid = true;
        } catch (NumberFormatException e) {
            rd.valid = false;
            return ;
        }
        
        try {
            ContactBussiness contactBussiness = new ContactBussiness();
            rd.success = contactBussiness.deleteContact(user, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
