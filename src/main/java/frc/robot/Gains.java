/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

public class Gains {
    public final double kP;
    public final double kI;
    public final double kD;
    public final double kIz;
    public final double kFF;
    public final double kMinOutput;
    public final double kMaxOutput;

    public Gains(double kP, double kI, double kD, double kIz, double kFF, double kMinOutput, double kMaxOutput){
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kIz = kIz;
        this.kFF = kFF;
        this.kMinOutput = kMinOutput;
        this.kMaxOutput = kMaxOutput;
    }
}
