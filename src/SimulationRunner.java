public class SimulationRunner implements Runnable {

    private final Robot robot;
    private final Warehouse warehouse;
    private final Package targetPackage;
    private volatile boolean running = true;

    public SimulationRunner(Robot robot, Warehouse warehouse,
                            Package targetPackage) {
        this.robot = robot;
        this.warehouse = warehouse;
        this.targetPackage = targetPackage;
    }

    @Override
    public void run() {

        while (running && !Thread.currentThread().isInterrupted()) {
            try {
                boolean finished = false;

                synchronized (warehouse.getSimulationLock()) {

                    int robotX = robot.getX();
                    int robotY = robot.getY();

                    int targetX = targetPackage.getX();
                    int targetY = targetPackage.getY();

                    // Stop if the package has already been collected
                    if (targetPackage.isPickedUp()) {
                        finished = true;

                    // First move horizontally towards the package
                    } else if (robotX != targetX) {

                        int direction = targetX > robotX ? 1 : -1;

                        // Use the existing Strategy pattern
                        robot.performMove(warehouse, direction);

                    // Then move vertically towards the package
                    } else if (robotY != targetY) {

                        int stepY = targetY > robotY
                                ? Math.min(5, targetY - robotY)
                                : -Math.min(5, robotY - targetY);

                        warehouse.moveRobot(robot, 0, stepY);

                    // The robot has reached the package
                    } else {

                        boolean success = warehouse.pickUpPackage(
                                robot, targetPackage
                        );

                        if (success) {
                            System.out.println(
                                "Package collected successfully!"
                            );
                        } else {
                            System.out.println(
                                "Package could not be collected."
                            );
                        }

                        finished = true;
                    }

                    // Detect insufficient battery or failed movement
                    if (!finished
                            && robot.getX() == robotX
                            && robot.getY() == robotY) {

                        System.out.println(
                            "Robot cannot continue moving."
                        );
                        finished = true;
                    }
                }

                if (finished) {
                    break;
                }

                // Pause between movements
                Thread.sleep(100);

            } catch (InvalidPositionException e) {
                System.out.println(e.getMessage());
                break;

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