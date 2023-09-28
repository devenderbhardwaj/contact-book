package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import bussiness.ContactBussiness;
import entities.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DeleteContact extends HttpServlet{

    /**
	 * 
	 */
	private static final long serialVersionUID = -5774239806694409089L;

	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/json");
        boolean result = false;

        User user = (User) req.getSession().getAttribute("user");
        Long id = Long.parseLong(req.getParameter("contact_id"));
        if (user != null) {
            try {
                ContactBussiness contactBussiness = new ContactBussiness();
                result = contactBussiness.deleteContact(user, id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
		
        try (PrintWriter out = resp.getWriter()) {
            out.println("{ \"deleted\": " + result + "}");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
