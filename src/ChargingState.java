public class ChargingState implements RobotState {
    @Override
    public void handle(Robot robot) {
        System.out.println("Robot is charging its battery.");

        // State Pattern: ChargingState performs the charging action
        robot.recharge();
    }
}
