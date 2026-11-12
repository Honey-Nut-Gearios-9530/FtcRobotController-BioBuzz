package org.firstinspires.ftc.teamcode;

//import static com.pedropathing.follower.ManualDrive.*;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.pedro.Constants;

import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.ManualDrive;

@TeleOp(name = "Drivetrain Test", group = "Iterative OpMode")
public class PedroPathingTeleOpTest extends OpMode {
    private final ElapsedTime runtime = new ElapsedTime();

    private DcMotorEx backLeftMotor;
    private DcMotorEx backRightMotor;
    private DcMotorEx frontLeftMotor;
    private DcMotorEx frontRightMotor;

    private Follower follower;

    @Override
    public void init() {
        follower = Constants.create(hardwareMap);

//        backLeftMotor = hardwareMap.get(DcMotorEx.class, "backLeftMotor");
//        backRightMotor = hardwareMap.get(DcMotorEx.class, "backRightMotor");
//        frontLeftMotor = hardwareMap.get(DcMotorEx.class, "frontLeftMotor");
//        frontRightMotor = hardwareMap.get(DcMotorEx.class, "frontRightMotor");
//
//        // motors on the left need to be set to reverse, motors on the right need to be set to forward
//        backLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
//        backRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
//        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
//        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
//
//        DcMotorEx[] drivetrain = new DcMotorEx[]{backLeftMotor, backRightMotor, frontLeftMotor, frontRightMotor};
//        for (DcMotorEx motor : drivetrain) {
//            //this is the old breaking system.
//            //motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//            //motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        }
//
//        telemetry.addData("Status", "Initialized");
//        telemetry.update();
    }

    @Override
    public void start() {
        runtime.reset();
    }

    @Override
    public void loop() {
        double strafe = gamepad1.left_stick_x; // WHY DOES THIS WORK
        double turn = gamepad1.right_stick_x;
        double drive = -gamepad1.left_stick_y;

//        follower.manual(drive,strafe,turn);
        ManualDrive.driveOrHold(
                follower,
                -gamepad1.left_stick_y,
                gamepad1.left_stick_x,
                gamepad1.right_stick_x
        );
        follower.update();
    }
}
