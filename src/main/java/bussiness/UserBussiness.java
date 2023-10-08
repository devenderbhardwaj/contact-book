package bussiness;

import java.sql.SQLException;

import Exceptions.DoesNotExistException;
import Exceptions.WrongPassword;
import dao.UserDao;
import entities.User;

public class UserBussiness {
    private UserDao userDao;

    public UserBussiness() throws ClassNotFoundException, SQLException {
        userDao = new UserDao();
    }
    public boolean addUser(String name, String email, String password) throws SQLException {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(password);

        return userDao.addUser(user);
    }

    public boolean addUser(User user) throws SQLException {
        return userDao.addUser(user);
    }

    public User authenticateUser(String email, String password) throws WrongPassword, DoesNotExistException, SQLException{
        User user = userDao.getUser(email);
        if (user == null) {
            throw new DoesNotExistException();
        }
        if (!user.getPassword().equals(password)) {
            throw new WrongPassword();
        }
        return user;
    }
}
