public class Bird {

    enum BirdType {
        PENGUIN,
        SPARROW,
        PARROT
    }

    private BirdType type;
    private Speed baseSpeed;

    Bird(BirdType type, Speed baseSpeed) {
        this.type = type;
        this.baseSpeed = baseSpeed;
    }

    public Speed getSpeed() {
        int speed = baseSpeed.getSpeed();
        switch (type) {
            case PENGUIN:
                return new Speed(1);
            case SPARROW:
                return new Speed(2 * speed);
            case PARROT:
                return new Speed(speed > 10 ? speed : 10);
            default:
                return baseSpeed;
        }
    }

    public static void main(String[] args) {
        Bird aBird = new Bird(BirdType.PENGUIN, new Speed(1));
        System.out.println(aBird.getSpeed());
    }

}
