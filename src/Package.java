public class Package extends Entity {
    private String id;
    private boolean pickedUp;
    private boolean delivered;

    public Package(String id, int x, int y) {
        super(x, y);
        this.id = id;
        this.pickedUp = false;
        this.delivered = false;
    }

    public String getId() {
        return id;
    }

    public boolean isPickedUp() {
        return pickedUp;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void pickUp() {
        if (!delivered) {
            pickedUp = true;
        }
    }

    public void deliver() {
        if (pickedUp) {
            delivered = true;
        }
    }

    @Override
    public void display() {
        System.out.println("Package ID: " + id
                + " at (" + getX() + ", " + getY() + ")"
                + " | Picked up: " + pickedUp
                + " | Delivered: " + delivered);
    }
}