package entities;

public class Label {
    private long id;
    private String text;
    private long user_id;

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public Label(long id, String text) {
        this.id = id;
        this.text = text;
    }

    public Label() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String toJson() {
        return "{\"id\":" + id + ", \"text\":\"" + text + "\"}";
    }

    @Override
    public String toString() {
        return toJson();
    }
}
