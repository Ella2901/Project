import java.util.ArrayList;
import java.util.List;

public class Robot extends Entity {
    private int battery;
    private List<RobotObserver> observers = new ArrayList<>();

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
        move(1);
    }

    // Overload move method2
    // Move a specified distance
    public void move(int distance) {
        if (distance == 0) {
            return;
        }

        x += distance;
        battery -= Math.abs(distance);

        if (battery < 0) {
            battery = 0;
        }

        notifyObservers();
    }
    
    private MovementStrategy movementStrategy;

    public void setMovementStrategy(MovementStrategy strategy) {
        this.movementStrategy = strategy;
    }

    public void performMove(Warehouse warehouse, int direction) {
        if (movementStrategy != null) {
            movementStrategy.move(this, warehouse, direction);
        }
    }


    public void addObserver(RobotObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(RobotObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (RobotObserver observer : observers) {
            observer.onRobotMoved(this);
        }
    }


}
