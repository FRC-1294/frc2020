/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DigitalOutput;
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

  public DigitalOutput DOI;

  public float Timer;
  
  public LIDARCommand() {
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    timer = new Timer();
    timer.reset();
    timer.start();
    timer.get();
    //Set DOI to be the one on the roborio, DONE
    /*
    set value of the LidAR to 0 (false)
    Set it to 1 (true)

    */

    DOI = new DigitalOutput(0);
    DOI.set(false);
    Timer = 0;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    Timer += 1;

    if(DOI.isPulsing()){
      
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


