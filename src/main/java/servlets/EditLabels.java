package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import bussiness.ContactBussiness;
import entities.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class EditLabels extends HttpServlet {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private class ResponseData {
        Boolean authenticate;
        Boolean autherize;
        Boolean valid;
        Boolean success;
        String data;

        String toJson() {
            StringBuilder sb = new StringBuilder("{");
            sb.append("\"authenticate\":").append(authenticate).append(", ");
            sb.append("\"autherize\":").append(autherize).append(", ");
            sb.append("\"valid\":").append(valid).append(", ");
            sb.append("\"success\":").append(success).append(", ");
            sb.append("\"data\":").append(data);
            sb.append("}");
            return sb.toString();
        }

        @Override
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
        }
    }

    private void process(ResponseData rd, HttpServletRequest req, HttpServletResponse resp) {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            rd.authenticate = false;
            return;
        }
        rd.authenticate = true;

        Long contact_id;
        ArrayList<Long> label_ids = new ArrayList<>();
        try {
            contact_id = Long.parseLong(req.getParameter("contact_id"));
        } catch (NumberFormatException | NullPointerException e) {
            e.printStackTrace();
            rd.valid = false;
            return;
        }

        try {
            String[] ids = req.getParameterValues("id");
            for (String idString : ids) {
                label_ids.add(Long.parseLong(idString));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            rd.valid = false;
            return;
        } catch (NullPointerException e) {

        }
        rd.valid = true;

        try {
            ContactBussiness cb = new ContactBussiness();
            rd.data = cb.editLabels(user, contact_id, label_ids);
        } catch (Exception e) {
            e.printStackTrace();
            return ;
        }
        rd.success = true;

    }

}
