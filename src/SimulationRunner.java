public class SimulationRunner implements Runnable {
    // 1. Fields and Constructor
    private final Robot robot;
    private final Warehouse warehouse;
    private final Package targetPackage;
    private final DeliveryStation deliveryStation;

    private volatile boolean running = true;

    public SimulationRunner(Robot robot,
        Warehouse warehouse, Package targetPackage,
        DeliveryStation deliveryStation
    ) {

        this.robot = robot;
        this.warehouse = warehouse;
        this.targetPackage = targetPackage;
        this.deliveryStation = deliveryStation;
    }

    // 2. Main Simulation Loop
    @Override
    public void run() {

        while (running && !Thread.currentThread().isInterrupted()) {

            try {
                boolean finished = false;

                synchronized (warehouse.getSimulationLock()) {

                    int previousX = robot.getX();
                    int previousY = robot.getY();

                    // Stage 1: Travel to the package
                    if (!targetPackage.isPickedUp()) {

                        if (hasReached(targetPackage)) {

                            boolean success = warehouse.pickUpPackage(
                                robot, targetPackage
                            );

                            if (success) {
                                System.out.println(
                                    "Robot collected package "
                                    + targetPackage.getId()
                                );
                            } else {
                                System.out.println("Pickup failed.");
                                finished = true;
                            }

                        } else {
                            moveTowards(
                                targetPackage.getX(),
                                targetPackage.getY()
                            );
                        }

                    // Stage 2: Travel to delivery station
                    } else if (!targetPackage.isDelivered()) {

                        if (hasReached(deliveryStation)) {

                            boolean success = warehouse.deliverPackage(
                                robot,
                                targetPackage,
                                deliveryStation
                            );

                            if (success) {
                                System.out.println(
                                    "Delivery task completed!"
                                );
                            } else {
                                System.out.println("Delivery failed.");
                            }

                            finished = true;

                        } else {
                            moveTowards(
                                deliveryStation.getX(),
                                deliveryStation.getY()
                            );
                        }

                    } else {
                        // Package has already been delivered
                        finished = true;
                    }

                    // Detect unsuccessful movement
                    if (!finished
                            && robot.getX() == previousX
                            && robot.getY() == previousY
                            && !hasReached(targetPackage)
                            && !hasReached(deliveryStation)) {

                        System.out.println(
                            "Robot cannot continue moving."
                        );

                        finished = true;
                    }
                }

                if (finished) {
                    break;
                }

                // Sleep outside the lock
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

    // 3. Navigation
    // Check whether robot reached an entity
    private boolean hasReached(Entity target) {
        return robot.getX() == target.getX()
            && robot.getY() == target.getY();
    }

    // Move robot towards the specified target
    private void moveTowards(int targetX, int targetY) {

        int currentX = robot.getX();
        int currentY = robot.getY();

        // Move horizontally first
        if (currentX != targetX) {

            int difference = targetX - currentX;

            int step = Math.min(5, Math.abs(difference));
            int direction = difference > 0 ? 1 : -1;

            // Use Strategy Pattern for full 5-unit steps
            if (step == 5) {
                robot.performMove(warehouse, direction);
            } else {
                warehouse.moveRobot(robot, direction * step, 0);
            }

        // Then move vertically
        } else if (currentY != targetY) {

            int difference = targetY - currentY;

            int step = Math.min(5, Math.abs(difference));
            int direction = difference > 0 ? 1 : -1;

            warehouse.moveRobot(robot, 0, direction * step);
        }
    }

    // 4. Stop Simulation
    public void stop() {
        running = false;
    }
}
