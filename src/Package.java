public class Package extends Entity {
    private String id;
    private boolean delivered;

    public Package(String id, int x, int y) {
        super(x, y);
        this.id = id;
        this.delivered = false;
    }

    @Override
    public void display() {
        // Implementation for displaying package
        System.out.println("Package ID: " + id + " at (" + getX() + ", " + getY() + ")");
    }
}