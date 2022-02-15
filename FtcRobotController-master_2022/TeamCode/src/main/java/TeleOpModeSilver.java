package org.firstinspires.ftc.robotcontroller.external.samples;

//imports
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp (name="TeleOpModeSilver")
public class TeleOpModeSilver extends OpMode{
    /*
    Throughout this section, we initialize the multiple motors and servos required to
    run the robot.
     */

    //Initialize the motors that move the chassis
    DcMotor fl; //Front Left Motor
    DcMotor fr; //Front Right Motor
    DcMotor bl; //Back Left Motor
    DcMotor br; //Back Right Motor

    //Initialize the motors that move the main arm
    DcMotor arm;
    DcMotor toparm;
    CRServo intake;

    //Initialize the carousel
    DcMotor carousel;


    /*
    In the init method, we assign each of the motors and servos to their respective spots on
    the hardware map. We also declare the directions that each of the motors will run.
     */
    @Override
    public void init() {
        //Our telemetry is what we can see on the driver hub. Here, we tell the telemetry that the
        //robot is initialized
        telemetry.addData("Status", "Intialized");

        //We assign each of the dcMotor objects to their respective places on the hardware map
        fl = hardwareMap.dcMotor.get("fl");
        fr = hardwareMap.dcMotor.get("fr");
        bl = hardwareMap.dcMotor.get("bl");
        br = hardwareMap.dcMotor.get("br");

        //We assign the arm and the top arm to their respective places on the hardware map
        arm = hardwareMap.dcMotor.get("arm");
        toparm = hardwareMap.dcMotor.get("toparm");

        //Here, we assign the Continuous Rotation servos. They require a similar yet different setup.
        carousel = hardwareMap.dcMotor.get("carousel");
        intake = hardwareMap.crservo.get("intake");

        //Here, we reset the encoders that run the arm so that the presets work as intended.
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        toparm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //We set the directions of the motor
        fl.setDirection(DcMotorSimple.Direction.FORWARD);
        fr.setDirection(DcMotorSimple.Direction.REVERSE);
        bl.setDirection(DcMotorSimple.Direction.FORWARD);
        br.setDirection(DcMotorSimple.Direction.REVERSE);

        //We set the direction of the arm
        arm.setDirection(DcMotorSimple.Direction.FORWARD);

    }

