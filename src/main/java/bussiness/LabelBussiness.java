package bussiness;

import java.sql.SQLException;
import java.util.ArrayList;

import Exceptions.DoesNotExistException;
import Exceptions.UnAuthorizedActionException;
import dao.LabelDao;
import entities.Label;
import entities.User;

public class LabelBussiness {
    LabelDao labelDao;

    public LabelBussiness() throws ClassNotFoundException, SQLException{
        labelDao = new LabelDao();
    }

    public Label addLabel(Label label, User user) throws SQLException {
        return addLabel(label.getText(), user.getId());
    }

    public Label addLabel(String text, Long user_id) throws SQLException {
        return  labelDao.addLabel(text, user_id);
    }

    public Label addLabel(String text, User user) throws SQLException {
        return addLabel(text, user.getId());
    }

    public ArrayList<Label> getLabels (ArrayList<Long> ids) throws SQLException {
        ArrayList<Label> list = new ArrayList<>();
        for (long id : ids) {
            list.add(labelDao.getLabel(id));
        }
        return list;
    }

    public ArrayList<Label> getLabels(User user) throws SQLException {
        return labelDao.getLabels(user);
    }
    public String getLabelsString(User user) throws SQLException {
        ArrayList<Label> list = labelDao.getLabels(user);
        if (list.size() == 0 ) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        for (Label label : list) {
            sb.append(label.toJson()).append(", ");
        }
        int index = sb.lastIndexOf(",");
        sb.deleteCharAt(index);
        sb.append("]");
        return sb.toString();
    }

    public boolean deleteLabel(User user, long label_id) throws SQLException, DoesNotExistException, UnAuthorizedActionException {
        if (!autherize(user, label_id)) {
            throw new UnAuthorizedActionException("UnAuthorized attempt to delete label");
        }
        return labelDao.deleteLabel(label_id);

    }

    private boolean autherize(User user, long label_id) throws SQLException, DoesNotExistException {
        Label label = labelDao.getLabel(label_id);
        if (label == null) {
            throw new DoesNotExistException("Label Does not Exist");
        }
        return label.getUser_id() == user.getId();
    }

    public boolean autherize(User user, ArrayList<Long> ids) throws SQLException, DoesNotExistException {
        for (long id: ids) {
            autherize(user, id);
        }
        return true;
    }

}
