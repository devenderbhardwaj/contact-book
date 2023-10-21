package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import Exceptions.DoesNotExistException;
import Exceptions.WrongPassword;
import bussiness.UserBussiness;
import entities.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Login extends HttpServlet {
    private class ResponseData {
        Boolean success;
        Boolean valid;
        Boolean accountDoesNotExist;
        Boolean wrongPassword;
        String redirectUrl;

        String toJson() {
            StringBuilder sb = new StringBuilder("{");
            sb.append("\"success\":").append(success).append(", ");
            sb.append("\"valid\":").append(valid).append(", ");
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
    
    private UserBussiness userBussiness;

    public void init() throws UnavailableException {
        try {
            userBussiness = new UserBussiness();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new UnavailableException("Servlet cannot  be initialized");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ResponseData rd = new ResponseData();
        process(rd, req, resp);

        resp.setContentType("text/json");
        try (PrintWriter writer = resp.getWriter()) {
            writer.println(rd.toJson());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void process(ResponseData rd, HttpServletRequest req, HttpServletResponse resp) {
        

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (email == null || password == null) {
            rd.valid = false;
            return;
        }

        try {
            User user = userBussiness.authenticateUser(email, password);
            req.getSession().setAttribute("user", user);
            rd.success = true;
            rd.redirectUrl = "index.jsp";
        } catch (WrongPassword e) {
            rd.wrongPassword = true;
            return;
        } catch (DoesNotExistException e) {
            rd.accountDoesNotExist = true;
            return;
        } catch (SQLException|NoSuchAlgorithmException e) {
            resp.setStatus(500);
            rd.success = false;
            e.printStackTrace();
        }

    }
}