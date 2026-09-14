public class Station extends Entity {
    protected String name;

    public Station(String name, int x, int y) {
        super(x, y);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void display() {
        // Implementation for displaying station
        System.out.println("Station: " + name + " at (" + getX() + ", " + getY() + ")");
    }
}
