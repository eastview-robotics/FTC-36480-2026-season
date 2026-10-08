package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanisims.Intake;
import org.firstinspires.ftc.teamcode.mechanisims.MecanumDrive;

@TeleOp(name="Cool Teleop", group = "Concept")
public class MecanumFieldOrientatedOpMode extends OpMode {
    MecanumDrive drive = new MecanumDrive();
    // intake
    Intake intake = new Intake();
    private boolean intakeOn = false;
    private boolean isReversed = false;
    private boolean lastGamepad1B = false;
    private boolean lastGamepad1A = false;
    private static final double DEADZONE = 0.05;

    @Override
    public void init() {
        drive.init(hardwareMap);
        initIntake();
    }

    @Override
    public void start() {
        drive.resetYaw(); // Reset heading when driver presses Play
    }

    @Override
    public void loop() {
        double forward = applyDeadzone(-gamepad1.left_stick_y);
        double strafe  = applyDeadzone(gamepad1.left_stick_x);
        double rotate  = applyDeadzone(gamepad1.right_stick_x);

        drive.driveFieldRelative(forward, strafe, rotate);
        loopIntake();
    }

    private void initIntake() {
        intake.init(hardwareMap);
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
    }
    private double applyDeadzone(double value) {
        return Math.abs(value) > DEADZONE ? value : 0.0;
    }
}