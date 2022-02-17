//Project imports, many of them are for camera detection
package org.firstinspires.ftc.robotcontroller.external.samples;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

import org.firstinspires.ftc.robotcontroller.external.samples.cvDetector;

//We initialize the program as an autonomous file
@Autonomous (name="cameraTesting")
public class camTest extends LinearOpMode{
    //Creating a camera object (internal camera)
    Telemetry telemetry;
    OpenCvCamera phoneCam;
    //Create a basic rectangle with the specified points
    public static final Rect leftROI = new Rect(
            new Point(10,60),
            new Point(110,160)
    );
    public static final Rect rightROI = new Rect(
            new Point(180,60),
            new Point(240,160)
    );

    //main op mode; If you plan to use this code inside of an actual autonomous copy this code into
    //the top of your code
    @Override
    public void runOpMode() throws InterruptedException {
        /*
        This part is a little tricky. The code is basically creating an ID for the camera so that
        the software can recognize it. Then we assign the ID to the phoneCam object that we
        instantiated above.
         */
        int cameraMonitorViewId = hardwareMap.appContext.getResources().
                getIdentifier("cameraMonitorViewId","id",hardwareMap.appContext.getPackageName());
        phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(
                OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId
        );

        /*
        In order to be able to see the actual output, we create a pipeline to transfer the data
        to the telemetry and also add the rectangles to the view so that the user can see
        what's actually being seen by the robot.
         */

        cvDetector detector = new cvDetector(telemetry, leftROI, rightROI);

        phoneCam.setPipeline(detector);

        /*
        This code basically starts streaming whatever is being seen by the phone cam. If you
        want to see an example of how to do this using an external webcam reference Gold Team's code
         */
        phoneCam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                //you can do upright or sideways
                phoneCam.startStreaming(320,240, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {}
        });

        //Waits for the play button to be pressed
        //***IMPORTANT: Give the phone some time to initialize the camera before actually pressing play.
        waitForStart();

        cvDetector.position location = detector.location;


        if (location == cvDetector.position.LEFT) {
            telemetry.addData("Location: ", "Left");
        } else if (location == cvDetector.position.RIGHT) {
            telemetry.addData("Location: ", "Right");
        } else if (location == cvDetector.position.NOT_DETECTED){
            telemetry.addData("Location: ","Not in Field of View");
        } else {
            telemetry.addData("I am broken ","please help");
        }

        telemetry.update();
        sleep(5000);
        //stops the stream from sending us data because it takes up a lot of space
        phoneCam.stopStreaming();
    }
}
