
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create the warehouse
            Warehouse warehouse = new Warehouse();

            // Create the robot and select a movement strategy
            Robot robot = new Robot(100, 100, 1000);
            robot.setMovementStrategy(new FastMovement());

            // Create warehouse entities
            Package package1 = new Package("P001", 200, 150);
            ChargingStation chargingStation =
                new ChargingStation("CS001", 100, 400);
            DeliveryStation deliveryStation =
                new DeliveryStation("DS001", 600, 400);
            Shelf shelf = new Shelf("S001", 600, 150);

            // Add entities to the warehouse
            warehouse.addEntity(robot);
            warehouse.addEntity(package1);
            warehouse.addEntity(chargingStation);
            warehouse.addEntity(deliveryStation);
            warehouse.addEntity(shelf);

            // Display warehouse statistics using streams
            warehouse.displayBatteryStatistics();

            Grid<Entity> grid = warehouse.getGrid();

            long robotCount = grid.stream()
                .filter(e -> e instanceof Robot)
                .count();
            System.out.println("Number of robots: " + robotCount);

            long packageCount = grid.stream()
                .filter(e -> e instanceof Package)
                .count();
            System.out.println("Number of packages: " + packageCount);

            // Create the graphical simulation window
            JFrame window = new JFrame("Warehouse Simulation");
            SimulationPanel panel = new SimulationPanel(warehouse);

            // Observer Pattern: update the panel when the robot moves
            robot.addObserver(panel);

            window.add(panel);
            window.setSize(800, 600);
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setVisible(true);

            // Start automatic robot movement on a background thread
            SimulationRunner runner =
                new SimulationRunner(robot, warehouse);

            Thread simulationThread =
                new Thread(runner, "SimulationThread");

            simulationThread.start();

            // Demonstrate the State Pattern
            robot.setState(new DeliveringState());
            robot.performAction();

            robot.setState(new ChargingState());
            robot.performAction();

            robot.setState(new IdleState());
            robot.performAction();

            // Display robot states using streams
            warehouse.displayRobotStateSummary();
        });
    }
}
