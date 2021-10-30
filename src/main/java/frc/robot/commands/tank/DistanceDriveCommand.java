package frc.robot.commands.tank;

import frc.robot.subsystems.tank.TankSubsystem;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class DistanceDriveCommand extends CommandBase {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })

    private final TankSubsystem tankSubsystem;

    private boolean forward;
    private double meters;

    public DistanceDriveCommand(TankSubsystem tankSubsystem, boolean forward, double meters) {
        this.tankSubsystem = tankSubsystem;
        this.forward = forward;
        this.meters = meters;

        addRequirements(tankSubsystem);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void initialize() {
        tankSubsystem.resetQuadraturePosition();

        System.out.println((forward ? "forward " : "backward ") + meters + "m");
    }

    @Override
    public void execute() {
        // set motor power
        if (forward) {
            tankSubsystem.setDrivePowers(TankSubsystem.MOTOR_POWER, 0);
        } else {
            // negative power for backward movement
            tankSubsystem.setDrivePowers(-TankSubsystem.MOTOR_POWER, 0);
        }
    }

    @Override
    public boolean isFinished() {
        // finished if both the left and right side have travelled enough distance
        return (tankSubsystem.getLeftDisplacement() >= meters) && (tankSubsystem.getRightDisplacement() >= meters);
    }

    public void end() {
        tankSubsystem.setDrivePowers(0, 0);
    }
}