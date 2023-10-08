package bussiness;

import java.sql.SQLException;
import java.util.ArrayList;

import Exceptions.DoesNotExistException;
import Exceptions.UnAuthorizedActionException;
import dao.ContactDao;
import entities.Contact;
import entities.Label;
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
            throw new UnAuthorizedActionException("UnAuthorized attempt to delete contact");
        }
        return contactFromDatabase;

    }
}
