package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "Drivetrain Test", group = "Iterative OpMode")
public class DriveTrainTest extends OpMode {
    private final ElapsedTime runtime = new ElapsedTime();

    private DcMotorEx backLeftMotor;
    private DcMotorEx backRightMotor;
    private DcMotorEx frontLeftMotor;
    private DcMotorEx frontRightMotor;
    private DcMotorEx[] drivetrain;
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

        drivetrain = new DcMotorEx[] {backLeftMotor, backRightMotor, frontLeftMotor, frontRightMotor};
        for (DcMotorEx motor : drivetrain) {
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    @Override
    public void start() {
        runtime.reset();
    }

    @Override
    public void loop() {
        double drive = gamepad1.left_stick_x; // WHY DOES THIS WORK
        double turn = gamepad1.right_stick_x;
        double strafe = -gamepad1.left_stick_y;

        telemetry.addData("-gamepad1.left_stick_y", -gamepad1.left_stick_y);
        telemetry.addData("-gamepad1.right_stick_y", -gamepad1.right_stick_y);
        telemetry.addData("gamepad1.left_stick_x", gamepad1.left_stick_x);
        telemetry.addData("gamepad1.right_stick_x", gamepad1.right_stick_x);
        //this is the hadbreak
        if ((drive == 0) && (turn == 0) && (strafe == 0)) {
            for (DcMotorEx motor : drivetrain) {
                motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                motor.setPower(0);
            }
        } else { // this lets it drive if you are driving the bot
            for (DcMotorEx motor : drivetrain) {
                motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            }
            double frontLeftPower = Range.clip(drive + strafe + turn, -1, 1);
            double frontRightPower = Range.clip(drive - strafe - turn, -1, 1);
            double backLeftPower = Range.clip(drive - strafe + turn, -1, 1);
            double backRightPower = Range.clip(drive + strafe - turn, -1, 1);

            telemetry.addLine();
            telemetry.addData("frontLeftPower", frontLeftPower);
            telemetry.addData("backLeftPower", backLeftPower);
            telemetry.addData("frontRightPower", frontRightPower);
            telemetry.addData("backRightPower", backRightPower);
            telemetry.update();

            frontLeftMotor.setPower(frontLeftPower);
            backLeftMotor.setPower(backLeftPower);
            frontRightMotor.setPower(frontRightPower);
            backRightMotor.setPower(backRightPower);
        }
    }
}
