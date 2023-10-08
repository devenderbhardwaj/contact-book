package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import bussiness.LabelBussiness;
import entities.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DeleteLabel extends HttpServlet{
    private class ResponseData {
        Boolean authenticate ;
        Boolean autherize;
        Boolean valid;
        Boolean success;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("{");
            sb.append("\"authenticate\":").append(authenticate).append(",");
            sb.append("\"autherize\":").append(autherize).append(",");
            sb.append("\"validRequest\":").append(valid).append(",");
            sb.append("\"success\":").append(success);
            sb.append("}");
            return sb.toString();
        }

        String toJson() {
            return toString();
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResponseData rd = new ResponseData();
        process(rd, req, resp);
        try (PrintWriter out = resp.getWriter()) {
            out.print(rd.toJson());
        } catch (Exception e) {
            resp.setStatus(500);
            e.printStackTrace();
        }
        
    }

    private void process(ResponseData rd, HttpServletRequest req, HttpServletResponse resp) {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            rd.authenticate = false;
            resp.setStatus(401);
            return ;
        }
        rd.authenticate = true;

        long label_id;
        try {
            label_id = Long.parseLong(req.getParameter("label_id"));
        } catch (NumberFormatException|NullPointerException e) {
            rd.valid = false;
            return ;
        }

        try {
            LabelBussiness labelBussiness = new LabelBussiness();
            if (labelBussiness.LabelUserRelation(user, label_id)) {
                rd.valid = true;
                if (labelBussiness.deleteLabel(label_id)) {
                    rd.success = true;
                } else {
                    rd.success = false;
                }; 
            } else {
                rd.valid = false;
                return ;
            }
        } catch (Exception e) {
            resp.setStatus(500);
            e.printStackTrace();
            return ;
        }
    }
    
}
