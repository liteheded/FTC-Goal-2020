package org.firstinspires.ftc.teamcode.commands.groups;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelDeadlineGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.commands.Com_PickUp;
import org.firstinspires.ftc.teamcode.commands.Com_PutDown;
import org.firstinspires.ftc.teamcode.commands.RapidFireCommand;
import org.firstinspires.ftc.teamcode.commands.rr.TrajectoryFollowerCommand;
import org.firstinspires.ftc.teamcode.commands.rr.TurnCommand;
import org.firstinspires.ftc.teamcode.subsystems.MecanumDriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.WobbleSubsystem;

@Config
public class OneRing extends SequentialCommandGroup {

    public static double xBox = 24.0, yBox = -36.0;
    public static double shootPosX = -44.0, shootPosY = 22.0;
    public static double secWobblePosX = -20.0, secWobblePosY = 0.0;
    public static double wobbleXTwo = 16.0, wobbleYTwo = 0.0;
    public static double boxTwoX = 16.0, boxTwoY = 0.0;
    public static double finalX = -10.0, finalY = -5.0;

    private Pose2d startPose = new Pose2d(-63.0, -40.0, Math.toRadians(180.0));

    public OneRing(MecanumDriveSubsystem drive, WobbleSubsystem wobbleSystem, ShooterSubsystem shooter){
        drive.setPoseEstimate(startPose);
        Trajectory traj0 = drive.trajectoryBuilder(startPose)
                .strafeLeft(14.5)
                .build();

        Trajectory traj1 = drive.trajectoryBuilder(traj0.end())
                .back(1.0)
                .splineToConstantHeading(new Vector2d(1.0, -60.0), 0.0)
                .splineToConstantHeading(new Vector2d(xBox, yBox), 0.0)
                .build();

        Vector2d shootPose = traj1.end().vec().plus(new Vector2d(shootPosX, shootPosY));

        Trajectory traj2 = drive.trajectoryBuilder(traj1.end(), true)
                .lineToConstantHeading(shootPose)
                .build();

//        Vector2d secondWobble = traj2.end().vec().plus(new Vector2d(secWobblePosX, secWobblePosY));

        Trajectory traj3 = drive.trajectoryBuilder(traj2.end(), 0.0)
                .splineToLinearHeading(new Pose2d(-37.8,-18.5, 0.0), Math.toRadians(-90.0))
                .build();

        Trajectory traj4 = drive.trajectoryBuilder(traj3.end(), 0)
                .splineTo(traj3.end().vec().plus(new Vector2d(boxTwoX, boxTwoY)), 0.0)
                .splineToLinearHeading(traj1.end().plus(new Pose2d(finalX, finalY, Math.toRadians(180.0))), 0.0)
                .build();

        addCommands(
                new ParallelDeadlineGroup(
                        new TrajectoryFollowerCommand(drive, traj0),
                        new Com_PutDown(wobbleSystem)
                ),
                new TrajectoryFollowerCommand(drive, traj1),
                new InstantCommand(wobbleSystem::openGrabber, wobbleSystem),
                new WaitCommand(800),
                new Com_PickUp(wobbleSystem),
                new TrajectoryFollowerCommand(drive, traj2),
                new RapidFireCommand(shooter),
                new InstantCommand(shooter::stop, shooter),
                new ParallelDeadlineGroup(
                        new TrajectoryFollowerCommand(drive, traj3),
                        new Com_PutDown(wobbleSystem)
                ),
                new InstantCommand(wobbleSystem::closeGrabber, wobbleSystem),
                new WaitCommand(1000),
                new TrajectoryFollowerCommand(drive, traj4),
                new TurnCommand(drive, Math.toRadians(180)),
                new InstantCommand(wobbleSystem::openGrabber, wobbleSystem),
                new WaitCommand(500),
                new Com_PickUp(wobbleSystem)
        );
    }
}
