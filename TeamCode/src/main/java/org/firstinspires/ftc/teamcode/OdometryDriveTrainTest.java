package org.firstinspires.ftc.teamcode;


import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

@TeleOp(name = "Odometry Drive Test", group = "Iterative OpMode")
public class OdometryDriveTrainTest extends OpMode {
    private final ElapsedTime runtime = new ElapsedTime();

    private DcMotorEx backLeftMotor;
    private DcMotorEx backRightMotor;
    private DcMotorEx frontLeftMotor;
    private DcMotorEx frontRightMotor;
    private GoBildaPinpointDriver pinpointOdo;

    @Override
    public void init() {
        backLeftMotor = hardwareMap.get(DcMotorEx.class, "backLeftMotor");
        backRightMotor = hardwareMap.get(DcMotorEx.class, "backRightMotor");
        frontLeftMotor = hardwareMap.get(DcMotorEx.class, "frontLeftMotor");
        frontRightMotor = hardwareMap.get(DcMotorEx.class, "frontRightMotor");

        // motors on the left need to be set to reverse, motors on the right need to be set to forward
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        DcMotorEx[] drivetrain = {backLeftMotor, backRightMotor, frontLeftMotor, frontRightMotor};
        for (DcMotorEx motor : drivetrain) {
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

        // adding Pinpoint
        pinpointOdo = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");

        // measured 10/20/2026
        pinpointOdo.setOffsets(-1.5, -6.5, DistanceUnit.INCH);
        pinpointOdo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        // might have to change pod directions
        pinpointOdo.setEncoderDirections(
                GoBildaPinpointDriver.EncoderDirection.FORWARD,
                GoBildaPinpointDriver.EncoderDirection.FORWARD
        );

        // calibrate IMU
        pinpointOdo.resetPosAndIMU();

        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    @Override
    public void start() {
        runtime.reset();
    }

    @Override
    public void loop() {
        // update pinpointOdo and get position
        pinpointOdo.update();
        Pose2D pos = pinpointOdo.getPosition();

        double drive = -gamepad1.left_stick_x; // WHY DOES THIS WORK
        double turn = gamepad1.right_stick_x;
        double strafe = gamepad1.left_stick_y;

        double frontLeftPower = Range.clip(drive + strafe + turn, -1, 1);
        double frontRightPower = Range.clip(drive - strafe - turn, -1, 1);
        double backLeftPower = Range.clip(drive - strafe + turn, -1, 1);
        double backRightPower = Range.clip(drive + strafe - turn, -1, 1);

        frontLeftMotor.setPower(frontLeftPower);
        backLeftMotor.setPower(backLeftPower);
        frontRightMotor.setPower(frontRightPower);
        backRightMotor.setPower(backRightPower);

        // telemetry for pinpointOdo
        telemetry.addData("X (in)", "%.2f", pos.getX(DistanceUnit.INCH));
        telemetry.addData("Y (in)", "%.2f", pos.getY(DistanceUnit.INCH));
        telemetry.addData("Heading (deg)", "%.1f", pos.getHeading(AngleUnit.DEGREES));
        telemetry.addData("Pinpoint Status", pinpointOdo.getDeviceStatus());
        telemetry.update();
    }
}
