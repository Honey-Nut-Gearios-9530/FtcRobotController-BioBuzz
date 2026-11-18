package org.firstinspires.ftc.teamcode;

//import static com.pedropathing.follower.ManualDrive.*;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
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

@TeleOp(name = "PedroPathing TeleOp Test", group = "Iterative OpMode")
public class PedroPathingTeleOpTest extends OpMode {
    Follower follower;
    PoseFactory p = PoseFactory.degrees();
    Pose startPose = p.of(0,0,0);
//    private final ElapsedTime runtime = new ElapsedTime();
//
//    private DcMotorEx backLeftMotor;
//    private DcMotorEx backRightMotor;
//    private DcMotorEx frontLeftMotor;
//    private DcMotorEx frontRightMotor;


    @Override
    public void init() {
        follower = Constants.create(hardwareMap);
        follower.setPose(startPose);// this should be changed to take the last position of the robot form otonomus mode once that is implemented
        follower.update();
    }

//    @Override
//    public void start() {
//        runtime.reset();
//    }
int a = 0;
    @Override
    public void loop() {
        if (a==0){
            ManualDrive.driveOrHold(
                    follower,
                    -gamepad1.left_stick_y,
                    -gamepad1.left_stick_x,
                    -gamepad1.right_stick_x
            );
            follower.update();
            if (gamepad1.a){
                a ++;
            }
        }
        if (a == 1) {
            DrivePowers powers = ManualDrive.fieldCentric(
                    -gamepad1.left_stick_y,
                    -gamepad1.left_stick_x,
                    -gamepad1.right_stick_x,
                    follower.pose().heading()
            );
            ManualDrive.driveOrHold(follower, powers);
            follower.update();
            telemetry.addData("heading", follower.pose().heading());
            if (gamepad1.x){
                follower.setPose(startPose);// this is temporary i nead to change this later.
                follower.update(startPose.heading());
            }
            if (gamepad1.b){
                a --;
            }
        }
    }
}
