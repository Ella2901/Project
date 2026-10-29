public class NormalMovement implements MovementStrategy {
    @Override
    public void move(Robot robot, Warehouse warehouse, int direction) {
        warehouse.moveRobot(robot, 2 * direction);
    }
}

