package bussiness;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import Exceptions.DoesNotExistException;
import Exceptions.UnAuthorizedActionException;
import dao.ContactDao;
import entities.Contact;
import entities.Label;
import entities.ProfilePicture;
import entities.User;

public class ContactBussiness {

    ContactDao contactDao;

    public ContactBussiness() throws ClassNotFoundException, SQLException {
        contactDao = new ContactDao();
    }
    
    public Contact save(Contact contact) throws SQLException {
        return contactDao.saveContact(contact);      
    }

    public ArrayList<Label> getLabels(Long contact_id) throws SQLException, ClassNotFoundException {
        var list = contactDao.getLabelsIds(contact_id);
        LabelBussiness labelBussiness = new LabelBussiness();
        return labelBussiness.getLabels(list);
    }

public String getContactsString(User user) throws SQLException, ClassNotFoundException{
        var list = contactDao.getContacts(user);
        if (list.size() == 0)
            return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Contact contact : list) {
            contact.setLabels(getLabels(contact.getId()));
            sb.append(contact.toJson());
            sb.append(",");
        }
        int x = sb.lastIndexOf(",");
        if (x != -1)
            sb.deleteCharAt(x);
        sb.append("]");
        return sb.toString();
    }

    public boolean deleteContact(User user, long contact_id) throws UnAuthorizedActionException, SQLException, DoesNotExistException {
        autherize(user, contact_id);
        return contactDao.deleteContact(contact_id);
    }

    public Contact editContact(User user, Contact contact) throws SQLException, DoesNotExistException, UnAuthorizedActionException{
        autherize(user, contact.getId());
        return contactDao.editContact(contact);
    }

    public String editLabels(User user, long contact_id, ArrayList<Long> ids) throws SQLException, DoesNotExistException, UnAuthorizedActionException, ClassNotFoundException {
        Contact contact = autherize(user, contact_id);
        LabelBussiness labelBussiness = new LabelBussiness();
        labelBussiness.autherize(user, ids);
        ArrayList<Label> labelList = labelBussiness.getLabels(ids);
        Contact toReturn = null;

        contactDao.deleteLabels(contact);
        contactDao.insertLabels(contact, labelList);
        toReturn = contactDao.getContact(contact_id);
        toReturn.setLabels(getLabels(contact_id));
        return toReturn.toJson();
    }

    private Contact autherize(User user, long contact_id) throws SQLException, DoesNotExistException, UnAuthorizedActionException {
        Contact contactFromDatabase = contactDao.getContact(contact_id);
        if (contactFromDatabase == null) {
            throw new DoesNotExistException("Contact with id " + contact_id + " does not exist");
        }
        if ( !(contactFromDatabase.getUser_id() == user.getId())) {
            throw new UnAuthorizedActionException("UnAuthorized attempt to access contact");
        }
        return contactFromDatabase;

    }

    public boolean saveProfilePicture(User user, ProfilePicture profilePicture) throws SQLException, DoesNotExistException, UnAuthorizedActionException, IOException {
        autherize(user, profilePicture.getContact_id());
        String path = "D:\\Programming\\Java\\Servlet And JSP\\contacts\\Images\\" + profilePicture.getContact_id() + "." + profilePicture.getType();
                
        try(FileOutputStream fos = new FileOutputStream(path)) {
            fos.write(profilePicture.getInputStream().readAllBytes());
        }
        File file = new File(path);
        try {
            if (!contactDao.saveProfilePicture(profilePicture)) {
                file.delete();
                return false;
            }
        } catch (SQLException e) {
            file.delete();
            return false;
        }
        return true;
    }

    public ProfilePicture getProfilePicture(User user, long contact_id) throws SQLException, DoesNotExistException, UnAuthorizedActionException, IOException {
        autherize(user, contact_id);
        ProfilePicture pp = contactDao.getProfilePicture(contact_id);
        File file = new File("D:\\Programming\\Java\\Servlet And JSP\\contacts\\Images\\" + pp.getContact_id() + "." + pp.getType());
        
        if (file.exists()) {
            FileInputStream fis = new FileInputStream(file);
            pp.setInputStream(fis);
            return pp;
        }
        return null;
    }
}
