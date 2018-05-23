package ReplaceConditionalWithPolymorphismTests.example1;

public class SpecialNumberOriginal {

    private int specialNumber = generateNum();

    public int getSpecialNumber() {
        switch(specialNumber) {
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
        SpecialNumberOriginal specialNumber = new SpecialNumberOriginal();
        System.out.println(specialNumber.getSpecialNumber());

        //polymorphism
        int num = 1;
        SpecialNumberOriginal specialNumberOriginal;
        //TODO: create method instantiateTheRightThing
        specialNumberOriginal = instantiateTheRightThing(num);
        System.out.println(specialNumberOriginal.getSpecialNumber());
    }
}




