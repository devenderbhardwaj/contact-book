package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLIntegrityConstraintViolationException;

import bussiness.UserBussiness;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AddNewUser extends HttpServlet {
    private class ResponseData {
        boolean success = false;
        boolean name = false;
        boolean email = false;
        boolean password = false;
        Boolean alreadyExist = null;

        String toJson() {
            StringBuilder jsonBuilder = new StringBuilder();
            jsonBuilder.append("{");
            jsonBuilder.append("\"created\":").append(success).append(",");
            jsonBuilder.append("\"name\":").append(name).append(",");
            jsonBuilder.append("\"email\":").append(email).append(",");
            jsonBuilder.append("\"password\":").append(password).append(",");
            jsonBuilder.append("\"alreadyExist\":").append(alreadyExist).append(",");
            jsonBuilder.append("}");
            return jsonBuilder.toString();
        }

        private ResponseData nameValid(String name) {
            this.name = name != null && (!name.equals(""));
            return this;
        }

        private ResponseData emailValid(String email) {
            this.email = email != null && (!email.equals(""));
            return this;
        }

        private ResponseData passwordValid(String password) {
            this.password = password != null && (!password.equals(""));
            return this;
        }

        private ResponseData validate(String name, String email, String password) {
            return this.nameValid(name)
                    .emailValid(email)
                    .passwordValid(password);
        }
    };

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        ResponseData rd = new ResponseData();
        rd.validate(name, email, password);

        if (rd.name && rd.email && rd.password) {
            try {
                UserBussiness userBussiness = new UserBussiness();
                boolean added = userBussiness.addUser(name, email, password);
                rd.alreadyExist = !added;
                rd.success = added;
            } catch (SQLIntegrityConstraintViolationException e) {
                rd.alreadyExist = true;
                rd.success = false;
            } catch (Exception e) {
                rd.success = false;
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                e.printStackTrace();
            }
        }

        resp.setContentType("text/json");
        try (PrintWriter out = resp.getWriter()) {
            out.println(rd.toJson());
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }



}
