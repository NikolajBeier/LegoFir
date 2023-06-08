package org.example.ui.Calibration;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.*;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import static org.opencv.calib3d.Calib3d.CALIB_CB_ADAPTIVE_THRESH;
import static org.opencv.calib3d.Calib3d.CALIB_CB_NORMALIZE_IMAGE;
import static org.opencv.imgproc.Imgproc.resize;


public class CameraCalibration {

    MatOfPoint3f obj = new MatOfPoint3f();
    MatOfPoint2f imageCorners = new MatOfPoint2f();
    Mat savedImage = new Mat();
    Mat undistoredImage = null;
    List<Mat> imagePoints = new ArrayList<>();
    List<Mat> objectPoints = new ArrayList<>();
    Mat intrinsic = new Mat(3, 3, CvType.CV_32FC1);
    Mat distCoeffs = new Mat();
    int successes = 0;
    boolean isCalibrated = false;
    boolean found;

    List<Mat> rvecsGlobal = new ArrayList<>();
    List<Mat> tvecsGlobal = new ArrayList<>();

    private final int boardsNumber;

    private final int numCornersHor;
    private final int numCornersVer;

    private boolean running = true;

    VideoCapture capture;
    Mat image;
    Mat webCamImage;
    byte[] imageData;
    ImageIcon icon;
    boolean detect;

    public CameraCalibration(){
        this.boardsNumber = 4;
        this.numCornersHor = 9;
        this.numCornersVer = 6;
        int numSquares = this.numCornersHor * this.numCornersVer;
        for (int j = 0; j < numSquares; j++)
            obj.push_back(new MatOfPoint3f(new Point3((double) j / this.numCornersHor, j % this.numCornersVer, 0.0f)));
    }

    public void runCalibration(JLabel cameraScreen){
        capture = new VideoCapture(0);
        capture.set(Videoio.CAP_PROP_BUFFERSIZE, 3);
        image = new Mat();
        webCamImage = new Mat();
        capture.read(image);
        final MatOfByte tempBuf = new MatOfByte();
        Imgcodecs.imencode(".jpg", image, tempBuf);
        imageData = tempBuf.toArray();
        icon = new ImageIcon(imageData);


        while(running){
            // read image to matrix
            capture.read(image);
            //image = Imgcodecs.imread("chessboard.jpg");

            if(detect){
                detectBoard(image);
            }

            if (getCalibrated())
            {
                image = undistort(image);
            }
            resize(image, image, new Size(1280, 720));
            final MatOfByte buf = new MatOfByte();
            Imgcodecs.imencode(".jpg", image, buf);

            imageData = buf.toArray();

            // Add to JLabel
            icon = new ImageIcon(imageData);
            cameraScreen.setIcon(icon);
        }
    }


    public void detectBoard(Mat image){
        Mat grayImage = new Mat();

        // I would perform this operation only before starting the calibration
        // process
        if (this.successes < this.boardsNumber) {
            // convert the frame in gray scale
            Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);

            // the size of the chessboard
            Size boardSize = new Size(this.numCornersVer, this.numCornersHor);
            // look for the inner chessboard corners
            found = Calib3d.findChessboardCorners(grayImage, boardSize, imageCorners,
                     Calib3d.CALIB_CB_FAST_CHECK );//+ CALIB_CB_ADAPTIVE_THRESH);
            // all the required corners have been found...
            if (found && !imageCorners.empty())
            {
                // optimization
                TermCriteria term = new TermCriteria(TermCriteria.EPS | TermCriteria.MAX_ITER, 30, 0.1);
                Imgproc.cornerSubPix(grayImage, imageCorners, new Size(5, 5), new Size(-1, -1), term);
                // save the current frame for further elaborations
                grayImage.copyTo(this.savedImage);
                // show the chessboard inner corners on screen
                Calib3d.drawChessboardCorners(image, boardSize, imageCorners, found);

                // enable the option for taking a snapshot
            }
        }
    }

    public void calibrateCam(){
        if(!isCalibrated) {
            // init needed variables according to OpenCV docs
            List<Mat> rvecs = new ArrayList<>();
            List<Mat> tvecs = new ArrayList<>();
            intrinsic.put(0, 0, 1);
            intrinsic.put(1, 1, 1);
            // calibrate!
            Calib3d.calibrateCamera(objectPoints, imagePoints, savedImage.size(), intrinsic, distCoeffs, rvecs, tvecs);
            this.isCalibrated = true;

            rvecsGlobal = rvecs;
            tvecsGlobal = tvecs;
        }
    }
    public void TakeSnapShot(){
        if(found){
            this.imagePoints.add(imageCorners);
            imageCorners = new MatOfPoint2f();
            this.objectPoints.add(obj);
            this.successes++;
        }
    }

    public boolean isSufficient(){
        return this.successes == this.boardsNumber;
    }

    public Mat undistort(Mat image){
        // prepare the undistored image
        Mat undistort = new Mat();
        Calib3d.undistort(image, undistort, intrinsic, distCoeffs);

        return undistort;
    }

    public boolean getCalibrated(){
        return this.isCalibrated;
    }

    public void saveCalibration() {

        JSONArray objectjson = new JSONArray();
        objectjson.add(objectPoints);
        JSONObject params = new JSONObject();
        params.put("objectPoints", objectjson);
        params.put("imagePoints", new JSONArray().add(imagePoints));
        params.put("savedImageSize", new JSONArray().add(savedImage.size()));
        params.put("intrinsic", new JSONArray().add(intrinsic));
        params.put("distCoeff", new JSONArray().add(distCoeffs));
        params.put("rvecs", new JSONArray().add(rvecsGlobal));
        params.put("rvecs", new JSONArray().add(tvecsGlobal));

        try {
            FileWriter fileWriter = new FileWriter("src/main/java/org/example/ui/Calibration/colors.json");
            fileWriter.write(params.toJSONString());
            fileWriter.close();
        } catch (Exception exception) {

        }
    }
    public void closeCalibration(){
        running = false;
        capture.release();
    }
    public void setDetect(boolean detect){
        this.detect = detect;
    }
}
