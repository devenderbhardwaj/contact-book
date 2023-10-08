package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import bussiness.ContactBussiness;
import entities.Contact;
import entities.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class EditContact extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private class ResponseData {
        Boolean success;
        Boolean authenticate;
        Boolean autherize;
        String data;
        Boolean valid;

        String toJson() {
            StringBuilder sb = new StringBuilder("{");
            sb.append("\"success\":").append(success).append(",");
            sb.append("\"authenticate\":").append(authenticate).append(",");
            sb.append("\"autherize\":").append(autherize).append(",");
            sb.append("\"valid\":").append(valid).append(",");
            sb.append("\"data\":").append(data);
            return sb.append("}").toString();
        }

        public String toString() {
            return toJson();
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
            rd.authenticate = false;
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        rd.authenticate = true;

        Long id;
        try {
            id = Long.parseLong(req.getParameter("contact-id"));
        } catch (NumberFormatException e) {
            rd.valid = false;
            return;
        }
        String name = req.getParameter("name");
        String phone = req.getParameter("phone");
        String email = req.getParameter("email");
        String address = req.getParameter("address");
        if (name == null || name.equals("")) {
            rd.valid = false;
            return;
        }
        rd.valid = true;
        Contact contact = new Contact(id);
        contact.setAddress(address);
        contact.setEmail(email);
        contact.setPhone(phone);
        contact.setName(name);

        try {
            ContactBussiness cb = new ContactBussiness();
            rd.data = cb.editContact(user, contact);
        } catch(Exception e) {
            rd.success = false;
            return ;
        }
        rd.success = true;
    }
}
