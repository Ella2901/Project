import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;

public class SimulationPanel extends JPanel implements RobotObserver {
    private final Warehouse warehouse;

    public SimulationPanel(Warehouse warehouse) {
        this.warehouse = warehouse;
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        synchronized (warehouse.getSimulationLock()) {

            Grid<Entity> grid = warehouse.getGrid();

            Robot robot = null;
            Package package1 = null;

            // Find the robot and package
            for (int i = 0; i < grid.size(); i++) {
                Entity entity = grid.getEntity(i);

                if (entity instanceof Robot && robot == null) {
                    robot = (Robot) entity;
                } else if (entity instanceof Package
                        && package1 == null) {
                    package1 = (Package) entity;
                }
            }

            // 1. Simulation Information
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 16));

            g.drawString(
                "Warehouse Delivery Robot Simulation", 30, 25
            );

            g.setFont(new Font("Arial", Font.PLAIN, 13));

            if (robot != null) {
                g.drawString(
                    "Robot Battery: " + robot.getBattery(),
                    30, 45
                );

                // Display current robot state
                String stateName = robot.getState() == null
                    ? "No State"
                    : robot.getState().getClass().getSimpleName();

                g.drawString(
                    "Robot State: " + stateName,
                    30, 65
                );
            }

            if (package1 != null) {

                String status;

                if (package1.isDelivered()) {
                    status = "Delivered";
                } else if (package1.isPickedUp()) {
                    status = "Delivering";
                } else {
                    status = "Waiting for pickup";
                }

                g.drawString(
                    "Package Status: " + status, 250, 45
                );
            }

            // 2. Draw Warehouse Entities
            for (int i = 0; i < grid.size(); i++) {
                Entity entity = grid.getEntity(i);

                int x = entity.getX();
                int y = entity.getY();

                // Draw Robot
                if (entity instanceof Robot) {

                    g.setColor(Color.BLUE);
                    g.fillRect(x, y, 40, 40);

                    g.setColor(Color.WHITE);
                    g.drawString("Robot", x + 4, y + 24);

                }

                // Draw Package
                else if (entity instanceof Package) {

                    Package pkg = (Package) entity;

                    // Show package only at original position
                    // while waiting for pickup
                    if (!pkg.isPickedUp()) {

                        g.setColor(Color.ORANGE);
                        g.fillRect(x, y, 30, 30);

                        g.setColor(Color.BLACK);
                        g.drawString("Package", x, y - 5);
                    }
                }

                // Draw Charging Station
                else if (entity instanceof ChargingStation) {

                    g.setColor(new Color(80, 170, 100));
                    g.fillRect(x, y, 100, 50);

                    g.setColor(Color.WHITE);
                    g.drawString("Charging", x + 20, y + 30);
                }

                // Draw Delivery Station
                else if (entity instanceof DeliveryStation) {

                    g.setColor(new Color(160, 100, 190));
                    g.fillRect(x, y, 100, 50);

                    g.setColor(Color.WHITE);
                    g.drawString("Delivery", x + 20, y + 30);
                }
            }

            // 3. Draw Package During Delivery
            if (robot != null && package1 != null) {

                // Package is being carried by robot
                if (package1.isPickedUp()
                        && !package1.isDelivered()) {

                    g.setColor(Color.ORANGE);
                    g.fillRect(
                        robot.getX() + 25,
                        robot.getY() - 12,
                        20, 20
                    );
                }
            }

            // 4. Draw Completed Deliveries
            for (int i = 0; i < grid.size(); i++) {
                Entity entity = grid.getEntity(i);

                if (entity instanceof Package) {
                    Package pkg = (Package) entity;

                    if (pkg.isDelivered()) {
                        for (int j = 0; j < grid.size(); j++) {
                            Entity station = grid.getEntity(j);

                            if (station instanceof DeliveryStation) {
                                g.setColor(Color.ORANGE);
                                g.fillRect(
                                    station.getX() + 60,
                                    station.getY() + 10,
                                    20, 20
                                );
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    // Observer Pattern: repaint when robot changes
    @Override
    public void onRobotMoved(Robot robot) {
        repaint();
    }
}