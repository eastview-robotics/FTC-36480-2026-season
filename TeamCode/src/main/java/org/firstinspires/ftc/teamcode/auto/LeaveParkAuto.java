package org.firstinspires.ftc.teamcode.auto;

import com.pedropathing.api.Paths;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.utils.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.mechanisims.Intake;
import org.firstinspires.ftc.teamcode.pedro.Constants;

@Autonomous(name="Leave Park Auto", group = "Concept")
public class LeaveParkAuto extends OpMode {
    private Follower follower;
    Intake intake = new Intake();
    private Timer pathTimer, opModeTimer;

    public enum PathState {
        DRIVE_START_POS_LEAVE_POS,
        WAIT_LEAVE_POS,
        WAIT_PARK_POS,
        DONE
    }
    private PathState pathState;

    private final Pose startPose = new Pose(56, 8, Math.toRadians(90));
    private final Pose leavePose = new Pose(52.8, 37.7, Math.toRadians(90));
    // For pure rotations, hold position at leavePose while updating heading target,
    // or include the turn directly into the park path.
    private final Pose parkPose  = new Pose(56, 8, Math.toRadians(-90));

    private Path driveStartPosLeavePos;
    private Path driveParkPos;

    public void buildPaths() {
        driveStartPosLeavePos = Paths.line(startPose, leavePose).constant(startPose.heading());
        driveParkPos          = Paths.line(leavePose, parkPose).linear(leavePose.heading(), parkPose.heading());
    }

    public void statePathUpdate() {
        switch (pathState) {
            case DRIVE_START_POS_LEAVE_POS:
                follower.follow(driveStartPosLeavePos);
                setPathState(PathState.WAIT_LEAVE_POS);
                intake.setIntakeDirection(1);
                break;

            case WAIT_LEAVE_POS:
                if (!follower.isBusy()) {
                    telemetry.addLine("Path 1 Done -> Moving to Park");
                    follower.follow(driveParkPos);
                    intake.setIntakeDirection(-1);
                    setPathState(PathState.WAIT_PARK_POS);
                }
                break;

            case WAIT_PARK_POS:
                if (!follower.isBusy()) {
                    telemetry.addLine("Park Path Done");
                    intake.setIntakeDirection(0);
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