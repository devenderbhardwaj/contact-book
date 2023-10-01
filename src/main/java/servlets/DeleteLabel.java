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
        Boolean initialAuth ;
        Boolean validRequest;
        Boolean success;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("{");
            sb.append("\"initialAuth\":").append(initialAuth).append(",");
            sb.append("\"validRequest\":").append(validRequest).append(",");
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
            rd.initialAuth = false;
            resp.setStatus(401);
            return ;
        }
        rd.initialAuth = true;
        try {
            long label_id = Long.parseLong(req.getParameter("label_id"));

            LabelBussiness labelBussiness = new LabelBussiness();
            if (labelBussiness.LabelUserRelation(user, label_id)) {
                rd.validRequest = true;
                if (labelBussiness.deleteLabel(label_id)) {
                    rd.success = true;
                } else {
                    rd.success = false;
                }; 
            } else {
                rd.validRequest = false;
                return ;
            }
        } catch (NumberFormatException e) {
            rd.validRequest = false;
            e.printStackTrace();
            return ;
        } catch (Exception e) {
            resp.setStatus(500);
            e.printStackTrace();
            return ;
        }
    }
    
}
