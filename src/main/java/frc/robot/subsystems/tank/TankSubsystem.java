// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.tank;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class TankSubsystem extends SubsystemBase {

  public static final double MOTOR_POWER = 0.5;

  private TalonSRX fLeft;
  private TalonSRX mLeft;
  private TalonSRX bLeft;
  private TalonSRX fRight;
  private TalonSRX mRight;
  private TalonSRX bRight;

  private double leftPower;
  private double rightPower;

  // TODO default drivetankcommand
  public TankSubsystem(int fLeftId, int mLeftId, int bLeftId, int fRightId, int mRightId, int bRightId) {

    // instatiate motors
    fLeft = new TalonSRX(fLeftId);
    mLeft = new TalonSRX(mLeftId);
    bLeft = new TalonSRX(bLeftId);

    fRight = new TalonSRX(fRightId);
    mRight = new TalonSRX(mRightId);
    bRight = new TalonSRX(bRightId);

    // set up motor following
    mLeft.follow(fLeft);
    bLeft.follow(fLeft);
    mRight.follow(fRight);
    bRight.follow(fRight);

    mLeft.setInverted(InvertType.FollowMaster);
    bLeft.setInverted(InvertType.FollowMaster);
    mRight.setInverted(InvertType.FollowMaster);
    bRight.setInverted(InvertType.FollowMaster);

    // set motors to neutral mode
    fLeft.setNeutralMode(NeutralMode.Brake);
    mLeft.setNeutralMode(NeutralMode.Brake);
    bLeft.setNeutralMode(NeutralMode.Brake);
    fRight.setNeutralMode(NeutralMode.Brake);
    mRight.setNeutralMode(NeutralMode.Brake);
    bRight.setNeutralMode(NeutralMode.Brake);

    // initialize motor power
    leftPower = 0;
    rightPower = 0;
  }

  @Override
  public void periodic() {
    fLeft.set(ControlMode.PercentOutput, leftPower);
    fRight.set(ControlMode.PercentOutput, rightPower);
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }

  /**
   * Resets the quadrature position of the left and right motors.
   */
  public void resetQuadraturePosition() {
    fLeft.getSensorCollection().setQuadraturePosition(0, 0);
    fRight.getSensorCollection().setQuadraturePosition(0, 0);
  }

  /**
   * Returns the quadrature position of the left motors.
   * 
   * @return the number of ticks
   */
  public int getLeftPosition() {
    return fLeft.getSensorCollection().getQuadraturePosition();
  }

  /**
   * Returns the quadrature position of the right motors.
   * 
   * @return the number of ticks
   */
  public int getRightPosition() {
    return fRight.getSensorCollection().getQuadraturePosition();
  }

  // TODO check if left/right displacement and rotation displacement still work
  // when robot is both rotating and turning
  /**
   * Returns the position of the left motors, in meters.
   * 
   * @return the position (meters)
   */
  public double getLeftDisplacement() {
    return getLeftPosition() * Constants.DISTANCE_PER_TICK;
  }

  /**
   * Returns the position of the right motors, in meters.
   * 
   * @return the position (meters)
   */
  public double getRightDisplacement() {
    return getRightPosition() * Constants.DISTANCE_PER_TICK;
  }

  /**
   * Returns the rotational displacement of the robot, in degrees. Use for
   * in-place rotation.
   * 
   * @return rotational displacement (degrees)
   */
  public double getRotationDisplacement() {
    return getLeftPosition() / Constants.TICKS_PER_360_ROTATION;
  }

  /**
   * Drive the system with the given power scales.
   * 
   * @param yScale       Scale in the forward/backward direction, from 1 to -1.
   * @param angularScale Scale in the rotational direction, from 1 to -1,
   *                     clockwise to counterclockwise.
   */
  public void setDrivePowers(double yScale, double angularScale) {
    setDrivePowers(yScale, angularScale, true);
  }

  public void setDrivePowers(double yScale, double angularScale, boolean squareInput) {
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

    this.leftPower = leftPower;
    this.rightPower = rightPower;
  }

  public void followPathCommand() {

  }
}