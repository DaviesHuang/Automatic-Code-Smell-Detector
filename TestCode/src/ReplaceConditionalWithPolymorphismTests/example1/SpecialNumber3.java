package ReplaceConditionalWithPolymorphismTests.example1;

public class SpecialNumber3 extends SpecialNumber {
    public SpecialNumber3(int specialNumber) {
        super(specialNumber);
    }

    @Override protected int zzz() {
        return 3;
    }
}
