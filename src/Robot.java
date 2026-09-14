public class Robot extends Entity {
    private int battery;

    public Robot(int x, int y, int battery) {
        super(x, y);
        this.battery = battery;
    }

    public int getBattery() {
        return battery;
    }

    // Override the display method from entity
    @Override
    public void display() {
        // Implementation for displaying robot
        System.out.println("Robot at (" + getX() + ", " + getY() + ") with battery: " + battery);
    }

    // Overload move method1
    // Move 1 unit to the right
    public void move() {
       x++;
    }

    // Overload move method2
    // Move a specified distance to the right
    public void move(int distance) {
        x += distance;
    }
    
    /* 
    public void move(int dx, int dy) {
        x += dx;
        y += dy;
        battery -= (Math.abs(dx) + Math.abs(dy)); // Decrease battery based on movement
        if (battery < 0) {
            battery = 0; // Ensure battery doesn't go below 0
        }
    }
    */

}
