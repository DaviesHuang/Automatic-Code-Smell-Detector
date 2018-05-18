package ReplaceConditionalWithPolymorphismTests.example1;

public class SpecialNumberOne extends AbstractSpecialNumber {

    @Override
    public int generateNum() {
        return 1;
    }

    @Override
    public int getSpecialNumber() {
        return 1;
    }
}
