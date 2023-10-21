package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import bussiness.ContactBussiness;
import entities.Contact;
import entities.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SaveContact extends HttpServlet {
    private class ResponseData {
        Boolean success;
        Boolean auth;
        String data;
        Boolean valid;

        String toJson() {
            StringBuilder sb = new StringBuilder("{");
            sb.append("\"success\":").append(success).append(",");
            sb.append("\"auth\":").append(auth).append(",");
            sb.append("\"valid\":").append(valid).append(",");
            sb.append("\"data\":").append(data);
            return sb.append("}").toString();
        }

        public String toString() {
            return toJson();
        }
    }

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

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
        try (PrintWriter out = resp.getWriter()) {
            out.println(rd.toJson());
        } catch (Exception e) {
            resp.setStatus(500);
            e.printStackTrace();
        }
    }

    private void process(ResponseData rd, HttpServletRequest req, HttpServletResponse resp) {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            rd.auth = false;
            return;
        }
        rd.auth = true;
        String name = req.getParameter("name");
        String phone = req.getParameter("phone");
        String email = req.getParameter("email");
        String address = req.getParameter("address");
        if (name == null || name.equals("") || phone == null || email == null || address == null) {
            rd.valid = false;
            return ;
        }
        rd.valid = true;

        Contact contact = new Contact(name);
        contact.setAddress(address);
        contact.setEmail(email);
        contact.setPhone(phone);
        contact.setUser_id(user.getId());

        try {
            rd.data = contactBussiness.save(contact).toJson();
            rd.success = true;
        } catch (SQLException e) {
            rd.success = false;
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        } catch (NullPointerException e) {
            rd.success = false;
            e.printStackTrace();
        }
    }
}
