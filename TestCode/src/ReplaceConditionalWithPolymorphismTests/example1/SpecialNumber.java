package ReplaceConditionalWithPolymorphismTests.example1;

public abstract class SpecialNumber {

    private int specialNumber = generateNum();

    public int getSpecialNumber() {
        return wwwd();
    }

    private int wwwd() {
        return cde();
    }

    private int cde() {
        return wwwq();
    }

    private int wwwq() {
        return qwer();
    }

    private int qwer() {
        return qqqa();
    }

    private int qqqa() {
        return zcf();
    }

    private int zcf() {
        return wwqed();
    }

    private int wwqed() {
        return sss();
    }

    private int sss() {
        return ddfw();
    }

    private int ddfw() {
        return ddd();
    }

    private int ddd() {
        return ttyg();
    }

    private int ttyg() {
        return wdqwfwef();
    }

    private int wdqwfwef() {
        return dwdwqdc();
    }

    protected int dwdwqdc() {
        switch(specialNumber)
        {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            default:
                return -1;
        }
    }

    private int generateNum() {
        return 1;
    }


    public static void main(String[] args) {
        //conditional
        SpecialNumber specialNumber = new SpecialNumber();
        specialNumber.getSpecialNumber();

        //polymorphism
        int num = 1;
        AbstractSpecialNumber absSpecialNumber;
        absSpecialNumber = instantiateTheRightThing(num);
        System.out.println(absSpecialNumber.getSpecialNumber());
    }

    private static AbstractSpecialNumber instantiateTheRightThing(int num) {
        switch(num) {
            case 1:
                return new SpecialNumberOne();
            case 2:
                return new SpecialNumberTwo();
            default:
                return new SpecialNumberOne();
        }
    }

}




