package servlets;

import java.io.IOException;
import java.sql.SQLException;

import Exceptions.DoesNotExistException;
import Exceptions.UnAuthorizedActionException;
import bussiness.ContactBussiness;
import entities.ProfilePicture;
import entities.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GetProfilePicture extends HttpServlet{

    /**
	 * 
	 */
	private static final long serialVersionUID = 6298252469210205784L;

	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       process(req, resp);
    }

    private void process(HttpServletRequest req, HttpServletResponse  resp) {
        ContactBussiness cb ;
        try {
            cb = new ContactBussiness();
        } catch (ClassNotFoundException | SQLException e) {
            resp.setStatus(500);
            e.printStackTrace();
            return ;
        }
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.setStatus(401);
            return ;
        }
        long contact_id;
        try {
            contact_id = Long.parseLong(req.getParameter("contact_id"));
        } catch (NullPointerException|NumberFormatException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return ;
        }
        try {
            ProfilePicture picture = cb.getProfilePicture(user, contact_id);
            resp.setContentType("image/"+picture.getType());
            var out = resp.getOutputStream();
            out.write(picture.getInputStream().readAllBytes());
        } catch (SQLException|IOException e) {
            resp.setStatus(500);
            e.printStackTrace();
        } catch (DoesNotExistException|UnAuthorizedActionException e) {
            resp.setStatus(401);
            e.printStackTrace();
        } catch (NullPointerException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

    }
    
}
