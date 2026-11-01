public class IdleState implements RobotState {
    @Override
    public void handle(Robot robot) {
        System.out.println("Robot is idle, waiting for a task.");
    }
}