package entities;

import java.util.ArrayList;

public class Contact {
    private long id;
    private String name;
    private String phone;
    private String email;
    private String address;
    private long user_id;
    private ArrayList<Label> labels = new ArrayList<>();

    public ArrayList<Label> getLabels() {
        return labels;
    }

    public void setLabels(ArrayList<Label> labels) {
        this.labels = labels;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public Contact(long id) {
        this.id = id;
    }

    public Contact(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    

    public String toJson()  {
        return "{\"contact_id\":"+id+", \"name\":\""+name+"\", \"phone\":\""+phone+"\", \"email\":\""+email+"\",\"address\":\""+address + "\", \"labels\":" + labels.toString() + "}";
    }

    @Override
    public String toString() {
        return "Contact [id=" + id + ", name=" + name + ", phone=" + phone + ", email=" + email + ", address=" + address
                + ", user_id=" + user_id + ", labels=" + labels.toString() + "]";
    }
}
