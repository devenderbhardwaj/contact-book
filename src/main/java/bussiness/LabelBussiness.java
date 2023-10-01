package bussiness;

import java.util.ArrayList;

import dao.LabelDao;
import entities.Label;
import entities.User;

public class LabelBussiness {
    LabelDao labelDao;

    public LabelBussiness() throws Exception {
        labelDao = new LabelDao();
    }

    public String addLabel(Label label, User user) {
        return addLabel(label.getText(), user.getId());
    }

    public String addLabel(String text, Long user_id) {
        Label label =  labelDao.addLabel(text, user_id);
        if (label == null) {
            return "null";
        }
        return label.toJson();
    }

    public boolean LabelUserRelation(User user, Long label_id) {
        Label labelfromDatabase = labelDao.getLabel(label_id);
        if (labelfromDatabase != null && labelfromDatabase.getUser_id() == user.getId()) {
            return true;
        }
        return false;
    }

    public ArrayList<Label> getLabels (ArrayList<Long> ids) {
        ArrayList<Label> list = new ArrayList<>();
        for (long id : ids) {
            list.add(labelDao.getLabel(id));
        }
        return list;
    }

    public String getLabels(User user) {
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

    public boolean deleteLabel(long label_id) {
        return labelDao.deleteLabel(label_id);
    }
}
