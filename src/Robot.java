import java.util.ArrayList;
import java.util.List;

public class Robot extends Entity {
    // 1. Fields
    private int battery;
    private final int maxBattery;
    private final List<RobotObserver> observers = new ArrayList<>();
    private MovementStrategy movementStrategy;
    private RobotState currentState;

    // 2. Constructor and Getters
    public Robot(int x, int y, int battery) {
        super(x, y);

        if (battery <= 0) {
            throw new IllegalArgumentException(
                "Initial battery capacity must be positive"
            );
        }

        this.battery = battery;
        this.maxBattery = battery;
    }

    public int getBattery() {
        return battery;
    }

    public int getMaxBattery() {
        return maxBattery;
    }

    // Recharge the battery to its maximum capacity
    public void recharge() {
        battery = maxBattery;

        System.out.println(
            "Robot fully charged. Battery: " + battery
        );

        notifyObservers();
    }

    // 3. Display Method
    @Override
    public void display() {
        System.out.println(
            "Robot at (" + getX() + ", " + getY()
            + ") with battery: " + battery
        );
    }

    // 4. Robot Movement
    // Move 1 unit to the right
    public void move() {
        move(1);
    }

    // Move horizontally by a specified distance
    public void move(int distance) {
        moveBy(distance, 0);
    }

    // Move in both X and Y directions
    public void moveBy(int dx, int dy) {

        if (dx == 0 && dy == 0) {
            return;
        }

        // Manhattan distance determines battery usage
        long requiredBattery =
            Math.abs((long) dx) + Math.abs((long) dy);

        if (battery < requiredBattery) {
            System.out.println("Not enough battery to move.");
            return;
        }

        // Update robot position
        x += dx;
        y += dy;

        // Reduce battery based on movement distance
        battery -= (int) requiredBattery;

        // Notify observers to update the GUI
        notifyObservers();
    }

    // 5. Strategy Pattern
    public void setMovementStrategy(MovementStrategy strategy) {
        this.movementStrategy = strategy;
    }

    public void performMove(Warehouse warehouse, int direction) {
        if (movementStrategy != null) {
            movementStrategy.move(this, warehouse, direction);
        }
    }

    // 6. Observer Pattern
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

    public void notifyTaskUpdated(){
        notifyObservers();
    }

    // 7. State Pattern
    public void setState(RobotState state) {
        this.currentState = state;
        notifyObservers();
    }

    public RobotState getState() {
        return currentState;
    }

    public void performAction() {
        if (currentState != null) {
            currentState.handle(this);
        }
    }
}
