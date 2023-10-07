package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import Exceptions.UserDoesNotExist;
import Exceptions.WrongPassword;
import bussiness.UserBussiness;
import entities.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Login extends HttpServlet {
    private class Response {
        Boolean success = false;
        Boolean accountDoesNotExist = null;
        Boolean wrongPassword = null;
        String redirectUrl = "";

        String toJson() {
            StringBuilder sb = new StringBuilder("{");
            sb.append("\"success\":").append(success).append(", ");
            sb.append("\"accountDoesNotExist\":").append(accountDoesNotExist).append(", ");
            sb.append("\"wrongPassword\":").append(wrongPassword).append(", ");
            sb.append("\"redirectUrl\":\"").append(redirectUrl).append("\"");
            sb.append("}");
            return sb.toString();
        }
    }

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        Response toSend = new Response();
        try {
            UserBussiness userBussiness = new UserBussiness();
            User user = userBussiness.authenticateUser(email, password);
            req.getSession().setAttribute("user", user);
            if (user != null) {
                toSend.success = true;
                toSend.redirectUrl = "index.jsp";
            }
            toSend.wrongPassword = false;
            toSend.accountDoesNotExist = false;
        } catch(UserDoesNotExist e) {
            toSend.accountDoesNotExist = true;
        } catch(WrongPassword e) {
            toSend.accountDoesNotExist = false;
            toSend.wrongPassword = true;
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }

        resp.setContentType("text/json");
        try (PrintWriter writer = resp.getWriter()) {
            writer.println(toSend.toJson());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}