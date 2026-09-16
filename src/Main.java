import javax.swing.JFrame;
import javax.swing.Timer;

public class Main {
    public static void main(String[] args) {
        // Create a warehouse instance
        Warehouse warehouse = new Warehouse();

        // Create entities
        Robot robot = new Robot(100, 100, 1000);
        Package package1 = new Package("P001", 200, 150);
        ChargingStation chargingStation = new ChargingStation("CS001", 100, 400);
        DeliveryStation deliveryStation = new DeliveryStation("DS001", 600, 400);
        Shelf shelf = new Shelf("S001", 600, 150);

        // Add entities to the warehouse
        warehouse.addEntity(robot);
        warehouse.addEntity(package1);
        warehouse.addEntity(chargingStation);
        warehouse.addEntity(deliveryStation);
        warehouse.addEntity(shelf);

        // Create a simulation panel and add it to a JFrame
        JFrame window = new JFrame("Warehouse Simulation");
        SimulationPanel panel = new SimulationPanel(warehouse);

        window.add(panel);
        window.setSize(800, 600);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);

        final int[] distance = {1}; // 1 for right, -1 for left

        Timer timer = new Timer(100, e -> {
            try {
                if (robot.getX() >= 700){
                    distance[0] = -1; // Change direction to left
                } 
                
                if (robot.getX() <= 50) {
                    distance[0] = 1; // Change direction to right
                }

                // Move the robot 2 units in the current direction
                warehouse.moveRobot(robot, 2*distance[0]);

                panel.repaint(); // Refresh the panel to show updated positions
            } 

            catch (InvalidPositionException ex) {
                System.out.println(ex.getMessage());
                // Change direction if the robot hits the boundary
                distance[0] = -distance[0];
            }
        });

        timer.start(); // Start the timer to move the robot every 100 milliseconds

        /* 
        Robot robot = new Robot(100, 100, 100);
        Entity package1 = new Package("P001", 200, 150);
        Entity chargingStation = new ChargingStation("CS001", 300, 250);

        robot.display();
        package1.display();
        chargingStation.display();

        robot.move();
        robot.display();

        robot.move(5);
        robot.display();

        Grid<Entity> grid = new Grid<>(800, 600);

        grid.addEntity(robot);
        grid.addEntity(package1);
        grid.addEntity(chargingStation);

        grid.displayEntities();

        try {
            warehouse.moveRobot(robot, 50);
            robot.display();
        } catch (InvalidPositionException e) {
            System.out.println(e.getMessage());
        }

        */
    }
}
