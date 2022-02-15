import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


@Autonomous
public class BlueLeft_Auto extends LinearOpMode {
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

    //    @RequiresApi(api = Build.VERSION_CODES.O)
    public void runOpMode() throws InterruptedException {
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

        fl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        br.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        carousel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        waitForStart();

        final int ticks = 1600;
        //MOVE TO THE LEFT
        fl.setTargetPosition(2500);
        fr.setTargetPosition(2500);
        bl.setTargetPosition(2500);
        br.setTargetPosition(2500);

        fl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        br.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        while (fl.getCurrentPosition() < fl.getTargetPosition()) {
            fl.setPower(1);
            fr.setPower(-1);
            bl.setPower(-1);
            br.setPower(0.8);
        }

        fl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        br.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        fl.setTargetPosition(7000);
        fr.setTargetPosition(7000);
        bl.setTargetPosition(7000);
        br.setTargetPosition(7000);

        fl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        br.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        while (fl.getCurrentPosition() < fl.getTargetPosition()) {
            fl.setPower(1);
            fr.setPower(1);
            bl.setPower(1);
            br.setPower(0.8);
        }
        fl.setPower(0);
        fr.setPower(0);
        bl.setPower(0);
        br.setPower(0);
    }
}
