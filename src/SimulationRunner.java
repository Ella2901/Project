public class SimulationRunner implements Runnable {
    // 1. Fields and Constructor
    private final Robot robot;
    private final Warehouse warehouse;
    private final Package targetPackage;
    private final DeliveryStation deliveryStation;
    private final ChargingStation chargingStation;

    private volatile boolean running = true;

    private boolean chargingTrip = false;
    private boolean chargedOnce = false;

    private static final int STEP_SIZE = 5;
    private static final int BATTERY_RESERVE = 50;

    public SimulationRunner(
            Robot robot,
            Warehouse warehouse,
            Package targetPackage,
            DeliveryStation deliveryStation,
            ChargingStation chargingStation) {

        this.robot = robot;
        this.warehouse = warehouse;
        this.targetPackage = targetPackage;
        this.deliveryStation = deliveryStation;
        this.chargingStation = chargingStation;
    }

    // 2. Main Simulation Loop
    @Override
    public void run() {

        synchronized (warehouse.getSimulationLock()) {
            robot.setState(new DeliveringState());
            robot.performAction();
        }

        while (running && !Thread.currentThread().isInterrupted()) {

            try {
                boolean finished = false;
                boolean attemptedMove = false;
                boolean startedCharging = false;

                synchronized (warehouse.getSimulationLock()) {

                    int previousX = robot.getX();
                    int previousY = robot.getY();

                    // Stage 1: Complete charging after arriving
                    if (robot.getState() instanceof ChargingState) {

                        if (warehouse.chargeRobot(
                                robot, chargingStation)) {

                            chargedOnce = true;
                            chargingTrip = false;

                            robot.setState(new DeliveringState());
                            robot.performAction();

                        } else {
                            System.out.println("Charging failed.");
                            finished = true;
                        }

                    // Stage 2: Collect the package
                    } else if (!targetPackage.isPickedUp()) {

                        if (hasReached(targetPackage)) {

                            if (!warehouse.pickUpPackage(
                                    robot, targetPackage)) {
                                System.out.println("Pickup failed.");
                                finished = true;
                            }

                        } else {
                            moveTowards(
                                targetPackage.getX(),
                                targetPackage.getY()
                            );
                            attemptedMove = true;
                        }

                    // Stage 3: Deliver the package
                    } else if (!targetPackage.isDelivered()) {

                        int distanceToDelivery = distanceTo(
                            deliveryStation.getX(),
                            deliveryStation.getY()
                        );

                        // Decide whether charging is necessary
                        if (!chargingTrip && !chargedOnce
                                && robot.getBattery()
                                   < distanceToDelivery + BATTERY_RESERVE) {

                            int distanceToCharger = distanceTo(
                                chargingStation.getX(),
                                chargingStation.getY()
                            );

                            if (robot.getBattery()
                                    < distanceToCharger) {

                                System.out.println(
                                    "Not enough battery to reach charger."
                                );
                                finished = true;

                            } else {
                                chargingTrip = true;
                                System.out.println(
                                    "Battery is low. Heading to charger."
                                );
                            }
                        }

                        if (!finished) {

                            if (chargingTrip) {

                                if (hasReached(chargingStation)) {

                                    // Enter ChargingState only.
                                    // The actual recharge happens on the
                                    // next iteration after Warehouse
                                    // validates the robot's location.
                                    robot.setState(new ChargingState());

                                    startedCharging = true;

                                } else {
                                    moveTowards(
                                        chargingStation.getX(),
                                        chargingStation.getY()
                                    );
                                    attemptedMove = true;
                                }

                            } else if (hasReached(deliveryStation)) {

                                if (warehouse.deliverPackage(
                                        robot,
                                        targetPackage,
                                        deliveryStation)) {

                                    System.out.println(
                                        "Delivery task completed!"
                                    );

                                    robot.setState(new IdleState());
                                    robot.performAction();

                                } else {
                                    System.out.println("Delivery failed.");
                                }

                                finished = true;

                            } else {
                                moveTowards(
                                    deliveryStation.getX(),
                                    deliveryStation.getY()
                                );
                                attemptedMove = true;
                            }
                        }

                    } else {
                        finished = true;
                    }

                    // Detect unsuccessful movement
                    if (attemptedMove
                            && robot.getX() == previousX
                            && robot.getY() == previousY) {

                        System.out.println(
                            "Robot cannot continue moving."
                        );
                        finished = true;
                    }
                }

                if (finished) {
                    break;
                }

                // Pause outside the simulation lock
                Thread.sleep(startedCharging ? 750 : 100);

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

    // 3. Navigation Helper Methods
    private boolean hasReached(Entity target) {
        return robot.getX() == target.getX()
            && robot.getY() == target.getY();
    }

    // Manhattan distance between robot and destination
    private int distanceTo(int targetX, int targetY) {

        return Math.abs(targetX - robot.getX())
             + Math.abs(targetY - robot.getY());
    }

    // Move horizontally first, then vertically
    private void moveTowards(int targetX, int targetY) {

        int currentX = robot.getX();
        int currentY = robot.getY();

        if (currentX != targetX) {

            int difference = targetX - currentX;

            int step = Math.min(
                STEP_SIZE, Math.abs(difference)
            );

            int direction = difference > 0 ? 1 : -1;

            // Use the FastMovement Strategy for 5-unit steps
            if (step == STEP_SIZE) {
                robot.performMove(warehouse, direction);
            } else {
                warehouse.moveRobot(
                    robot, direction * step, 0
                );
            }

        } else if (currentY != targetY) {

            int difference = targetY - currentY;

            int step = Math.min(
                STEP_SIZE, Math.abs(difference)
            );

            int direction = difference > 0 ? 1 : -1;

            warehouse.moveRobot(
                robot, 0, direction * step
            );
        }
    }

    // 4. Stop Simulation
    public void stop() {
        running = false;
    }
}