import java.util.ArrayList;
import java.util.List;

public class Robot extends Entity {
    private int battery;
    private List<RobotObserver> observers = new ArrayList<>();
    private RobotState currentState;

    private MovementStrategy movementStrategy;

    public Robot(int x, int y, int battery) {
        super(x, y);
        this.battery = battery;
    }

    public int getBattery() {
        return battery;
    }

    // Override the display method from Entity
    @Override
    public void display() {
        System.out.println("Robot at (" + getX() + ", " + getY()
                + ") with battery: " + battery);
    }

    // Overloaded movement method
    // Move 1 unit to the right
    public void move() {
        move(1);
    }

    // Move a specified distance
    public synchronized void move(int distance) {
        if (distance == 0) {
            return;
        }

        int requiredBattery = Math.abs(distance);

        // Prevent movement when battery is insufficient
        if (battery < requiredBattery) {
            System.out.println("Not enough battery to move.");
            return;
        }

        // Update robot position and battery
        x += distance;
        battery -= requiredBattery;

        // Notify observers when the robot moves
        notifyObservers();
    }

    // Strategy Pattern: Set movement strategy
    public void setMovementStrategy(MovementStrategy strategy) {
        this.movementStrategy = strategy;
    }

    // Perform movement using the selected strategy
    public void performMove(Warehouse warehouse, int direction) {
        if (movementStrategy != null) {
            movementStrategy.move(this, warehouse, direction);
        }
    }

    // Observer Pattern: Register an observer
    public void addObserver(RobotObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    // Remove an observer
    public void removeObserver(RobotObserver observer) {
        observers.remove(observer);
    }

    // Notify observers when robot position or state changes
    private void notifyObservers() {
        for (RobotObserver observer : observers) {
            observer.onRobotMoved(this);
        }
    }

    // State Pattern: Set the current robot state
    public void setState(RobotState state) {
        this.currentState = state;
        notifyObservers();
    }

    // Get the current robot state
    public RobotState getState() {
        return currentState;
    }

    // Perform the action associated with the current state
    public void performAction() {
        currentState.handle(this);
    }
}