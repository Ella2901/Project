import java.util.IntSummaryStatistics;
import java.util.stream.Collectors;

public class Warehouse {
    private Grid<Entity> grid;

    public Warehouse() {
        grid = new Grid<>(800,600);
    }

    public void addEntity(Entity entity) {
        grid.addEntity(entity);
    }

    public void moveRobot(Robot robot, int distance) {
        int newX = robot.getX() + distance;
        int newY = robot.getY(); // Assuming movement is only horizontal for simplicity

        grid.checkPosition(newX, newY); // Check if the new position is valid

        robot.move(distance); // Move the robot
    }

    public void displayEntities() {
        grid.displayEntities();
    }

    public void displayBatteryStatistics() {
        IntSummaryStatistics stats = grid.stream()
        .filter(e -> e instanceof Robot)
        .map(e -> (Robot) e)
        .collect(Collectors.summarizingInt(Robot::getBattery));  
        
        if (stats.getCount() == 0){
            System.out.println("No robots in warehouse");
            return;
        }

        System.out.println("Total robots: " + stats.getCount());
        System.out.println("Average Battery: " + stats.getAverage());
    }

    public Grid<Entity> getGrid() {
        return grid;
    }
}
