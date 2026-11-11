package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanisims.Indexer;
import org.firstinspires.ftc.teamcode.mechanisims.Intake;
import org.firstinspires.ftc.teamcode.mechanisims.MecanumDrive;
import org.firstinspires.ftc.teamcode.mechanisims.Outtake;

@TeleOp(name="Cool Teleop", group = "Concept")
public class MecanumFieldOrientatedOpMode extends OpMode {
    MecanumDrive drive = new MecanumDrive();
    // intake
    private boolean lastGamepad1X = false;
    Intake intake = new Intake();
    private boolean intakeOn = true;
    private boolean isReversed = true;
    private boolean lastGamepad1B = false;
    private boolean lastGamepad1A = false;

    Outtake outtake = new Outtake();
    private boolean outtakeOn = true;
    private boolean lastGamepad2B = false;

    Indexer indexer = new Indexer();

    private boolean lastGamepad2L_Bumper = false;
    private boolean indexerUp = false;
    private static final double DEADZONE = 0.04;



    @Override
    public void init() {
        drive.init(hardwareMap);
        initIntake();
        initOuttake();
        initIndexer();
    }

    @Override
    public void start() {
        //drive.resetYaw(); // Reset heading when driver presses Play
    }

    @Override
    public void loop() {
        double forward = applyDeadzone(-gamepad1.left_stick_y);
        double strafe  = applyDeadzone(gamepad1.left_stick_x);
        double rotate  = applyDeadzone(gamepad1.right_stick_x);

        drive.driveFieldRelative(forward, strafe, rotate);
        loopIntake();
        loopOuttake();
        loopIndexer();

        if (gamepad1.x && !lastGamepad1X) {
            drive.resetYaw();
        }
        lastGamepad1X = gamepad1.x;

        telemetry.addData("Raw Right Stick X", gamepad1.right_stick_x);
        telemetry.addData("Deadzoned Rotate", rotate);
        telemetry.addData("Deadzoned Forward", forward);
        telemetry.addData("Deadzoned Strafe", strafe);
    }

    private void initIntake() {
        intake.init(hardwareMap);
    }

    private void initOuttake() {
        outtake.init(hardwareMap);
    }
    private void initIndexer() {
        indexer.init(hardwareMap);
    }

    private void loopIntake() {
        //  Toggle ON / OFF with Button B
        if (gamepad1.b && !lastGamepad1B) {
            intakeOn = !intakeOn;
        }
        lastGamepad1B = gamepad1.b;

        // Toggle Direction (FORWARD / REVERSE) with Button A
        if (gamepad1.a && !lastGamepad1A) {
            isReversed = !isReversed;
        }
        lastGamepad1A = gamepad1.a;

        // Apply Power
        if (intakeOn) {
            double INTAKE_MULTIPLIER = isReversed ? -1 : 1;
            intake.setIntakeDirection(INTAKE_MULTIPLIER);
        } else {
            intake.setIntakeDirection(0.0);
        }
        telemetry.addData("Motor Revs", intake.getIntakeRevs());
        telemetry.addData("Intake On", intakeOn);
        telemetry.addData("Intake Reversed", isReversed);
    }

    private void loopOuttake() {
        //  Toggle ON / OFF with Button B
        if (gamepad2.b && !lastGamepad2B) {
            outtakeOn = !outtakeOn;
        }
        lastGamepad2B = gamepad2.b;

        // Apply Power
        if (outtakeOn) {
            double OUTTAKE_MULTIPLIER = 1;
            outtake.setOuttakeDirection(OUTTAKE_MULTIPLIER);
        } else {
            outtake.setOuttakeDirection(0.0);
        }
        telemetry.addData("Motor Revs", outtake.getOuttakeRevs());
        telemetry.addData("Outtake On", outtake);
        telemetry.addData("Outtake Reversed", isReversed);
    }

    private void loopIndexer() {
        if (gamepad2.left_bumper && !lastGamepad2L_Bumper) {
            indexerUp = !indexerUp;
        }
        lastGamepad2L_Bumper = gamepad2.left_bumper;

        if (indexerUp) {
            indexer.setServoPos(0.0);
        } else {
            indexer.setServoPos(1.0);
        }
    }
    private double applyDeadzone(double value) {
        return Math.abs(value) > DEADZONE ? value : 0.0;
    }
}