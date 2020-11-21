package org.firstinspires.ftc.teamcode.subsystems.commands;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.WobbleSystem;

public class Com_PickUp extends CommandBase {
    private final WobbleSystem wobblySystem;
    private final ElapsedTime time;

    public Com_PickUp(WobbleSystem subby, ElapsedTime elapsedTime){
        wobblySystem = subby;
        time = elapsedTime;
        addRequirements(subby);
    }
    @Override
    public void initialize(){
        time.reset();
    }
    @Override
    public void execute(){
        wobblySystem.spinMeRightRoundBaby();
    }
    @Override
    public void end(boolean interrupted){
        wobblySystem.motorUp();
        if(time.seconds() >= 10)
            wobblySystem.motorStop();
    }
    @Override
    public boolean isFinished(){
        return time.seconds() >= 1;
    }
}