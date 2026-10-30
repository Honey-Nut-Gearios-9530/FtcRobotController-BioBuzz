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

    @Override
    public void init() {
        backLeftMotor = hardwareMap.get(DcMotorEx.class, "backLeftMotor");
        backRightMotor = hardwareMap.get(DcMotorEx.class, "backRightMotor");
        frontLeftMotor = hardwareMap.get(DcMotorEx.class, "frontLeftMotor");
        frontRightMotor = hardwareMap.get(DcMotorEx.class, "frontRightMotor");

        // motors on the left need to be set to reverse, motors on the right need to be set to forward
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        DcMotorEx[] drivetrain = new DcMotorEx[]{backLeftMotor, backRightMotor, frontLeftMotor, frontRightMotor};
        for (DcMotorEx motor : drivetrain) {
            //motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            //motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            // this is where i started editting!!!
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            // this is where i stoped editing!!!
        }

        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    @Override
    public void start() {
        runtime.reset();
    }
    //this initialises a varyabal that is used so the stuff in the if statment runs only once.
    int a = 0;
    int b = 0;
    @Override
    public void loop() {
        double strafe = gamepad1.left_stick_x; // WHY DOES THIS WORK
        double turn = gamepad1.right_stick_x;
        double drive = -gamepad1.left_stick_y;
        //where i started editing!!! //this code has not been tested yet
        //this is a test for a new breaking system
        //i changed it to use the double varyabls ensted of getign the gampads psotion
        if(strafe ==0 && turn == 0 && drive==0){
            //this makes the thing run only once untuill the robot controler sends a command
            if(a==0) {
                b=0;
                int backLeft = backLeftMotor.getCurrentPosition();
                int backRight = backRightMotor.getCurrentPosition();
                int frontLeft = frontLeftMotor.getCurrentPosition();
                int frontRight = frontRightMotor.getCurrentPosition();
                //sets the target position to what it has lerned from the motors
                backLeftMotor.setTargetPosition(backLeft);
                backRightMotor.setTargetPosition(backRight);
                frontLeftMotor.setTargetPosition(frontLeft);
                frontRightMotor.setTargetPosition(frontRight);
                //this makes shure the loop only runs once.
                a+=1;
                //this is temporary telemetry to test the bad motor remove this after test.
                telemetry.addData("motor thing form get curnet position", backRightMotor.getCurrentPosition());//deleet this later
                telemetry.update();//deleet this later
            }
            //moves the motor to the target position
            backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            //this sets the power the moters will run at with the new beaking system
            //hopfully this will be removed when the loop ends
            backLeftMotor.setPower(0.25);
            backRightMotor.setPower(0.25);
            frontLeftMotor.setPower(0.25);
            frontRightMotor.setPower(0.25);

//            telemetry.addData("motor thing form get curnet position", backLeft);
//            telemetry.update();
        } else { //this is the code that moves the robot if you enter an input on the controler
            a = 0;
            //this makes shure this hapens only once so the moters dont repetadly turn on and off
            if (b==0) {
                backLeftMotor.setPower(0);
                backRightMotor.setPower(0);
                frontLeftMotor.setPower(0);
                frontRightMotor.setPower(0);
                backLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                backRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                frontLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                frontRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                b+=1;
            }
            // where i finished editing!!!

            telemetry.addData("-gamepad1.left_stick_y", -gamepad1.left_stick_y);
            telemetry.addData("-gamepad1.right_stick_y", -gamepad1.right_stick_y);
            telemetry.addData("gamepad1.left_stick_x", gamepad1.left_stick_x);
            telemetry.addData("gamepad1.right_stick_x", gamepad1.right_stick_x);

            double frontLeftPower = Range.clip(strafe + drive + turn, -1, 1);
            double frontRightPower = Range.clip(strafe - drive - turn, -1, 1);
            double backLeftPower = Range.clip(strafe - drive + turn, -1, 1);
            double backRightPower = Range.clip(strafe + drive - turn, -1, 1);

            telemetry.addLine();
            telemetry.addData("frontLeftPower", frontLeftPower);
            telemetry.addData("backLeftPower", backLeftPower);
            telemetry.addData("frontRightPower", frontRightPower);
            telemetry.addData("backRightPower", backRightPower);
//            telemetry.update();
            //this makes the robot move.
            frontLeftMotor.setPower(frontLeftPower);
            backLeftMotor.setPower(backLeftPower);
            frontRightMotor.setPower(frontRightPower);
            backRightMotor.setPower(backRightPower);
            // this is to check the power and see why the motor is not moving
            // deleet this section after the problem is found.
            double frontLeftPower2 = frontLeftMotor.getPower();
            double frontRightPower2 = frontRightMotor.getPower();
            double backLeftPower2 = backLeftMotor.getPower();
            double backRightPower2 = backRightMotor.getPower();

            telemetry.addData("frontLeftPower2", frontLeftPower2);
            telemetry.addData("backLeftPower2", backLeftPower2);
            telemetry.addData("frontRightPower2", frontRightPower2);
            telemetry.addData("backRightPower2", backRightPower2);
            telemetry.update();
        }
    }
}
