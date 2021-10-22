// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.elevator;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;


import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;


/** Add your docs here. */
public class StorageSubsystem extends SubsystemBase {
   // feel free to rename and reorganize these variables
    /* IR sensors */
    private AnalogInput ir1, ir2, ir3, ir4;
    /* Talon SRX motor controller. */
    private TalonSRX motor;


    private int motor_id;
    private int ir1_id;
    private int ir0_id;
    private int ir2_id;
    private int ir3_id;

    /* if the analog input value is greater than this number, then there is a lemon in front of the sensor */
    private static final int LEMON_THRESHOLD = 1200;

  public StorageSubsystem() {
  
    CommandScheduler.getInstance().registerSubsystem(this);

    this.motor = new TalonSRX(motor_id);
    this.ir1 = new AnalogInput(ir0_id);
   this.ir2 = new AnalogInput(ir1_id);
    this.ir3 = new AnalogInput(ir2_id);
    this.ir4 = new AnalogInput(ir3_id);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation

     // take input from sensors and make motors move here

        //Never want it to go past top
        if (true){
            motor.set(ControlMode.PercentOutput, 0);
        }
        //
        else if (true){
            motor.set(ControlMode.PercentOutput, .5);
        } 
      }
  }

