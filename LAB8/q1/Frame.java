
import java.io.Serializable;

public class Frame implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String payload;

    public Frame() { }

    public Frame(int id, String payload) {
        this.id = id;
        this.payload = payload;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "Frame [id=" + id + ", payload=" + payload + "]";
    }
}
