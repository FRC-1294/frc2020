/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
//import frc.robot.Robot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.I2C;
//import edu.wpi.first.wpilibj.PIDSourceType;

import java.nio.ByteBuffer;

import edu.wpi.first.hal.I2CJNI;
//import edu.wpi.first.wpilibj2.command.PIDCommand;

import edu.wpi.first.wpilibj.I2C.Port;


public class LIDARCommand extends CommandBase {
  /**
   * Creates a new LIDARSubsystem.
   */


  private static final byte k_deviceAddress = 0x62;

	private final byte m_port;

	private final ByteBuffer m_buffer = ByteBuffer.allocateDirect(2);
    
  public I2C i2c;
  
  public double offset;
  
  public LIDARCommand(Port port) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_port = (byte) port.value;
		I2CJNI.i2CInitialize(m_port);
  }

  public void startMeasuring() {
		writeRegister(0x04, 0x08 | 32); // default plus bit 5
		writeRegister(0x11, 0xff);
		writeRegister(0x00, 0x04);
	}

	public void stopMeasuring() {
		writeRegister(0x11, 0x00);
	}

	public int getDistance() {
		return readShort(0x8f);
	}

	private int writeRegister(int address, int value) {
		m_buffer.put(0, (byte) address);
		m_buffer.put(1, (byte) value);

		return I2CJNI.i2CWrite(m_port, k_deviceAddress, m_buffer, (byte) 2);
	}

	private short readShort(int address) {
		m_buffer.put(0, (byte) address);
		I2CJNI.i2CWrite(m_port, k_deviceAddress, m_buffer, (byte) 1);
		I2CJNI.i2CRead(m_port, k_deviceAddress, m_buffer, (byte) 2);
		return m_buffer.getShort(0);
	}

	// @Override
	// public void setPIDSourceType(PIDSourceType pidSource) {
	// 	if (pidSource != PIDSourceType.kDisplacement) {
	// 		throw new IllegalArgumentException("Only displacement is supported");
	// 	}
	// }

	// @Override
	// public PIDSourceType getPIDSourceType() {
	// 	return PIDSourceType.kDisplacement;
	// }

	//@Override
	public double pidGet() {
		return getDistance();
	}


  // Called when the command is initially scheduled.
  @Override
  public void initialize(){ 

    SmartDashboard.setDefaultNumber("Offset", -4);
    
    startMeasuring(); //Starts Measuring (Maybe, I have no idea)

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {


    offset = SmartDashboard.getNumber("Offset", -4);
    SmartDashboard.putNumber("Distance", getDistance()/2.54f + offset);
  
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

