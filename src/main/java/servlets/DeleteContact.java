package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import Exceptions.DoesNotExistException;
import Exceptions.UnAuthorizedActionException;
import bussiness.ContactBussiness;
import entities.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.UnavailableException;
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
            resp.setStatus(401);
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
            rd.success = contactBussiness.deleteContact(user, id);
        } catch (UnAuthorizedActionException|DoesNotExistException e) {
            rd.autherize = false;
            rd.success = false;
            resp.setStatus(401);
            e.printStackTrace();
            return ;
        } catch (SQLException e) {
            resp.setStatus(500);
            e.printStackTrace();
        }
    }

}
