package org.firstinspires.ftc.teamcode;

import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;
import static com.pedropathing.ivy.pedro.PedroCommands.hold;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedro.Constants;

@Autonomous(name = "PedroPathing/Ivy (Autonomous)", group = "Auto")
public class PedroPathingAuto extends OpMode {

    // simply put:
    // ivy plans stuff out and schedules it
    // pedro pathing just actually handles the pathing and driving

    // TODO: fix these placeholder nums
    // same height of zero unless playing at an incline
    private static final double START_X = 8,  START_Y = 60,  START_H = 0;   //against the wall
    private static final double LAUNCH_X = 48, LAUNCH_Y = 72, LAUNCH_H = 0; //aimed at target/hive
    private static final double PARK_X = 10,  PARK_Y = 30,  PARK_H = 0;     //inside loading

    private static final double LAUNCH_TIMEOUT_MS = 5000; //max time to fire 4 preloaded balls, could be adjusted

    private Follower follower;
    private boolean isRed = false;

    @Override
    public void init() {
        // clear ivy's scheduler
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        // TODO: we need to init launcher hardware here, once hardware is done
    }

    @Override
    public void init_loop() {
        if (gamepad1.x) {
            isRed = false;
        }
        if (gamepad1.b) {
            isRed = true;
        }
        //use ternary if possible
        telemetry.addData("Alliance", isRed ? "RED" : "BLUE");
        telemetry.addData("Change with", "X = blue, B = red");
        telemetry.update();
    }

    @Override
    public void start() {
        // load the variables as actual poses, using fieldPose to check if we're blue or red
        Pose startPose  = fieldPose(START_X, START_Y, START_H);
        Pose launchPose = fieldPose(LAUNCH_X, LAUNCH_Y, LAUNCH_H);
        Pose parkPose   = fieldPose(PARK_X, PARK_Y, PARK_H);

        Path toLaunch = line(startPose, launchPose).constant(heading(LAUNCH_H));
        Path toPark   = line(launchPose, parkPose).constant(heading(PARK_H));

        follower.setPose(startPose);

        // main auto sequence
        Command auto = sequential(
                follow(follower, toLaunch),              // drive off the wall
                hold(follower, launchPose),              // stay put while shooting
                launch().raceWith(waitMs(LAUNCH_TIMEOUT_MS)), // fire the balls, but stop after timeout
                follow(follower, toPark),                // drive to loading zone
                hold(follower, parkPose)                 // park
        );

        Scheduler.schedule(auto);
    }

    @Override
    public void loop() {
        follower.update();   // pedro pathing needs this every loop apparently
        Scheduler.execute(); // runs whatever Ivy command is active

        telemetry.addData("Alliance", isRed ? "RED" : "BLUE");
        telemetry.addData("Pose", follower.pose());
        telemetry.update();
    }

    @Override
    public void stop() {
        Scheduler.reset();
        stopLauncher();
    }

    // this is an Ivy "command" to start the launcher
    // launcher can be stopped 2 ways: when finished by launcherEmpty boolean, like here, or can just be interrupted by the timeout, like on line 71
    private Command launch() {
        return Command.build()
                .setStart(this::startLauncher)
                .setDone(this::launcherEmpty)
                .setEnd(endCondition -> stopLauncher());
    }

    // mirror blue coordinates across the field's center line for red
    private Pose fieldPose(double x, double y, double headingDeg) {
        if (isRed) {
            x = 144 - x;
        }
        return new Pose(x, y, heading(headingDeg));
    }

    // for heading
    private double heading(double headingDeg) {
        return Math.toRadians(isRed ? 180 - headingDeg : headingDeg);
    }

    private void startLauncher() {
	    // TODO: need launcher prototype finished from hardware team
    }
    private void stopLauncher()  {
	    // TODO: need launcher prototype finished from hardware team
    }
    private boolean launcherEmpty() {
	    // TODO: need launcher prototype finished from hardware team
	    // return false for now though
	    return false;
    }
}