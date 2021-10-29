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
        System.out.println((forward ? "forward " : "backward ") + meters + "m");
    }

    @Override
    public void execute() {
        System.out.println("executing");
        // set motor power
        if (forward) {
            tankSubsystem.setDrivePowers(TankSubsystem.MOTOR_POWER, TankSubsystem.MOTOR_POWER);
        } else {
            // negative power for backward movement
            tankSubsystem.setDrivePowers(-TankSubsystem.MOTOR_POWER, -TankSubsystem.MOTOR_POWER);
        }
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    public void end() {
        tankSubsystem.setDrivePowers(0, 0);
    }
}