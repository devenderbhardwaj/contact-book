package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import entities.Contact;
import entities.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SaveContact extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String phone = req.getParameter("phone");
        String email = req.getParameter("email");
        String address = req.getParameter("address");

        User user = (User) req.getSession().getAttribute("user");
        String result = "Something went wrong";
        if (user != null) {
            try {
                bussiness.ContactBussiness saveContact = new bussiness.ContactBussiness();
                Contact contact =new Contact(name);
                contact.setAddress(address);
                contact.setEmail(email);
                contact.setPhone(phone);
                contact.setUser_id(user.getId());
                result = saveContact.save( contact);
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                e.printStackTrace();
            }
        }
        try (PrintWriter out = resp.getWriter()) {
            out.println(result);
        } catch (Exception e) {
            resp.setStatus(500);
            e.printStackTrace();
        }
    }

}
