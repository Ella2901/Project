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

        robot.display();
        package1.display();

        robot.move();
        robot.display();

        robot.move(5);
        robot.display();
    }
}
