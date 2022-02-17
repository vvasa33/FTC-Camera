//package imports
package org.firstinspires.ftc.robotcontroller.external.samples;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class cvDetector extends OpenCvPipeline {
    //Initializes a new telemetry object and a matrix
    Telemetry telemetry;
    Mat mat = new Mat();

    /*
    Creates a scalar which can hold a list of values in a range. These scalars are pretty important
    because they describe the color which the program is going to be looking for. In this case,
    we're looking for yellow but if your shipping element is a different color for some reason
    you can change it using a HSV calculator.
     */
    Scalar lowHsv = new Scalar(38.0/2,180,100);
    Scalar highHsv = new Scalar(66.0/2,255,255);
    //Create two null rectangles which we'll initialize in our constructor
    Rect leftROI, rightROI;

    //This value will tell us how much of the duck needs to be in the frame to guarantee that there
    //duck
    static final double threshold = 0.45;


    boolean elementLeft, elementRight;
    public position location;
    public enum position {
        LEFT,
        RIGHT,
        NOT_DETECTED
    }
    /*
    Our main constructor. It takes in the telemetry and the two rectangles that we want
    displayed on the screen.
     */
    public cvDetector(Telemetry t, Rect left, Rect right) {
        telemetry = t;
        leftROI = left;
        rightROI = right;
    }

    public cvDetector() {

    }
    /*
    This one's a little tricky.
    This method takes in the image that the camera produces. The camera takes the image that it
    sees and splits it into a matrix of images in order to make processing the actual image
    easier for us. This method allows us to make decisions based on the rectangles that we
    draw on the screen. Technically, we could leave this all blank and output the actual image, but
    that's no fun (and useless), so we do some math to help us figure out if there's a duck in those
    squares.
     */
    @Override
    public Mat processFrame(Mat input) {
        /*
        We convert the image into a Hue, Saturation, Value format. We then give this code to a
        Core class which can do computations for us
         */
        Imgproc.cvtColor(input,mat,Imgproc.COLOR_RGBA2RGB);
        Imgproc.cvtColor(mat,mat,Imgproc.COLOR_RGB2HSV);
        Core.inRange(mat, lowHsv,highHsv,mat);

        /*
        We add the rectangles to the screen by creating a sub matrix inside of the original matrix.
        We can then analyze these two matrices to see if they have a duck in them or not.
         */
        Mat left = mat.submat(leftROI);
        Mat right = mat.submat(rightROI);

        /*
        We do some calculations to detect if there's a duck in the area defined by the rectangles
        that we created in the above lines.
         */
        double leftValue = Core.sumElems(left).val[0]/ leftROI.area()/255;
        double rightValue = Core.sumElems(right).val[0]/rightROI.area()/255;

        //Our matrices take up a *ton* of space so we release them from the memory
        left.release();
        right.release();

        //We output the values based on our calculations above into the telemetry
        telemetry.addData("Left Percentage: ", Math.round(leftValue*100) + "%");
        telemetry.addData("Right Percentage: ", Math.round(rightValue*100) + "%");

        /*
        We create these boolean values to see whether a duck is at least 45% visible in the frame.
        You can change the value of threshold to your liking
         */
        elementLeft = leftValue > threshold;
        elementRight = rightValue > threshold;

        /*
        We display whether the duck is in the left rectangle, right rectangle, or none of the
        rectangles.
         */
        if (elementLeft) {
            telemetry.addData("At the left","");
        } else if (elementRight) {
            telemetry.addData("At the right","");
        } else {
            telemetry.addData("Error:brain.exe has stopped working","");
        }

        //Update the telemetry so that the stuff we put out onto the screen actually shows up
        telemetry.update();

        /*
        We convert the image to a black/white image in order to clearly see the yellow in our screen.
        We also create some new scalars to change the color of the rectangle to green if there
        is a duck in sight and red if not.
         */
        Imgproc.cvtColor(mat,mat,Imgproc.COLOR_GRAY2RGB);
        Scalar colorElement = new Scalar(0,255,0);
        Scalar noElement = new Scalar(255,0,0);

        /*
        We evaluate a ternary to check what color values we need to give to the rectangles.
        If there's at least 45% of the duck visible to the program (which we already determined
        in the elementLeft and elementRight variables), then we can set the value of the rectangle
        to the colorElement scalar, and if not then we set it to the noElement scalar
         */
        Imgproc.rectangle(mat,leftROI, (elementLeft) ? colorElement : noElement);
        Imgproc.rectangle(mat,rightROI, (elementRight) ? colorElement : noElement);

        //Return the matrix back to the original program so that it can do some calculations!
        return mat;
    }

//    public position getLocation() {
//        if (elementLeft) {
//            return position.LEFT;
//        } else if (elementRight) {
//            return position.RIGHT;
//        } else {
//            return position.NOT_DETECTED;
//        }
//    }
}
