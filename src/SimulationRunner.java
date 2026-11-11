public class SimulationRunner implements Runnable {
    private final Robot robot;
    private final Warehouse warehouse;

    // Allows the simulation to be stopped safely
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
                boolean shouldStop = false;

                // Protect shared robot data using the warehouse lock
                synchronized (warehouse.getSimulationLock()) {

                    // Stop if the robot has no battery
                    if (robot.getBattery() <= 0) {
                        shouldStop = true;
                    } else {

                        // Reverse direction at the right boundary
                        if (robot.getX() >= 700) {
                            direction = -1;
                        }

                        // Reverse direction at the left boundary
                        if (robot.getX() <= 50) {
                            direction = 1;
                        }

                        int previousX = robot.getX();

                        // Strategy Pattern: move using selected strategy
                        robot.performMove(warehouse, direction);

                        // Stop if robot cannot complete the movement
                        if (previousX == robot.getX()) {
                            System.out.println(
                                "Robot stopped because it cannot move."
                            );
                            shouldStop = true;
                        }
                    }
                }

                if (shouldStop) {
                    break;
                }

                // Sleep outside the synchronized block
                // so Swing can repaint without being blocked
                Thread.sleep(100);

            } catch (InvalidPositionException e) {
                // Reverse direction if the robot reaches the grid boundary
                direction = -direction;

            } catch (InterruptedException e) {
                // Restore interrupt status and terminate the thread
                Thread.currentThread().interrupt();
                break;
            }
        }

        running = false;
    }

    // Stop the simulation when requested
    public void stop() {
        running = false;
    }
}