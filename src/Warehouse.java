import java.util.IntSummaryStatistics;
import java.util.Map;
import java.util.stream.Collectors;

public class Warehouse {
    // 1. Fields and Constructor
    private final Grid<Entity> grid;
    private final Object simulationLock = new Object();

    public Warehouse() {
        grid = new Grid<>(800, 600);
    }

    public Grid<Entity> getGrid() {
        return grid;
    }

    public Object getSimulationLock() {
        return simulationLock;
    }

    // 2. Entity Management
    public void addEntity(Entity entity) {
        synchronized (simulationLock) {
            grid.addEntity(entity);
        }
    }

    public void displayEntities() {
        synchronized (simulationLock) {
            grid.displayEntities();
        }
    }

    // 3. Robot Movement
    public void moveRobot(Robot robot, int distance) {
        synchronized (simulationLock) {

            int newX = robot.getX() + distance;
            int newY = robot.getY();

            // Check that the new position is valid
            grid.checkPosition(newX, newY);

            // Move the robot
            robot.move(distance);
        }
    }

    // 4. Package Pickup
    public boolean pickUpPackage(Robot robot, Package packageToPickUp) {
        synchronized (simulationLock) {

            // Ensure robot and package belong to the warehouse
            boolean robotExists = grid.stream()
                .anyMatch(entity -> entity == robot);

            boolean packageExists = grid.stream()
                .anyMatch(entity -> entity == packageToPickUp);

            if (!robotExists || !packageExists) {
                return false;
            }

            // Prevent duplicate pickup
            if (packageToPickUp.isPickedUp()
                    || packageToPickUp.isDelivered()) {
                return false;
            }

            // Check that the robot has reached the package
            if (robot.getX() == packageToPickUp.getX()
                    && robot.getY() == packageToPickUp.getY()) {

                packageToPickUp.pickUp();

                System.out.println(
                    "Robot picked up package: " + packageToPickUp.getId()
                );

                return true;
            }

            return false;
        }
    }

    // 5. Statistics and Stream Operations
    public void displayBatteryStatistics() {
        synchronized (simulationLock) {

            IntSummaryStatistics stats = grid.stream()
                .filter(entity -> entity instanceof Robot)
                .map(entity -> (Robot) entity)
                .collect(
                    Collectors.summarizingInt(Robot::getBattery)
                );

            if (stats.getCount() == 0) {
                System.out.println("No robots in warehouse");
                return;
            }

            System.out.println("Total robots: " + stats.getCount());
            System.out.println("Average Battery: " + stats.getAverage());
        }
    }

    public void displayRobotStateSummary() {
        synchronized (simulationLock) {

            Map<String, Long> stateCounts = grid.stream()
                .filter(entity -> entity instanceof Robot)
                .map(entity -> (Robot) entity)
                .collect(
                    Collectors.groupingBy(
                        robot -> robot.getState() == null
                            ? "No State"
                            : robot.getState().getClass().getSimpleName(),
                        Collectors.counting()
                    )
                );

            System.out.println("=== Robot State Summary ===");

            stateCounts.forEach((state, count) ->
                System.out.println(state + ": " + count)
            );
        }
    }
}