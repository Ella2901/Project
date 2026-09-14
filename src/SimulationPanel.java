import javax.swing.JPanel;
import java.awt.Graphics;

public class SimulationPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawString("Warehouse Delivery Robot Simulation", 50, 50);
        
        // Robot representation
        g.fillRect(200, 200, 40, 40);
        g.drawString("Robot", 200, 190);

        // Package representation
        g.drawRect(400, 250, 30, 30);
        g.drawString("Package", 390, 240);

        // Charging station
        g.drawRect(100, 450, 100, 50);
        g.drawString("Charging", 120, 480);

        // Delivery station
        g.drawRect(600, 450, 100, 50);
        g.drawString("Delivery", 620, 480);
    }
}