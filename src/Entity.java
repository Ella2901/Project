public class Entity {
    protected int x;
    protected int y;

    public Entity(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void display() {
        // Default implementation for displaying entity
        System.out.println("Entity at (" + x + ", " + y + ")");
    }
}
