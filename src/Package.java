public class Package extends Entity {
    private String id;

    public Package(String id, int x, int y) {
        super(x, y);
        this.id = id;
    }

    @Override
    public void display() {
        // Implementation for displaying package
        System.out.println("Package ID: " + id + " at (" + getX() + ", " + getY() + ")");
    }
}