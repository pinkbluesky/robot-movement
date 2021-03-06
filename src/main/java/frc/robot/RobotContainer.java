// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.tank.DistanceDriveCommand;
import frc.robot.commands.tank.DriveTankCommand;
import frc.robot.commands.tank.ReadInstructionsCommand;
import frc.robot.commands.tank.TimedDriveCommand;
import frc.robot.commands.elevator.ElevatorUpCommand;
import frc.robot.commands.elevator.ElevatorDownCommand;
import frc.robot.commands.elevator.ElevatorStopCommand;
import frc.robot.subsystems.elevator.ElevatorSubsystem;
import frc.robot.subsystems.tank.TankSubsystem;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // Path of config file, relative to the deploy folder
  private static final String CONFIG_PATH = "config.txt";
  public static final String TANK_MOVEMENT_CONFIG_PATH = "movement.txt";

  // Subsystems
  private final TankSubsystem tankSubsystem;
  private final ElevatorSubsystem elevatorSubsystem;

  // Controllers
  private XboxController controlXbox = new XboxController(0);

  // Commands
  private final DriveTankCommand tankCommand;
  private final ReadInstructionsCommand readInstructionsCommand;

  // Helper field variables
  Properties config;

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   * 
   * @throws IOException
   */
  public RobotContainer() {
    // Load the config file
    this.config = new Properties();

    try {
      FileInputStream stream = new FileInputStream(new File(Filesystem.getDeployDirectory(), CONFIG_PATH));
      config.load(stream);
    } catch (IOException ie) {
      System.out.println("config file not found");
    }

    // Instantiate subsystems
    tankSubsystem = new TankSubsystem(Integer.parseInt(config.getProperty("fLeft")),
        Integer.parseInt(config.getProperty("mLeft")), Integer.parseInt(config.getProperty("bLeft")),
        Integer.parseInt(config.getProperty("fRight")), Integer.parseInt(config.getProperty("mRight")),
        Integer.parseInt(config.getProperty("bRight")));

    elevatorSubsystem = new ElevatorSubsystem(Integer.parseInt(config.getProperty("elevator_master")),
        Integer.parseInt(config.getProperty("elevator_follower")));

    // Instantiate commands
    tankCommand = new DriveTankCommand(tankSubsystem, 0, 0);
    // tankSubsystem.setDefaultCommand(new DriveTankCommand(tankSubsystem,
    // controlXbox.getY(), controlXbox.getRawAxis(1)));
    elevatorSubsystem.setDefaultCommand(new ElevatorStopCommand(elevatorSubsystem));

    // drive tank using movement file
    readInstructionsCommand = new ReadInstructionsCommand(tankSubsystem, TANK_MOVEMENT_CONFIG_PATH);
    CommandScheduler.getInstance().schedule(readInstructionsCommand);

    // put tank command group on the SmartDashboard for testing
    SmartDashboard.putData("Move Tank", readInstructionsCommand);

    // Configure the button bindings
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by instantiating a {@link GenericHID} or one of its subclasses
   * ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then
   * passing it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    controllerBindings();
  }

  private void controllerBindings() {
    /**
     * tankSubsystem.setDefaultCommand(new RunCommand(() -> {
     * tankSubsystem.setDrivePowers(); // TODO }, tankSubsystem));
     */

    // when A is pressed, move elevator Up
    new JoystickButton(controlXbox, 1).whenPressed(new ElevatorUpCommand(elevatorSubsystem));

    // when B is pressed, move elevator down
    new JoystickButton(controlXbox, 2).whenPressed(new ElevatorDownCommand(elevatorSubsystem));

  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return tankCommand;
  }
}