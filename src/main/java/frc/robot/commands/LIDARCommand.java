/*
Through some testing, I have determined that using Digital I/O
for this LIDAR is hopeless. The I/O speed on the RoboRIO is
too slow for the LIDAR's output. May we have better luck with
using I2C.

  -Kevin

*/

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
  private Timer timer2 = new Timer();
  private double timee = 0;
  private int[] output = new int[100];
  int index=0;
  public double signal;

  public double distanceIn;
  public double distanceCm;

  double wavestart = 0;

  public DigitalInput DOI;

  public float Timer;
  public boolean previousState=false;
  private boolean risingEdgeDetect=false;
  private boolean fallingEdgeDetect=false;
  
  public LIDARCommand() {
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize(){ 
    timer=new Timer();
    
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



    


    //All code below is from earlier attempts at using the digital port to read the PWM signal
    // if(DOI.get()){ blake's code
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

    // if(DOI.get()&&!previousState){ //Kevin's attempt (failed)
    //   timer.start();
    //   previousState=DOI.get(); 
    // }else if(!DOI.get()&&previousState){
    //   distanceCm = timer.get()*100000;
    //   SmartDashboard.putNumber("Time", timer.get());
    //   SmartDashboard.putNumber("distanceCM", distanceCm);
    //   timer.reset();
    //   previousState=DOI.get();
    // }
    

    /*
    if(!DOI.get()&&!fallingEdgeDetect){
      risingEdgeDetect=true;
      output[index]=1;
    }
    if(DOI.get()&&risingEdgeDetect){
      timer.start();
      fallingEdgeDetect=true;
      risingEdgeDetect=false;
      // System.out.println(2);
    }
    if(DOI.get()&&fallingEdgeDetect){
      output[index]=2;
    }
    if(!DOI.get()&&fallingEdgeDetect){
      SmartDashboard.putNumber("Time", timer.get());
      distanceCm = timer.get()*100000;
      SmartDashboard.putNumber("distanceCM", distanceCm);
      timer.reset();
      fallingEdgeDetect=false;
      risingEdgeDetect=true;
      output[index]=3;
    }
    index++;
    if(index==100){
      for(int i=0;i<100;i++){
        try{
          System.out.println(output[index]+" ");
        }catch(IndexOutOfBoundsException e){

        }
      }
      System.out.println("");
      index=0;
    }
    */
    System.out.println(DOI.get());

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
