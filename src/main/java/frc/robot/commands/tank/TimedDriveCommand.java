package frc.robot.commands.tank;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.tank.TankSubsystem;

public class TimedDriveCommand extends CommandBase {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })

    private final TankSubsystem tankSubsystem;
    private double seconds;
    private boolean forward;

    private Timer timer;
    private double initial_time;

    public TimedDriveCommand(TankSubsystem tankSubsystem, boolean forward, double seconds) {
        this.tankSubsystem = tankSubsystem;
        this.seconds = seconds;
        this.forward = forward;

        this.timer = new Timer();

        addRequirements(tankSubsystem);
    }

    // Called once.
    @Override
    public void initialize() {
        // start the timer
        timer.start();
        initial_time = timer.get();

        System.out.println((forward ? "forward " : "backward ") + seconds + "s");
    }

    // Called every time the scheduler runs while the command is scheduled.
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
        // true if current time exceeds goal time
        return timer.get() >= (initial_time + seconds);
    }

    public void end() {
        System.out.println("timed drive ends");

        timer.stop();
        tankSubsystem.setDrivePowers(0, 0);
    }

}