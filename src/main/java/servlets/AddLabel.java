package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import bussiness.LabelBussiness;
import entities.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AddLabel extends HttpServlet {

    class ResponseData {
        Boolean auth;
        Boolean success;
        Boolean valid;
        String data;

        public String toJson() {
            return "{ \"auth\":" + auth + ", \"valid\":" + valid + ", \"success\":" + success + ", \"data\":" + data
                    + "}";
        }

        @Override
        public String toString() {
            return toJson();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResponseData rd = new ResponseData();
        process(rd, req, resp);

        resp.setContentType("text/json");
        try (PrintWriter out = resp.getWriter()) {
            out.println(rd.toJson());
        }
    }

    private void process(ResponseData rd, HttpServletRequest req, HttpServletResponse resp) {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            rd.auth = false;
            return;
        }
        rd.auth = true;

        String labelText = req.getParameter("text");

        if (labelText == null || labelText.equals("")) {
            rd.valid = false;
            return;
        }
        rd.valid = true;
        try {
            LabelBussiness lBussiness = new LabelBussiness();
            rd.data = lBussiness.addLabel(labelText, user.getId());
        } catch (Exception e) {
            rd.success = false;
            resp.setStatus(500);
            e.printStackTrace();
            return ;
        }
        rd.success = true;
    }
}
