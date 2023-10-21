package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import entities.Label;
import entities.User;

public class LabelDao {
    private final Connection con;

    public LabelDao() throws ClassNotFoundException, SQLException  {
        con = ConnectionProvider.getConnection();
    }

    public Label addLabel(String text, Long user_id) throws SQLException {
        String query = "INSERT INTO labels (label, user_id) VALUES (?, ?)";
        Label label = null;
        try (PreparedStatement pStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pStatement.setString(1, text);
            pStatement.setLong(2, user_id);
            if (pStatement.executeUpdate() > 0) {
                ResultSet rs = pStatement.getGeneratedKeys();
                if (rs.next()) {
                    label = new Label();
                    label.setId(rs.getLong(1));
                    label.setText(text);
                }
            }
        }
        return label;
    }

    public Label getLabel(Long label_id) throws SQLException {
        String query = "SELECT * FROM labels where label_id = ?";
        Label toReturn = null;
        try(PreparedStatement preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, label_id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                toReturn = new Label(rs.getLong("label_id"), rs.getString("label"));
                toReturn.setUser_id(rs.getLong("user_id"));
            }
        }
        return toReturn;
    }

    public ArrayList<Label> getLabels(User user) throws SQLException {
        ArrayList<Label> list = new ArrayList<>();

        String query = "SELECT * FROM labels WHERE user_id = ?";

        try ( PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setLong(1, user.getId());
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Label label = new Label();
                label.setId(rs.getLong("label_id"));
                label.setText(rs.getString("label"));
                label.setUser_id(rs.getLong("user_id"));
                list.add(label);
            }
        }
        return list;
    }

    public boolean deleteLabel(long label_id) throws SQLException {
        String query = "DELETE FROM labels WHERE label_id = ?";
        int rowsEffect = 0;
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setLong(1, label_id);
            rowsEffect = ps.executeUpdate();
        }
        return rowsEffect > 0;
    }
}
