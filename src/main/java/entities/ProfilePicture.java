package entities;

import java.io.InputStream;

public class ProfilePicture {
    private Long contact_id;
    private String type;
    private InputStream inputStream;

    public ProfilePicture() {
    }
    public ProfilePicture(Long contact_id, String type, InputStream inputStream) {
        this.contact_id = contact_id;
        this.type = type;
        this.inputStream = inputStream;
    }
    public InputStream getInputStream() {
        return inputStream;
    }
    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
    public Long getContact_id() {
        return contact_id;
    }
    public void setContact_id(Long contact_id) {
        this.contact_id = contact_id;
    }
    
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
