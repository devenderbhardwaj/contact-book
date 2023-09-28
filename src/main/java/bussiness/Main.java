package bussiness;

import entities.User;

public class Main {
    public static void main(String[] args) {
        try {
            User user = new User();
            user.setId(10);
            LabelBussiness lb = new LabelBussiness();
            System.out.println(lb.getLabels(user));;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
