package ReplaceConditionalWithPolymorphismTests.example1;

public class SpecialNumberTwo extends AbstractSpecialNumber {

    @Override
    public int generateNum() {
        return 2;
    }

    @Override
    public int getSpecialNumber() {
        return 2;
    }
}
