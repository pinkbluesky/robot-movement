package frc.robot.commands.tank;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.tank.TankSubsystem;

public class RotateAngleCommand extends CommandBase {

    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })

    private final TankSubsystem tankSubsystem;
    private double angle;

    /**
     * Rotates robot in-place by given angle.
     * 
     * @param tankSubsystem
     * @param angle         the angle to rotate, from 360 to -360 degrees, CW to
     *                      CCW.
     */
    public RotateAngleCommand(TankSubsystem tankSubsystem, double angle) {
        this.tankSubsystem = tankSubsystem;
        this.angle = angle;

        // if angle is out of bounds, set to zero
        if (angle > 360 || angle < -360) {
            angle = 0;
        }

        addRequirements(tankSubsystem);
    }

    // Called once.
    @Override
    public void initialize() {
        System.out.println("rotate " + angle);
        tankSubsystem.resetQuadraturePosition();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {

        tankSubsystem.setDrivePowers(0, angle / 360);
    }

    @Override
    public boolean isFinished() {
        // true if robot has rotated to or past the desired angle
        return tankSubsystem.getRotationDisplacement() >= angle;
    }

    public void end() {
        tankSubsystem.setDrivePowers(0, 0);
    }

}
