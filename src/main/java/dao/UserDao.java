package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import entities.User;

public class UserDao {
    private Connection con;

    public UserDao() throws ClassNotFoundException, SQLException {
        con = ConnectionProvider.getConnection();
    }

    public boolean addUser(User user) throws SQLException{
        String query = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";

        int rowEffected = 0;
        try (PreparedStatement preparedStatement = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            rowEffected = preparedStatement.executeUpdate();
        }
        return rowEffected > 0;
    }

    public User getUser(String email) throws SQLException {
        String query = "SELECT * FROM users WHERE email = ?";

        User user = null;

        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
        	preparedStatement.setString(1, email);
            ResultSet rSet = preparedStatement.executeQuery();

            if (rSet.next()) {
                user = new User();
                user.setId(rSet.getLong("user_id"));
                user.setName(rSet.getString("name"));
                user.setPassword(rSet.getString("password"));
                user.setEmail(rSet.getString("email"));
            }
        }
        return user;
    }
}
