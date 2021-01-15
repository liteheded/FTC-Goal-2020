package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.SimpleServo;
import com.arcrobotics.ftclib.hardware.motors.Motor;

public class WobbleSubsystem extends SubsystemBase {

    private Motor arm;
    private SimpleServo grabber;
    private boolean grabbing = false;

    public WobbleSubsystem(Motor arm, SimpleServo grabber){
            this.arm = arm;
            this.grabber = grabber;

            this.arm.setRunMode(Motor.RunMode.PositionControl);
            this.arm.resetEncoder();
    }

    //TODO: Set proper positions for grabber
    public void openGrabber(){
        grabbing = false;
        grabber.setPosition(0);
    }
    public void closeGrabber(){
        grabbing = true;
        grabber.setPosition(180);
    }
    public boolean isGrabbing(){
        return grabbing;
    }

    public Motor getMotor(){
        return arm;
    }
    public void stopMotor(){
        arm.stopMotor();
    }
    public void armUp(){
        arm.set(0.5);
    }
    public void armDown(){
        arm.set(0.3);
    }

}
