package ReplaceConditionalWithPolymorphismTests.example2;

public class Bird {

    enum BirdType {
        PENGUIN,
        SPARROW,
        PARROT
    }

    private BirdType type;
    private int baseSpeed;

    Bird(BirdType type, int baseSpeed) {
        this.type = type;
        this.baseSpeed = baseSpeed;
    }

    public int getSpeed() {
        if (baseSpeed < 0) {
            return 0;
        }
        switch (type) {
            case PENGUIN:
                return 1;
            case SPARROW:
                return 2 * baseSpeed;
            case PARROT:
                int totalSpeed = baseSpeed > 10 ? baseSpeed : 10;
                return totalSpeed;
            default:
                return 0;
        }
    }

    public static void main(String[] args) {
        Bird aBird = new Bird(BirdType.PENGUIN, 1);
        System.out.println(aBird.getSpeed());
    }

}
