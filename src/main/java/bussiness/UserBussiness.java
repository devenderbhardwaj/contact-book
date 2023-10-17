package bussiness;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Arrays;

import Exceptions.DoesNotExistException;
import Exceptions.WrongPassword;
import dao.UserDao;
import entities.User;

public class UserBussiness {
    private UserDao userDao;

    public UserBussiness() throws ClassNotFoundException, SQLException {
        userDao = new UserDao();
    }

    public boolean addUser(String name, String email, String password) throws SQLException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA3-256");
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        byte[] passbytes = password.getBytes();

        byte[] saltedPassBytes = new byte[salt.length + passbytes.length];
        for (int i = 0; i < passbytes.length; i++) {
            saltedPassBytes[i] = passbytes[i];
        }
        for (int i = passbytes.length, j = 0; i < saltedPassBytes.length && j < salt.length; ++i, j++) {
            saltedPassBytes[i] = salt[j];
        }

        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setHashPassword(digest.digest(saltedPassBytes));
        user.setSalt(salt);
        return userDao.addUser(user);
    }

    public boolean addUser(User user) throws SQLException {
        return userDao.addUser(user);
    }

    public User authenticateUser(String email, String password)
            throws WrongPassword, DoesNotExistException, SQLException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA3-256");
        User user = userDao.getUser(email);
        if (user == null) {
            throw new DoesNotExistException();
        }
        byte[] passbytes = password.getBytes();
        byte[] salt = user.getSalt();

        byte[] saltedPassBytes = new byte[salt.length + passbytes.length];

        for (int i = 0; i < passbytes.length; i++) {
            saltedPassBytes[i] = passbytes[i];
        }
        for (int i = passbytes.length, j = 0; i < saltedPassBytes.length && j < salt.length; ++i, j++) {
            saltedPassBytes[i] = salt[j];
        }
        if (!Arrays.equals(user.getHashPassword(), digest.digest(saltedPassBytes))) {
            throw new WrongPassword();
        }
        return user;
    }
}
