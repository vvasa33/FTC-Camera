package org.firstinspires.ftc.robotcontroller.external.samples;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class cvDetector extends OpenCvPipeline {
    Telemetry telemetry;
    Mat mat = new Mat();

    Scalar lowHsv = new Scalar(38.0/2,180,100);
    Scalar highHsv = new Scalar(66.0/2,255,255);
    Rect leftROI, rightROI;

    static final double threshold = 0.45;

    public cvDetector(Telemetry t, Rect left, Rect right) {
        telemetry = t;
        leftROI = left;
        rightROI = right;
    }
    @Override
    public Mat processFrame(Mat input) {
        Imgproc.cvtColor(input,mat,Imgproc.COLOR_RGBA2RGB);
        Imgproc.cvtColor(mat,mat,Imgproc.COLOR_RGB2HSV);
        Core.inRange(mat, lowHsv,highHsv,mat);

        Mat left = mat.submat(leftROI);
        Mat right = mat.submat(rightROI);

        double leftValue = Core.sumElems(left).val[0]/ leftROI.area()/255;
        double rightValue = Core.sumElems(right).val[0]/rightROI.area()/255;

        left.release();
        right.release();

        telemetry.addData("Left Percentage: ", Math.round(leftValue*100) + "%");
        telemetry.addData("Right Percentage: ", Math.round(rightValue*100) + "%");

        boolean elementLeft = leftValue > threshold;
        boolean elementRight = rightValue > threshold;
        if (elementLeft) {
            telemetry.addData("At the left","");
        } else if (elementRight) {
            telemetry.addData("At the right","");
        } else {
            telemetry.addData("Error:brain.exe has stopped working","");
        }

        telemetry.update();

        Imgproc.cvtColor(mat,mat,Imgproc.COLOR_GRAY2RGB);
        Scalar colorElement = new Scalar(0,255,0);
        Scalar noElement = new Scalar(255,0,0);

        Imgproc.rectangle(mat,leftROI, (elementLeft) ? colorElement : noElement);
        Imgproc.rectangle(mat,rightROI, (elementRight) ? colorElement : noElement);
        return mat;
    }
}
