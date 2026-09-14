public class ChargingStation extends Station {
    public ChargingStation(String name, int x, int y) {
        super(name, x, y);    
    }

    @Override
    public void display() {
        // Implementation for displaying charging station
        System.out.println("Charging Station: " + getName() + " at (" + getX() + ", " + getY() + ")");
    }
}
