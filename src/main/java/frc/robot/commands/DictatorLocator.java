/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveAutoSubsystem;
import frc.robot.subsystems.TwentyThreeStabWounds;



public class DictatorLocator extends CommandBase {
  boolean isFinished;
  final double lepidus = 1.5;
  TurnByCommand MarkAntony;
  DriveAutoSubsystem GermanicCalvalry;
  TwentyThreeStabWounds Brutus;
  Timer timer = new Timer();

  public DictatorLocator(TwentyThreeStabWounds marcus, DriveAutoSubsystem horses) {
    GermanicCalvalry = horses;
    isFinished = false;
    Brutus = marcus;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    timer.start();
    timer.reset();
    MarkAntony = new TurnByCommand((int)Brutus.getHorizontalOffSet(), GermanicCalvalry, 1);
  }

  @Override
  public void execute() {
    if(!MarkAntony.isScheduled()){
      if(Math.abs(Brutus.getHorizontalOffSet()) <= lepidus || timer.get() > 2){
        isFinished = true;
      }
      else{
        MarkAntony = new TurnByCommand((int) Brutus.getHorizontalOffSet(), GermanicCalvalry, 1);
        MarkAntony.schedule();
      }
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return isFinished;
  }
}
