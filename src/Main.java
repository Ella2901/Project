import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Run Swing components on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {

            // 1. Create the Warehouse
            Warehouse warehouse = new Warehouse();

            // 2. Create and Configure the Robot
            Robot robot = new Robot(100, 100, 1000);

            // Strategy Pattern: select movement speed
            robot.setMovementStrategy(new FastMovement());

            // State Pattern: robot starts idle
            robot.setState(new IdleState());
            robot.performAction();

            // 3. Create Warehouse Entities
            Package package1 = new Package("P001", 200, 150);

            ChargingStation chargingStation =
                new ChargingStation("CS001", 100, 400);

            DeliveryStation deliveryStation =
                new DeliveryStation("DS001", 600, 400);

            // 4. Add Entities to the Warehouse
            warehouse.addEntity(robot);
            warehouse.addEntity(package1);
            warehouse.addEntity(chargingStation);
            warehouse.addEntity(deliveryStation);

            // 5. Display Statistics Using Streams
            warehouse.displayBatteryStatistics();

            Grid<Entity> grid = warehouse.getGrid();

            long robotCount = grid.stream()
                .filter(entity -> entity instanceof Robot)
                .count();

            long packageCount = grid.stream()
                .filter(entity -> entity instanceof Package)
                .count();

            System.out.println("Number of robots: " + robotCount);
            System.out.println("Number of packages: " + packageCount);

            // 6. Create the Swing Window
            JFrame window = new JFrame(
                "Warehouse Delivery Robot Simulation"
            );

            SimulationPanel panel = new SimulationPanel(warehouse);

            // Observer Pattern: update GUI when robot changes
            robot.addObserver(panel);

            window.add(panel);
            window.setSize(800, 600);

            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setLocationRelativeTo(null);
            window.setVisible(true);

            // 7. Start the Background Simulation
            SimulationRunner runner = new SimulationRunner(
                robot,
                warehouse,
                package1,
                deliveryStation
            );

            Thread simulationThread = new Thread(
                runner,
                "SimulationThread"
            );

            simulationThread.start();
        });
    }
}
