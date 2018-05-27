package ReplaceConditionalWithPolymorphismTests.example1;

public class SpecialNumber {

    private int specialNumber = generateNum();
    protected int i = 0;

    SpecialNumber(int specialNumber) {
        this.specialNumber = specialNumber;
    }

    public int getSpecialNumber() {
        switch(specialNumber)
        {
            case 1:
                return i++;
            case 2:
                String f = "";
                f += "hi";
                return 2;
            case 3:
                return 3;
            default:
                return -1;
        }
    }

    public int containOtherStatement() {
        i++;
        switch(specialNumber)
        {
            case 5:
                return i++;
            case 2:
                return 2;
            case 3:
                return 3;
            default:
                return -1;
        }
    }

    public int useParameterInOneCase(int num) {
        switch(specialNumber)
        {
            case 1:
                return num;
            case 2:
                return 2;
            case 4:
                return 3;
            default:
                return -1;
        }
    }

    public int usePrivateFieldInCase() {
        switch(specialNumber)
        {
            case 0:
                return i;
            case 2:
                return 2;
            case 3:
                return 3;
            default:
                return -1;
        }
    }

    public int callPrivateMethodInCase() {
        switch(specialNumber)
        {
            case 0:
                return generateNum();
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
        SpecialNumber specialNumber = new SpecialNumber(1);
        specialNumber.getSpecialNumber();

        //polymorphism
//      SpecialNumber specialNumber = createSpecialNumber(1);
//      specialNumber.getSpecialNumber();
    }



}




