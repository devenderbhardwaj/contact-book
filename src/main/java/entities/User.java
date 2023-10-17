package entities;

import java.util.Arrays;

public class User {
    private long id;
    private String name;
    private String email;
    private byte[] salt;
    private byte[] hashPassword;
    
    public User(long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
    public User() {
    }
    public byte[] getSalt() {
        return salt;
    }
    public void setSalt(byte[] salt) {
        this.salt = salt;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public byte[] getHashPassword() {
        return hashPassword;
    }
    public void setHashPassword(byte[] password) {
        this.hashPassword = password;
    }
    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + Arrays.toString(this.hashPassword) + "]";
    }
}
