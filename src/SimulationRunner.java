
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

                int previousX;
                int currentX;

                synchronized (warehouse.getSimulationLock()) {
                    previousX = robot.getX();

                    // Move using the Strategy pattern
                    robot.performMove(warehouse, direction);

                    currentX = robot.getX();
                }

                // Stop if the robot cannot move
                if (previousX == currentX) {
                    System.out.println(
                        "Robot stopped because movement could not be completed."
                    );
                    break;
                }

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
