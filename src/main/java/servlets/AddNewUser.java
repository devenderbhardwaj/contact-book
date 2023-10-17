package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import bussiness.UserBussiness;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AddNewUser extends HttpServlet {
    private class Validate {
        private static boolean nameValid(String name) {
            return name != null && (!name.equals(""));
        }

        private static boolean emailValid(String email) {
            return email != null && (!email.equals(""));
        }

        private static boolean passwordValid(String password) {
            return password != null && (!password.equals(""));
        }

        static boolean validate(ResponseData rd, String name, String email, String password) {
            rd.email = emailValid(email);
            rd.name = nameValid(name);
            rd.password = passwordValid(password);
            return rd.email && rd.name && rd.password;
        }
    }

    private class ResponseData {
        boolean success = false;
        boolean name = false;
        boolean email = false;
        boolean password = false;
        Boolean alreadyExist = null;

        String toJson() {
            StringBuilder jsonBuilder = new StringBuilder();
            jsonBuilder.append("{");
            jsonBuilder.append("\"success\":").append(success).append(",");
            jsonBuilder.append("\"name\":").append(name).append(",");
            jsonBuilder.append("\"email\":").append(email).append(",");
            jsonBuilder.append("\"password\":").append(password).append(",");
            jsonBuilder.append("\"alreadyExist\":").append(alreadyExist);
            jsonBuilder.append("}");
            return jsonBuilder.toString();
        }
    };

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

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
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (!Validate.validate(rd, name, email, password)) {
            return;
        }
        ;
        try {
            UserBussiness userBussiness = new UserBussiness();
            boolean added = userBussiness.addUser(name, email, password);
            rd.alreadyExist = !added;
            rd.success = added;
        } catch (SQLIntegrityConstraintViolationException e) {
            rd.alreadyExist = true;
            rd.success = false;
        } catch (ClassNotFoundException|SQLException|NoSuchAlgorithmException e) {
            rd.success = false;
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }

}
