package ReplaceConditionalWithPolymorphismTests.example2;

public class Bird {

    enum BirdType {
        PENGUIN,
        EUROPEAN,
        NORWEGIAN_BLUE
    }

    private BirdType type;
    private int baseSpeed;

    public Bird(BirdType type, int baseSpeed) {
        this.type = type;
        this.baseSpeed = baseSpeed;
    }

    public int getSpeed() {
        switch (type) {
            case PENGUIN:
                return 1;
            case EUROPEAN:
                return 2 * baseSpeed;
            case NORWEGIAN_BLUE:
                int totalSpeed = baseSpeed > 10 ? baseSpeed : 10;
                return totalSpeed;
        }
        return -1;
    }

}
