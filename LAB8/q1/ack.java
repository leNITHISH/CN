
import java.io.Serializable;

public class Ack implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int ack_num;

    public Ack() { }

    public Ack(int ack_num) {
        this.ack_num = ack_num;
    }

    public int getAck_num() {
        return ack_num;
    }

    public void setAck_num(int ack_num) {
        this.ack_num = ack_num;
    }

    @Override
    public String toString() {
        return "Ack [ack_num=" + ack_num + "]";
    }
}
