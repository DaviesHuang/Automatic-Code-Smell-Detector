package ReplaceConditionalWithPolymorphismTests.example2;

public class Bird {

    enum BirdType {
        PENGUIN,
        EUROPEAN,
        NORWEGIAN_BLUE
    }

    private BirdType type;
    protected int baseSpeed;

    Bird(BirdType type, int baseSpeed) {
        this.type = type;
        this.baseSpeed = baseSpeed;
    }

    Bird(BirdType type, int baseSpeed, int additionalSpeed) {
        this.type = type;
        this.baseSpeed = baseSpeed;
    }

    public int getSpeed() {
        String hmm = "";
        return getCorrectSpeed();
    }

    protected int getCorrectSpeed() {
        switch (type) {
            case PENGUIN:
                return 1;
            case EUROPEAN:
                return 2 * baseSpeed;
            case NORWEGIAN_BLUE:
                int totalSpeed = baseSpeed > 10 ? baseSpeed : 10;
                return totalSpeed;
            default:
                return 0;
        }
    }

}
