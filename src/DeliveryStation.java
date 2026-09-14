public class DeliveryStation extends Station {
    public DeliveryStation(String name, int x, int y) {
        super(name, x, y);
    }

    @Override
    public void display() {
        System.out.println("Delivery Station: " + getName() + " at (" + getX() + ", " + getY() + ")");
    }
}
