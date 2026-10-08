package org.firstinspires.ftc.teamcode.mechanisims;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {
    private DcMotor intakeMotor;
    private double ticksPerRev; // Revolution
    //set up the motor
    public void init(HardwareMap hwMap) {
        intakeMotor = hwMap.get(DcMotor.class, "intake_motor");
        intakeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intakeMotor.setDirection(DcMotor.Direction.FORWARD);
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        ticksPerRev = intakeMotor.getMotorType().getTicksPerRev();
        if (ticksPerRev == 0) {
            ticksPerRev = 1.0;
        }
    }
    //change the direction of the intake
    //1 = normal direction, -1 = reverse, 0 = stop
    public void setIntakeDirection(double multi) {
        double speed = 1.0;
        intakeMotor.setPower(speed*multi);
    }
    // if this number is counting down instead of up then somebody don't know how 2 wire 😂
    public double getIntakeRevs(){
        // if we are using gears and stuff we GOTTA multiply ts with the gear ratio
        if (ticksPerRev == 0) return 0.0;
        return intakeMotor.getCurrentPosition() / ticksPerRev;

    }
}
