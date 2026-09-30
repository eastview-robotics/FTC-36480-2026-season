package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanisims.Intake;
import org.firstinspires.ftc.teamcode.mechanisims.MecanumDrive;

@TeleOp(name="Cool Teleop", group = "Concept")
public class MecanumFieldOrientatedOpMode extends OpMode {
    // drive train
    MecanumDrive drive = new MecanumDrive();
    double forward, strafe, rotate;
    // intake
    Intake intake = new Intake();
    private boolean intakeOn = false;
    private boolean isReversed = false;
    private boolean lastGamepad1B = false;
    private boolean lastGamepad1A = false;



    @Override
    public void init() {
        drive.init(hardwareMap);
        initIntake();
    }

    @Override
    public void loop() {
        // the y stick is inverted on the gamepad...I KNOW F😈
        forward = -gamepad1.left_stick_y;
        strafe = gamepad1.left_stick_x;
        rotate = gamepad1.right_stick_x;

        drive.driveFieldRelative(forward,strafe,rotate);
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

}
