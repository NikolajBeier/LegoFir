package org.example.ui.Calibration;

import com.google.gson.*;

import com.google.gson.reflect.TypeToken;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.*;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.opencv.calib3d.Calib3d.CALIB_CB_ADAPTIVE_THRESH;
import static org.opencv.calib3d.Calib3d.CALIB_CB_NORMALIZE_IMAGE;


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

    public CameraCalibration(){
        this.boardsNumber = 30;
        this.numCornersHor = 9;
        this.numCornersVer = 6;
        int numSquares = this.numCornersHor * this.numCornersVer;
        for (int j = 0; j < numSquares; j++)
            obj.push_back(new MatOfPoint3f(new Point3((double) j / this.numCornersHor, j % this.numCornersVer, 0.0f)));
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
                     Calib3d.CALIB_CB_FAST_CHECK + CALIB_CB_ADAPTIVE_THRESH);
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
            Imgcodecs.imwrite("src/main/java/org/example/ui/Calibration/snapshots" + successes + ".png", savedImage);
            this.imagePoints.add(imageCorners);
            imageCorners = new MatOfPoint2f();
            this.objectPoints.add(obj);
            this.successes++;
        }
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
        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(objectPoints , new TypeToken<List<Mat>>() {}.getType());
        JsonArray jsonArray = element.getAsJsonArray();
        String json = gson.toJson(jsonArray);
        String json123 = gson.toJson(element);
        //String json = gson.toJson(objectPoints,objectPointsType);
        String json2 = gson.toJson(imagePoints,imagePoints.getClass());
        String json3 = gson.toJson(savedImage.size(),savedImage.size().getClass());
        String json4 = gson.toJson(intrinsic,intrinsic.getClass());
        String json5 = gson.toJson(distCoeffs,distCoeffs.getClass());
        String json6 = gson.toJson(rvecsGlobal,rvecsGlobal.getClass());
        String json7 = gson.toJson(tvecsGlobal,tvecsGlobal.getClass());

        JsonObject object = new JsonObject();
        object.addProperty("objectPoints", json);
        object.addProperty("objectPoints2", json123);
        object.addProperty("imagePoints", json2);
        object.addProperty("savedImageSize", json3);
        object.addProperty("intrinsic", json4);
        object.addProperty("distCoeff", json5);
        object.addProperty("rvecs", json6);
        object.addProperty("tvecs", json7);


        try {
            FileWriter fileWriter = new FileWriter("src/main/java/org/example/ui/Calibration/cameracalibration.json");
            fileWriter.write(object.toString());
            fileWriter.close();
        } catch (Exception exception) {

        }
    }
}
