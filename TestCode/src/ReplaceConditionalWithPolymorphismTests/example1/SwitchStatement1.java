package ReplaceConditionalWithPolymorphismTests.example1;

import java.util.Date;

public class SwitchStatement1 {

    private int number = 0;
    public String s = "";

    public int getNumber() {

        final int x = generateNum();
        switch(generateNum())
        {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
            default:
                return -1;
        }

    }

    private int generateNum() {
        return 1;
    }

    public static void main(String[] args) {
        SwitchStatementAbs statement = new SwitchStatementNew1();
        statement.getNumber();
    }

}




