package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import bussiness.LabelBussiness;
import entities.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GetLabels extends HttpServlet{
    private class ResponseData {
        Boolean success;
        Boolean auth;
        String data;

        String toJson() {
            StringBuilder sb = new StringBuilder("{");
            sb.append("\"success\":").append(success).append(",");
            sb.append("\"auth\":").append(auth).append(",");
            sb.append("\"data\":").append(data);
            
            return sb.append("}").toString();
        }

        public String toString() {
            return toJson();
        }
    }

    private LabelBussiness labelBussiness;

    @Override
    public void init() throws UnavailableException{
        try {
            labelBussiness = new LabelBussiness();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new UnavailableException("Servlet Cannot be instantiated");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResponseData rd = new ResponseData();
        process(rd, req, resp);
        try (PrintWriter out = resp.getWriter()) {
            out.println(rd);
        }
    }
    
    private void process(ResponseData rd , HttpServletRequest req, HttpServletResponse resp) {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            rd.auth = false;
            resp.setStatus(401);
            return ;
        }
        rd.auth = true;
        try {
            rd.data = labelBussiness.getLabelsString(user);
            rd.success = true;
        } catch (SQLException e) {
            rd.success = false;
            resp.setStatus(500);
            e.printStackTrace();
        }

    }
}
