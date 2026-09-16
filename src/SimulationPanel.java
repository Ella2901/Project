import javax.swing.JPanel;
import java.awt.Graphics;

public class SimulationPanel extends JPanel {
    private Warehouse warehouse;

    public SimulationPanel(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawString("Warehouse Delivery Robot Simulation", 50, 50);
        
        // Get the grid from the warehouse
        Grid<Entity> grid = warehouse.getGrid();

        //
        for (int i = 0; i < grid.size(); i++) {
            Entity entity = grid.getEntity(i);
            int x = entity.getX();
            int y = entity.getY();

            // Draw Robot
            if (entity instanceof Robot) {
                Robot robot = (Robot) entity;
                g.fillRect(x, y, 40, 40);
                g.drawString("Robot", x + 5, y + 30);

                g.drawString("Battery: " + robot.getBattery(), x, y - 10);
            }

            // Draw Package
            else if (entity instanceof Package){
                g.drawRect(x, y, 30, 30);
                g.drawString("Package", x - 10, y - 10);
            }

            // Draw Charging Station
            else if (entity instanceof ChargingStation) {
                g.drawRect(x, y, 100, 50);
                g.drawString("Charging", x + 20, y + 30);
            }

            // Draw Delivery Station
            else if (entity instanceof DeliveryStation) {
                g.drawRect(x, y, 100, 50);
                g.drawString("Delivery", x + 20, y + 30);
            }

            // Draw Shelf
            else if (entity instanceof Shelf) {
                g.drawRect(x, y, 50, 50);
                g.drawString("Shelf", x + 10, y + 30);
            }

            // Draw other entities if needed
            else {
                g.drawOval(x, y, 20, 20);
                g.drawString("Entity", x, y);
            }
        }
    }
}