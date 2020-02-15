/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LIDARCommand extends CommandBase {
  /**
   * Creates a new LIDARSubsystem.
   */
    
  public Timer timer;
  public double signal;

  public double distanceIn;
  public double distanceCm;

  double wavestart = 0;

  public DigitalInput DOI;

  public float Timer;
  public boolean previousState;
  
  public LIDARCommand() {
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize(){ 

    
    //Set DOI to be the one on the roborio, DONE
    /*
    set value of the LidAR to 0 (false)
    Set it to 1 (true)

    */

    DOI = new DigitalInput(0);
    //DOI.set(false);
    //Timer = 0;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {


    // if(DOI.get()){
    //   distanceCm += 1;
    // }
    // else{

    //   distanceCm *= 2.85714;
    //   if(distanceCm != 0){
    //     SmartDashboard.putBoolean("Value", DOI.get());
    //     SmartDashboard.putNumber("distanceCM", distanceCm);
    //     System.out.print(distanceCm);
    //   }
      
    //   distanceCm = 0;
      
    // }
    if(DOI.get()&&!previousState){
      timer.start();
    }else if(!DOI.get()&&previousState){
      SmartDashboard.putNumber("Time", timer.get());
      timer.reset();
    }
    
    

  }

  
  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }

}

/*
Timer += 1;

    if(DOI.get()){
      
      if(wavestart == 0){
        wavestart = timer.get();
      }
      
    }
    

    else{
      signal = (timer.get() - wavestart);
      timer.reset();
      wavestart = 0;
      distanceCm = signal*100000;
      distanceIn = distanceCm * 0.03937007874016;


    }

    SmartDashboard.putBoolean("Value", DOI.get());
    SmartDashboard.putNumber("distanceCM", distanceCm);
    SmartDashboard.putNumber("distanceIn", distanceIn);
    */
