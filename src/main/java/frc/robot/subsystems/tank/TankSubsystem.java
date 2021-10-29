// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.tank;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class TankSubsystem extends SubsystemBase {

  public static final double MOTOR_POWER = 5;
  public static final double TIME_TO_MOVE_1M_WITH_1POWER = 3; // in seconds

  private final TalonGroup left;
  private final TalonGroup right;

  private double leftPower;
  private double rightPower;

  // TODO default drivetankcommand
  public TankSubsystem(int fLeftId, int bLeftId, int fRightId, int bRightId) {
    super();

    left = new TalonGroup(new TalonSRX[] { new TalonSRX(fLeftId), new TalonSRX(bLeftId) });
    left.setNeutralMode(NeutralMode.Brake);

    // TODO determine if this really needs to be inverted
    right = new TalonGroup(new TalonSRX[] { new TalonSRX(fRightId), new TalonSRX(bRightId) }, true);
    right.setNeutralMode(NeutralMode.Brake);

    // initialize motor power
    leftPower = 0;
    rightPower = 0;
  }

  @Override
  public void periodic() {
    left.set(ControlMode.PercentOutput, leftPower);
    right.set(ControlMode.PercentOutput, rightPower);
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }

  public void setDrivePowers(double leftPower, double rightPower) {
    this.leftPower = leftPower;
    this.rightPower = rightPower;
  }

  /**
   * Drive the system with the given power scales.
   * 
   * @param yScale       Scale in the forward/backward direction, from 1 to -1.
   * @param angularScale Scale in the rotational direction, from 1 to -1,
   *                     clockwise to counterclockwise.
   */
  public void setDrivePowersScaled(double yScale, double angularScale) {
    setDrivePowersScaled(yScale, angularScale, true);
  }

  public void setDrivePowersScaled(double yScale, double angularScale, boolean squareInput) {
    // Square the input if needed for finer control
    if (squareInput) {
      yScale = Math.copySign(yScale * yScale, yScale);
      angularScale = Math.copySign(angularScale * angularScale, angularScale);
    }

    double leftPower = yScale + angularScale;
    double rightPower = yScale - angularScale;

    double scale = 1.0 / Math.max(Math.abs(leftPower), Math.abs(rightPower));

    leftPower *= scale;
    rightPower *= scale;

    left.set(ControlMode.PercentOutput, leftPower);
    right.set(ControlMode.PercentOutput, rightPower);
  }

  public double calculateTime(double meters) {
    return meters * TIME_TO_MOVE_1M_WITH_1POWER * MOTOR_POWER;
  }

  public void followPathCommand() {

  }
}