    @Override
    public void loop() {
        /*
        GAMEPAD 1 CODE
         */
        //These variables will give us the direction and movement of the robot
        double flDirection = +gamepad1.left_stick_x - gamepad1.left_stick_y + gamepad1.right_stick_x;
        double frDirection = -gamepad1.left_stick_x - gamepad1.left_stick_y - gamepad1.right_stick_x;
        double blDirection = -gamepad1.left_stick_x - gamepad1.left_stick_y + gamepad1.right_stick_x;
        double brDirection = +gamepad1.left_stick_x - gamepad1.left_stick_y - gamepad1.right_stick_x;

        //This code makes the robot actually move based on the values derived above ^
        if (gamepad1.left_bumper) {
            //This is just a slower version of the code. If the left bumper is held down then
            //the robot will go slower.
            fl.setPower(flDirection/3);
            fr.setPower(frDirection/3);
            bl.setPower(blDirection/3);
            br.setPower(brDirection/3);
        } else {
            //This code runs the robot at full speed
            fl.setPower(flDirection);
            fr.setPower(frDirection);
            bl.setPower(blDirection);
            br.setPower(brDirection);
        }

        /*
        GAMEPAD 2 CODE
         */

        //This code detects the intake and spins it in the proper direction.
        if (gamepad2.left_bumper) {
            intake.setPower(.8);
        } else if (gamepad2.right_bumper) {
            intake.setPower(-.8);
        } else {
            intake.setPower(0);
        }

        //This code gets the values of the two sticks and moves the motors based on the code
        double left_stick = gamepad2.left_stick_y;
        double right_stick = gamepad2.right_stick_y;

        //This code moves the carousel
        if (gamepad2.x) {
            carousel.setPower(0.5);
        } else {
            carousel.setPower(0);
        }
        //This code moves the carousel the other way
        if (gamepad2.y) {
            carousel.setPower(-0.5);
        } else {
            carousel.setPower(0);
        }

        //move lower arm manually
        if (left_stick != 0) {
            arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            arm.setPower(left_stick);
        } else {
            arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            arm.setPower(0.01);
        }
        //move upper arm manually
        if (right_stick != 0) {
            toparm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            toparm.setPower(right_stick);
        } else {
            toparm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            toparm.setPower(0.01);
        }

        /*
        PRESETS
         */

        // preset for moving the bottom of the arm back to the starting position using the B button

        if (gamepad2.b) {
            if (arm.getCurrentPosition() > 10) {
                arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                arm.setPower(-0.7);
            } else if (arm.getCurrentPosition() < -10) {
                arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                arm.setPower(0.7);
            } else if ((-10 <= arm.getCurrentPosition()) && (arm.getCurrentPosition() <= 10)) {
                arm.setPower(0);
            }
            if (toparm.getCurrentPosition() > 10) {
                toparm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                toparm.setPower(-0.7);
            } else if (toparm.getCurrentPosition() < -10) {
                toparm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                toparm.setPower(0.7);
            } else if ((-10 <= toparm.getCurrentPosition()) && (toparm.getCurrentPosition() <= 10)) {
                toparm.setPower(0);
            }

        }

        // moving arm to the ground

        if (gamepad2.a) {
            if  (arm.getCurrentPosition() > 3423){
                arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                arm.setPower(-0.7);
            } else if  (arm.getCurrentPosition() < 3443) {
                arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                arm.setPower(0.7);
            } else if ((3443 <= arm.getCurrentPosition()) && (arm.getCurrentPosition() <= 3423)) {
                arm.setPower(0);
            }
            if  (toparm.getCurrentPosition() > -3463){
                toparm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                toparm.setPower(-0.7);
            } else if (toparm.getCurrentPosition() < -3483) {
                toparm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                toparm.setPower(0.7);
            } else if ((-3483 <= toparm.getCurrentPosition()) && (toparm.getCurrentPosition() <= -3463)) {
                toparm.setPower(0);
            }

        }

        if (gamepad2.dpad_up) {
            if  (arm.getCurrentPosition() > 508){
                arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                arm.setPower(-0.7);
            } else if  (arm.getCurrentPosition() < 488) {
                arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                arm.setPower(0.7);
            } else if ((488 <= arm.getCurrentPosition()) && (arm.getCurrentPosition() <= 508)) {
                arm.setPower(0);
            }
            if  (toparm.getCurrentPosition() > -1463){
                toparm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                toparm.setPower(-0.7);
            } else if (toparm.getCurrentPosition() < -1483) {
                toparm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                toparm.setPower(0.7);
            } else if ((-1483 <= toparm.getCurrentPosition()) && (toparm.getCurrentPosition() <= -1463)) {
                toparm.setPower(0);
            }

        }

        // moving arm to middle hub level

        if (gamepad2.dpad_right) {
            if  (arm.getCurrentPosition() > 1546){
                arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                arm.setPower(-0.7);
            } else if  (arm.getCurrentPosition() < 1526) {
                arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                arm.setPower(0.7);
            } else if ((1526 <= arm.getCurrentPosition()) && (arm.getCurrentPosition() <= 1546)) {
                arm.setPower(0);
            }
            if  (toparm.getCurrentPosition() > -1752){
                toparm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                toparm.setPower(-0.7);
            } else if (toparm.getCurrentPosition() < -1772) {
                toparm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                toparm.setPower(0.7);
            } else if ((-1772 <= toparm.getCurrentPosition()) && (toparm.getCurrentPosition() <= -1752)) {
                toparm.setPower(0);
            }

        }

        // moving arm to lowest hub level

        if (gamepad2.dpad_down) {
            if  (arm.getCurrentPosition() > -2214){
                arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                arm.setPower(-0.7);
            } else if  (arm.getCurrentPosition() < -2224) {
                arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                arm.setPower(0.7);
            } else if ((-2224 <= arm.getCurrentPosition()) && (arm.getCurrentPosition() <= -2214)) {
                arm.setPower(0);
            }
            if  (toparm.getCurrentPosition() > -3082){
                toparm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                toparm.setPower(-0.7);
            } else if (toparm.getCurrentPosition() < -3092) {
                toparm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                toparm.setPower(0.7);
            } else if ((-3092 <= toparm.getCurrentPosition()) && (toparm.getCurrentPosition() <= -3082)) {
                toparm.setPower(0);
            }

        }
    }
}
