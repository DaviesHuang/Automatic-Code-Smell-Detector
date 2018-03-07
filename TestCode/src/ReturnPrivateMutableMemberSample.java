import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReturnPrivateMutableMemberSample {

    private Date d;
    private String s;
    private ArrayList<Date> list;

    public ReturnPrivateMutableMemberSample() {
        d = new Date();
    }

    public Date getDate() {
        return d;
    }

    public String getString() {
        return s;
    }

    public Date getLocalVeriable() {
        Date d2 = new Date();
        return d2;
    }

    public ArrayList<Date> getList() {
        return list;
    }

}
