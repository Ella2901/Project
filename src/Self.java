public class Self extends Entity {
    private String id;

    public Self(String id, int x, int y) {
        super(x, y);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public void display() {
        System.out.println("Self: " + id + " at (" + getX() + ", " + getY() + ")");
    }
}
