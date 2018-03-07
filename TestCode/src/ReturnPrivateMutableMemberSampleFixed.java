import java.util.ArrayList;
import java.util.Date;

public class ReturnPrivateMutableMemberSampleFixed {

    private Date d;
    private String s;
    private ArrayList<Date> list;

    public ReturnPrivateMutableMemberSampleFixed() {
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
