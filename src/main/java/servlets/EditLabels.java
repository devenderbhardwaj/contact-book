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

	class ResponseData {
        Boolean auth;
        Boolean dataValidation;
        Boolean success;
        String contactJson;

        String toJson() {
            StringBuilder sb = new StringBuilder("{");
            sb.append("\"auth\":").append(auth).append(", ");
            sb.append("\"valid\":").append(dataValidation).append(", ");
            sb.append("\"success\":").append(success).append(", ");
            sb.append("\"contact\":").append(contactJson);
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
        ResponseData responseData = new ResponseData();
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            responseData.auth = false;
        } else {
            responseData.auth = true;
            ArrayList<Long> label_ids = new ArrayList<>();
            try {
                Long contact_id = Long.parseLong(req.getParameter("contact_id"));
                String[] ids = req.getParameterValues("id");
                if (ids != null) {
                    for (String idString : ids) {
                        label_ids.add(Long.parseLong(idString));
                    }
                }
                responseData.dataValidation = true;
                ContactBussiness cb = new ContactBussiness();
                responseData.contactJson  = cb.editLabels(user, contact_id, label_ids);
                responseData.success = true;
            } catch (NumberFormatException | NullPointerException e) {
                responseData.dataValidation = false;
                e.printStackTrace();
            } catch (Exception e) {
                responseData.success = false;
                e.printStackTrace();
            }
        }

        try (PrintWriter out = resp.getWriter()) {
            out.println(responseData.toJson());
        }
    }

}
