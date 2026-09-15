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

        Grid<Entity> grid = new Grid<>();

        grid.addEntity(robot);
        grid.addEntity(package1);
        grid.addEntity(chargingStation);
        
        grid.displayEntities();
    }
}
