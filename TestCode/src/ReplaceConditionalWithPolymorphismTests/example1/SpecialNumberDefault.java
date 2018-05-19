package ReplaceConditionalWithPolymorphismTests.example1;

public class SpecialNumberDefault extends SpecialNumber {
    public SpecialNumberDefault(int specialNumber) {
        super(specialNumber);
    }

    @Override protected int zzz() {
        return -1;
    }
}
