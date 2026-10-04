package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.mechanisims.MecanumDrive;

@TeleOp(name="Cool Teleop", group = "Concept")
public class MecanumFieldOrientatedOpMode extends OpMode {
    MecanumDrive drive = new MecanumDrive();

    private static final double DEADZONE = 0.05;

    @Override
    public void init() {
        drive.init(hardwareMap);
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
    }

    private double applyDeadzone(double value) {
        return Math.abs(value) > DEADZONE ? value : 0.0;
    }
}