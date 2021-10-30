package frc.robot.commands.tank;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.tank.TankSubsystem;

public class ReadInstructionsCommand extends SequentialCommandGroup {

    private TankSubsystem tankSubsystem;
    private String filepath;

    /**
     * Command that reads movement instructions from a text file and executes them
     * sequentially.
     * 
     * @param tankSubsystem tank subsystem
     * @param filepath      filepath to instructions file
     */
    public ReadInstructionsCommand(TankSubsystem tankSubsystem, String filepath) {

        this.tankSubsystem = tankSubsystem;
        this.filepath = filepath;

        addRequirements(tankSubsystem);
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(Filesystem.getDeployDirectory(), filepath)));

            String str;
            // read the file line-by-line
            while ((str = br.readLine()) != null) {
                System.out.println(str);
                String[] arr = str.split(" ");

                String command = arr[0]; // can be forward/backward/turn
                String value = arr[1]; // can be 2m/2s/left/right/180

                // if command is a drive command
                if (command.equals("forward") || command.equals("backward")) {
                    boolean forward = command.equals("forward");

                    char movementUnit = value.charAt(value.length() - 1);
                    double movementNumber = Integer.parseInt(value.substring(0, value.length() - 1));

                    // if in seconds, add new timed drive command
                    if (movementUnit == 's') {
                        this.addCommands(new TimedDriveCommand(tankSubsystem, forward, movementNumber));
                    }
                    // if in meters, add new distance drive command
                    else if (movementUnit == 'm') {
                        this.addCommands(new DistanceDriveCommand(tankSubsystem, forward, movementNumber));
                    }

                }
                // if command is a turn command
                else if (command.equals("turn")) {

                    double degrees = 0;

                    if (value.equals("left")) {
                        degrees = -90;
                    } else if (value.equals("right")) {
                        degrees = 90;
                    } else {
                        degrees = Integer.parseInt(value);
                    }

                    this.addCommands(new RotateAngleCommand(tankSubsystem, degrees));
                }
            }

        } catch (IOException e) {
            System.out.println("Reading movement config file failed.");
            e.printStackTrace();
        }
    }

}
