import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame("Warehouse Simulation");
        SimulationPanel panel = new SimulationPanel();

        window.add(panel);
        window.setSize(800, 600);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);

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

        Grid <Robot> robotGrid = new Grid<>();
        Robot robot2 = new Robot(150, 150, 80);
        Robot robot3 = new Robot(200, 200, 60);

        robotGrid.addEntity(robot2);
        robotGrid.addEntity(robot3);

        robotGrid.displayEntities();

        Grid <String> testGrid = new Grid<>();
    }
}
