package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.StringTokenizer;

import Exceptions.DoesNotExistException;
import Exceptions.UnAuthorizedActionException;
import bussiness.ContactBussiness;
import entities.ProfilePicture;
import entities.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

public class SaveProfilePicture extends HttpServlet {

    private class ResponseData {
        Boolean success = false;
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
    private static final long serialVersionUID = -5468427283700902242L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResponseData rd = new ResponseData();
        process(rd, req, resp);
        PrintWriter out = resp.getWriter();
        out.println(rd.toJson());
    }

    private void process(ResponseData rd, HttpServletRequest req, HttpServletResponse resp) {
        ContactBussiness cb;
        try {
            cb = new ContactBussiness();
        } catch (ClassNotFoundException | SQLException e) {
            resp.setStatus(500);
            e.printStackTrace();
            return;
        }
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            rd.auth = false;
            resp.setStatus(401);
            return;
        }
        rd.auth = true;
        Long contact_id;
        try {
            contact_id = Long.parseLong(req.getParameter("contact_id"));
        } catch (NullPointerException | NumberFormatException e) {
            rd.valid = false;
            return;
        }

        Part part;
        try {
            part = req.getPart("image");
        } catch (IOException | ServletException e) {
            e.printStackTrace();
            return;
        }

        if (part == null) {
            rd.valid = false;
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String contentType = part.getContentType();
        String type = null;
        StringTokenizer st = new StringTokenizer(contentType, "/");
        while (st.hasMoreTokens()) {
            type = st.nextToken();
        }
        if (type == null) {
            rd.valid = false;
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        try {
            rd.success = cb.saveProfilePicture(
                    user,
                    new ProfilePicture(contact_id, type, part.getInputStream()));
        } catch (SQLException | IOException e) {
            resp.setStatus(500);
            e.printStackTrace();
        } catch (DoesNotExistException | UnAuthorizedActionException e) {
            rd.auth = false;
            resp.setStatus(401);
        }
    }
}
