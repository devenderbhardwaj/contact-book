package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import entities.Contact;
import entities.Label;
import entities.User;

public class ContactDao {
    private Connection con ;

    public ContactDao() throws Exception {
        con = ConnectionProvider.getConnection();
    }

    public Contact getContact(long id) {
        Contact contact = new Contact(id);
        String query = "SELECT * FROM contacts WHERE contact_id = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                contact.setName(resultSet.getString("name"));
                contact.setAddress(resultSet.getString("address"));
                contact.setEmail(resultSet.getString("email"));
                contact.setPhone(resultSet.getString("phone"));
                contact.setUser_id(resultSet.getLong("user_id"));
            } else {
                contact = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contact;

    }
    public  Contact saveContact(Contact contact) {
        String query = "INSERT INTO contacts (name, phone, email, address, user_id) VALUES (?, ?, ?, ?, ?);";
        try (PreparedStatement statement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, contact.getName());
            statement.setString(2, contact.getPhone());
            statement.setString(3, contact.getEmail());
            statement.setString(4, contact.getAddress());
            statement.setLong(5, contact.getUser_id());
            int affectedRows = statement.executeUpdate();
            long id = 0;
            if (affectedRows > 0) {
                ResultSet resultSet = statement.getGeneratedKeys();
                while (resultSet.next()) {
                    id = resultSet.getLong(1);
                }
            }
            contact.setId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contact;
    }
    public ArrayList<Contact> getContacts(User user) {
        String query = "SELECT * FROM contacts WHERE user_id = ?";
        ArrayList<Contact> list = new ArrayList<>();
        try (PreparedStatement statment = con.prepareStatement(query)) {
            statment.setLong(1, user.getId());
            ResultSet result = statment.executeQuery();
            while (result.next()) {
                Long id = result.getLong("contact_id");
                Long user_id = result.getLong("user_id");
                String name  = result.getString("name");
                String phone = result.getString("phone");
                String email = result.getString("email");
                String address = result.getString("address");
                
                Contact contact = new Contact(name);
                contact.setId(id);
                contact.setUser_id(user_id);
                contact.setAddress(address);
                contact.setEmail(email);
                contact.setPhone(phone);
                list.add(contact);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean deleteContact(Long id) {
        String query = "DELETE FROM contacts WHERE contact_id = ?";
        int rowEffected = 0;
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            rowEffected = preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rowEffected != 0; 
    }

    public Contact editContact(Contact contact) {
        String query = "UPDATE contacts SET " + 
            "name = ? ," + 
            "phone = ? ," + 
            "email = ? , " +
            "address = ? WHERE contact_id = ?" ;

        try (PreparedStatement preparedStatement = con.prepareStatement(query )) {
        	preparedStatement.setString(1, contact.getName());
            preparedStatement.setString(2, contact.getPhone());
            preparedStatement.setString(3, contact.getEmail());
            preparedStatement.setString(4, contact.getAddress());
            preparedStatement.setLong(5, contact.getId());
            
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contact;
    }

    public Contact insertLabels(Contact contact, ArrayList<Label> labels) {
        ArrayList<Label> labelList = new ArrayList<>();
        for (Label label : labels) {
            String query = "INSERT INTO contact_label (contact_id, label_id) VALUES (?, ?)";
            int rowEffected = 0;
            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                preparedStatement.setLong(1, contact.getId());
                preparedStatement.setLong(2, label.getId());
                rowEffected = preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (rowEffected > 0) {
                labelList.add(label);
            }
        }
        contact.setLabels(labelList);
        return contact;
    }
    public long deleteLabels(Contact contact) {
        String query = "DELETE FROM contact_label WHERE contact_id = ?";

        int rowEffected = 0;
        try (PreparedStatement pStatement = con.prepareStatement(query)) {
            pStatement.setLong(1, contact.getId());
            rowEffected = pStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowEffected;
    }
    public ArrayList<Long> getLabelsIds(Long contact_id) {
        ArrayList<Long> list = new ArrayList<>();
        String query = "SELECT * FROM contact_label WHERE contact_id = ?";

        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setLong(1, contact_id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                list.add(rs.getLong("label_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
