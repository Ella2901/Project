public class FastMovement implements MovementStrategy {
    @Override
    public void move(Robot robot, Warehouse warehouse, int direction) {
        warehouse.moveRobot(robot, 5 * direction);
    }
}
