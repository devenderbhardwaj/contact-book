package bussiness;

import java.util.ArrayList;

import dao.ContactDao;
import entities.Contact;
import entities.Label;
import entities.User;

public class ContactBussiness {

    ContactDao contactDao;

    public ContactBussiness() throws Exception {
        contactDao = new ContactDao();
    }

    private Contact ContactUserRelation(User user, Long contact_id) throws Exception {
        Contact contactFromDatabase = contactDao.getContact(contact_id);
        if (contactFromDatabase != null && contactFromDatabase.getUser_id() == user.getId()) {
            contactFromDatabase.setLabels(getLabels(contact_id));
            return contactFromDatabase;
        }
        return null;
    }

    public String save(Contact contact) {
        contactDao.saveContact(contact).toJson();
        if (contact.getId() == 0) {
            return "false";
        } else {
            return contact.toJson();
        }
    }

    public ArrayList<Label> getLabels(Long contact_id) throws Exception {
        var list = contactDao.getLabelsIds(contact_id);
        LabelBussiness labelBussiness = new LabelBussiness();
        return labelBussiness.getLabels(list);
    }

    public String getContacts(User user) throws Exception {
        var list = contactDao.getContacts(user);
        if (list.size() == 0) return "[]";
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

    public boolean deleteContact(User user, long contact_id) {
        Contact contactFromDatabase = contactDao.getContact(contact_id);
        if (contactFromDatabase != null && contactFromDatabase.getUser_id() == user.getId()) {
            return contactDao.deleteContact(contact_id);
        }
        return false;
    }

    public String editContact(User user, Contact contact) throws Exception {
        Contact contactFromDatabase = contactDao.getContact(contact.getId());
        if (contactFromDatabase != null && contactFromDatabase.getUser_id() == user.getId()) {
            contact = contactDao.editContact(contact);
            contact.setLabels(getLabels(contact.getId()));
        }
        return contact.toJson();
    }

    public String editLabels(User user, Long contact_id, ArrayList<Long> ids) throws Exception {
        Contact contact = ContactUserRelation(user, contact_id);
        LabelBussiness labelBussiness = new LabelBussiness();
        ArrayList<Label> labelList = labelBussiness.getLabels(ids);
        Contact toReturn = null;
        if (contact != null) {
            contactDao.deleteLabels(contact);
            contactDao.insertLabels(contact, labelList);
            toReturn = contactDao.getContact(contact_id);
            toReturn.setLabels(getLabels(contact_id));
        } else {
            throw new Exception("Something is fishy");
        }
        return toReturn.toJson();
    }
}
