package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import bussiness.ContactBussiness;
import entities.Contact;
import entities.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class EditContact extends HttpServlet{

	
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        String result = "{\"result\":false}";
        if (user == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            Long id = Long.parseLong(req.getParameter("contact-id"));
            String name = req.getParameter("name");
            String phone = req.getParameter("phone");
            String email = req.getParameter("email");
            String address = req.getParameter("address");
    
            try {
                ContactBussiness contactBussiness = new ContactBussiness();
                Contact contact =new Contact(id);
                contact.setAddress(address);
                contact.setEmail(email);
                contact.setPhone(phone);
                contact.setName(name);
                result = contactBussiness.editContact(user, contact);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try (PrintWriter out = resp.getWriter()) {
            out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
