package org.firstinspires.ftc.teamcode.auto;

import com.pedropathing.api.Paths;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.utils.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.mechanisims.Indexer;
import org.firstinspires.ftc.teamcode.mechanisims.Intake;
import org.firstinspires.ftc.teamcode.mechanisims.Outtake;
import org.firstinspires.ftc.teamcode.pedro.Constants;

@Autonomous(name="Leave Park Auto Hive", group = "Concept")
public class LeaveParkAutoHive extends OpMode {
    private Follower follower;
    Intake intake = new Intake();

    Outtake outtake = new Outtake();

    Indexer indexer = new Indexer();
    private ElapsedTime myTimer = new ElapsedTime();

    private Timer pathTimer, opModeTimer;

    public enum PathState {
        SHOOT_START_POS,
        DRIVE_START_POS_LEAVE_POS,
        WAIT_LEAVE_POS,
        WAIT_PARK_POS,
        DONE
    }
    private PathState pathState;

    private final Pose startPose = new Pose(57, 11, Math.toRadians(90));
    private final Pose leavePose = new Pose(57, 118, Math.toRadians(90));
    // For pure rotations, hold position at leavePose while updating heading target,
    // or include the turn directly into the park path.
    private final Pose parkPose  = new Pose(11, 118, Math.toRadians(90));

    private Path driveStartPosLeavePos;
    private Path driveParkPos;

    public void buildPaths() {
        driveStartPosLeavePos = Paths.line(startPose, leavePose).constant(startPose.heading());
        driveParkPos          = Paths.line(leavePose, parkPose).linear(leavePose.heading(), parkPose.heading());
    }

    public void statePathUpdate() {
        switch (pathState) {
            case SHOOT_START_POS:
                outtake.setOuttakeDirection(1);
                indexer.setServoPos(0.32);
                if (myTimer.milliseconds() > 5000) {
                    // do something after 3 seconds
                    setPathState(PathState.DRIVE_START_POS_LEAVE_POS);
                }
                myTimer = new ElapsedTime();
                break;

            case DRIVE_START_POS_LEAVE_POS:
                outtake.setOuttakeDirection(0.0);
                indexer.setServoPos(0.0);
                follower.follow(driveStartPosLeavePos);
                setPathState(PathState.WAIT_LEAVE_POS);
                break;

            case WAIT_LEAVE_POS:
                if (!follower.isBusy()) {
                    telemetry.addLine("Path 1 Done -> Moving to Park");
                    follower.follow(driveParkPos);
                    setPathState(PathState.WAIT_PARK_POS);
                }
                break;

            case WAIT_PARK_POS:
                if (!follower.isBusy()) {
                    telemetry.addLine("Park Path Done");
                    setPathState(PathState.DONE);
                }
                break;

            case DONE:
                telemetry.addLine("Auto Complete!");
                break;

            default:
                telemetry.addLine("No State Commanded");
                break;
        }
    }

    public void setPathState(PathState newState) {
        pathState = newState;
        pathTimer.reset();
    }

    @Override
    public void init() {
        pathState = PathState.DRIVE_START_POS_LEAVE_POS;
        pathTimer = new Timer();
        opModeTimer = new Timer();

        follower = Constants.create(hardwareMap);
        follower.setPose(startPose);

        intake.init(hardwareMap);

        buildPaths();
    }

    @Override
    public void start() {
        opModeTimer.reset();
        setPathState(PathState.DRIVE_START_POS_LEAVE_POS);
    }

    @Override
    public void loop() {
        follower.update();
        statePathUpdate();

        telemetry.addData("Path State", pathState.toString());
        telemetry.addData("Path Timer", pathTimer.seconds());
        telemetry.addData("Heading", Math.toDegrees(follower.pose().heading()));
        telemetry.addData("X", follower.pose().x());
        telemetry.addData("Y", follower.pose().y());
    }
}