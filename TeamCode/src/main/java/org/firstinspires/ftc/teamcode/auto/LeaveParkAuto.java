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
        DRIVE_SPIN,
        DRIVE_PARK_POS,
        DONE
    }
    PathState pathState;

    private final Pose startPose = new Pose(56, 8, Math.toRadians(90));
    private final Pose leavePose = new Pose(52.8, 37.7, Math.toRadians(90));

    private final Pose spinPose  = new Pose(52.8, 37.7, Math.toRadians(-90));


    private Path driveStartPosLeavePos;
    private Path driveSpinPos;
    private Path driveParkPos;

    public void buildPaths() {
        // Put in coordinates for starting pose > ending pose
        driveStartPosLeavePos = Paths.line(startPose, leavePose);
        driveSpinPos          = Paths.line(leavePose, spinPose);
        driveParkPos          = Paths.line(spinPose, startPose);
    }

    public void statePathUpdate() {
        switch (pathState) {
            case DRIVE_START_POS_LEAVE_POS:
                follower.follow(driveStartPosLeavePos);
                intake.setIntakeDirection(1);
                setPathState(PathState.DRIVE_SPIN);
                break;

            case DRIVE_SPIN:
                if (!follower.isBusy()) {
                    telemetry.addLine("wow yay (Path 1 Done)");
                    follower.follow(driveSpinPos);
                    intake.setIntakeDirection(-1);
                    setPathState(PathState.DRIVE_PARK_POS);
                }
                break;

            case DRIVE_PARK_POS:
                if (!follower.isBusy()) {
                    telemetry.addLine("ohhh shoot we are spinning too (Path 2 done)");
                    follower.follow(driveParkPos);
                    intake.setIntakeDirection(1);
                    setPathState(PathState.DONE);
                }
                break;

            case DONE:
                if (!follower.isBusy()) {
                    intake.setIntakeDirection(0);
                    telemetry.addLine("Auto Complete!");
                }
                break;

            default:
                telemetry.addLine("No State Commanded (yet)");
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
        opModeTimer.reset();
        follower = Constants.create(hardwareMap);
        //TODO add any other init mechanisms like the intake and stuff

        buildPaths();
        follower.setPose(startPose );
    }

    public void start() {
        opModeTimer.reset();
        setPathState(pathState);
    }
    @Override
    public void loop() {
        follower.update();
        statePathUpdate();

        telemetry.addData("Path State", pathState.toString());
        telemetry.addData("Path Timer", pathTimer.seconds());
        telemetry.addData("heading", follower.pose().heading());
        telemetry.addData("x", follower.pose().x());
        telemetry.addData("y", follower.pose().y());
    }
}

