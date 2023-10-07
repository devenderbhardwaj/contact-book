package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import bussiness.LabelBussiness;
import entities.User;
import jakarta.servlet.ServletException;
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
            LabelBussiness lbBussiness = new LabelBussiness();
            rd.data = lbBussiness.getLabels(user);
            rd.success = true;
        } catch (Exception e) {
            rd.success = false;
            e.printStackTrace();
        }

    }
}
