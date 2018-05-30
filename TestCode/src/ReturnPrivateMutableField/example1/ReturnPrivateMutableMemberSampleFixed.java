package ReturnPrivateMutableField.example1;

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
        return (Date) ggggg().clone();
    }

    private Date ggggg() {
        return (Date) d.clone();
    }

    public String getString() {
        return s;
    }

    public Date getLocalVeriable() {
        Date d2 = new Date();
        return d2;
    }

    private ArrayList<Date> getList() {
        return (ArrayList<Date>) list.clone();
    }
}


