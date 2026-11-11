
public class SimulationRunner implements Runnable {
    private final Robot robot;
    private final Warehouse warehouse;

    private volatile boolean running = true;

    public SimulationRunner(Robot robot, Warehouse warehouse) {
        this.robot = robot;
        this.warehouse = warehouse;
    }

    @Override
    public void run() {
        int direction = 1;

        while (running && !Thread.currentThread().isInterrupted()) {
            try {
                if (robot.getBattery() <= 0) {
                    break;
                }

                if (robot.getX() >= 700) {
                    direction = -1;
                }

                if (robot.getX() <= 50) {
                    direction = 1;
                }

                // Continue using the Strategy pattern
                robot.performMove(warehouse, direction);

                Thread.sleep(100);

            } catch (InvalidPositionException e) {
                direction = -direction;

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        running = false;
    }

    public void stop() {
        running = false;
    }
}
