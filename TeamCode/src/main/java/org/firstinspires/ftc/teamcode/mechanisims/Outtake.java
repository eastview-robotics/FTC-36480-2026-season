package org.firstinspires.ftc.teamcode.mechanisims;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Outtake {
    private DcMotor OuttakeMotor;
    private double ticksPerRev; // Revolution
    //set up the motor
    public void init(HardwareMap hwMap) {
        OuttakeMotor = hwMap.get(DcMotor.class, "outtake_motor");
        OuttakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        OuttakeMotor.setDirection(DcMotor.Direction.FORWARD);
        OuttakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        ticksPerRev = OuttakeMotor.getMotorType().getTicksPerRev();
        if (ticksPerRev == 0) {
            ticksPerRev = 28;
        }
    }
    //change the direction of the intake
    //1 = normal direction, -1 = reverse, 0 = stop
    public void setOuttakeDirection(double multi) {
        double speed = 1.0;
        OuttakeMotor.setPower(speed*multi);
    }
    // if this number is counting down instead of up then somebody don't know how 2 wire 😂
    public double getOuttakeRevs(){
        // if we are using gears and stuff we GOTTA multiply ts with the gear ratio
        if (ticksPerRev == 0) return 0.0;
        return OuttakeMotor.getCurrentPosition() / ticksPerRev;

    }
}
