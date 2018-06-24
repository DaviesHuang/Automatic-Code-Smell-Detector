package tests;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class CyclomaticComplexitySample {

    private Date d;
    private String s;
    private ArrayList<Date> list;
    HashMap h;

    public Date getDate() {
        if (true || false) {
            return new Date();
        }
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
        return (ArrayList<Date>) list.clone();
    }

    public void complexMethod() {
        if (true) {
            if (true || false) {
                return;
            }

            if (true || d.equals(s)) {
                if (d.compareTo(d) > 1) {
                    return;
                }
            }
        }

        System.out.println();

        if (true) {
            return;
        }
    }

}
