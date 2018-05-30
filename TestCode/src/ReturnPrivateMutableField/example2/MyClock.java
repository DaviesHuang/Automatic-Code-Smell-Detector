package ReturnPrivateMutableField.example2;

import java.util.Date;

public class MyClock {

    private Date date;
    private String dateInText;

    public MyClock() {
        date = new Date();
        dateInText = date.toString();
    }

    public Date getDate() {
        return date;
    }

    public String getString() {
        return dateInText;
    }

    public static void main(String[] args) {
        MyClock clock = new MyClock();
        clock.getDate();
        clock.getString();
    }
}
