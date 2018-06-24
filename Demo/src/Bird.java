public class Bird {

    enum BirdType {
        PENGUIN,
        SPARROW,
        PARROT
    }

    private BirdType type;
    private int baseSpeed;
    private final int maxSpeed = 100;

    Bird(BirdType type, int baseSpeed) {
        this.type = type;
        this.baseSpeed = baseSpeed;
    }

    public int getSpeed() {
        if (baseSpeed < 0 || baseSpeed > maxSpeed) {
            return 0;
        }
        switch (type) {
            case PENGUIN:
                return 1;
            case SPARROW:
                return 2 * baseSpeed;
            case PARROT:
                return baseSpeed > 10 ? baseSpeed : 10;
            default:
                return 0;
        }
    }
}
