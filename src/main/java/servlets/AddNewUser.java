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
    private class Response {
        boolean created = false;
        boolean name = false;
        boolean email = false;
        boolean password = false;
        Boolean alreadyExist = null;
        String info = "";

        String toJson() {
            StringBuilder jsonBuilder = new StringBuilder();
            jsonBuilder.append("{");
            jsonBuilder.append("\"created\":").append(created).append(",");
            jsonBuilder.append("\"name\":").append(name).append(",");
            jsonBuilder.append("\"email\":").append(email).append(",");
            jsonBuilder.append("\"password\":").append(password).append(",");
            jsonBuilder.append("\"alreadyExist\":").append(alreadyExist).append(",");
            jsonBuilder.append("\"info\":\"").append(info).append("\"");
            jsonBuilder.append("}");
            return jsonBuilder.toString();
        }

        private Response nameValid(String name) {
            this.name = name != null && (!name.equals(""));
            return this;
        }

        private Response emailValid(String email) {
            this.email = email != null && (!email.equals(""));
            return this;
        }

        private Response passwordValid(String password) {
            this.password = password != null && (!password.equals(""));
            return this;
        }

        private Response validate(String name, String email, String password) {
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

        Response toSend = new Response();
        toSend.validate(name, email, password);

        if (toSend.name && toSend.email && toSend.password) {
            try {
                UserBussiness userBussiness = new UserBussiness();
                boolean added = userBussiness.addUser(name, email, password);
                toSend.alreadyExist = !added;
                toSend.created = added;
            } catch (SQLIntegrityConstraintViolationException e) {
                toSend.alreadyExist = true;
                toSend.created = false;
            } catch (Exception e) {
                toSend.created = false;
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                e.printStackTrace();
            }
        }

        resp.setContentType("text/json");
        try (PrintWriter out = resp.getWriter()) {
            out.println(toSend.toJson());
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}
