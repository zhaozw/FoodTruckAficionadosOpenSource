package com.justin.truckfinder.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.hardware.GeomagneticField;
import android.location.Location;
import android.view.View;

/**
 * Created by justindelta on 3/26/14.
 */
public class MyCompassView extends View  {

    private Paint paint;
    private final int STROKE_MAX = 30;
    private final int STROKE_MIN = 1;
    private boolean growStroke = true;
    private int glowStrokeWidth = STROKE_MIN;

    private double startDoubleLong;
    private double startDoubleLat;
    private double endDoubleLong;
    private double endDoubleLat;
    protected float[] matrixValues = {};
    private float mDirection;
    protected GeomagneticField mGeomagneticField;


    private static final double RADIANS_TO_DEGREES = (180 / Math.PI);
    private static final double DEGREES_TO_RADIANS = (Math.PI / 180);


    private float[] mR = new float[16];
    private float[] mOrientation = new float[3];

    public SensorDataRequestListener sensorDataCallback;

    public interface SensorDataRequestListener{
        public float[] getDirection();
    }

    public void setSensorDataCallback(SensorDataRequestListener callback){
        sensorDataCallback = callback;
    }

    private float[] getDirection(){
        return sensorDataCallback.getDirection();
    }



    public MyCompassView(Context context) {
        super(context);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.ADD));
    }

    static Point currentPoint = new Point(1,1);
    static Point currentPointPlaceStatic = new Point(1,1);


    @Override
    protected void onDraw(Canvas canvas) {
        //redraw all previously touched coordinates
//        canvas.drawPoint(x, y, paint);
//        for (PointCoordinator p : drawnCoordinates){
//            canvas.drawPoint(p.x, p.y, paint);
//        }
        //if we want to grow the width (boolean indicator is true)
        // grow the width until we hit the max
        // when we hit the max (reset the boolean to tell it to shrink the width)
        //if we want to shrink the width and the width is greater than the min
        //shrink the width until we hit the min
        // when we hit the min, reset the boolean to tell it to grow
        getRotation();


        if (growStroke) {
            glowStrokeWidth++;
        } else {
            glowStrokeWidth--;
        }
        if (glowStrokeWidth >= STROKE_MAX || glowStrokeWidth <= STROKE_MIN) {
            growStroke = !growStroke;
        }

        paint.setColor(Color.argb(255, 1, 200, 50));
        paint.setStrokeWidth(glowStrokeWidth);

        float centerCircleX = getWidth() / 2;
        float centerCircleY = getHeight() / 2;
        float centerRadius = getWidth() / 2;

        canvas.drawCircle(centerCircleX, centerCircleY, centerRadius, paint);


        Arrow drawingArrow = getUnitArrow();

        matrixValues = getDirection();
        float lineStartX = centerCircleX + (float) drawingArrow.startX;
        float lineStartY = centerCircleY + (float) drawingArrow.startY;
        float lineEndPlaceStaticX = centerCircleX + (float) drawingArrow.endX * centerRadius;
        float lineEndPlaceStaticY = centerCircleY + (float) drawingArrow.endY * centerRadius;
        float lineEndMagNorthX = centerCircleX + centerRadius * -(float) Math.sin(matrixValues[0]);
        float lineEndMagNorthY = centerCircleY + centerRadius * -(float) Math.cos(matrixValues[0]);

        float deltaX = lineEndPlaceStaticX - lineEndMagNorthX;
        float deltaY = lineEndPlaceStaticY - lineEndMagNorthY;


        float angleInDegrees = (float) (Math.atan2(deltaY, deltaX) * RADIANS_TO_DEGREES);
//        angleInDegrees = (float) convertToDegreesOnCircle(angleInDegrees);
//        if (angleInDegrees == 0) {
//            angleInDegrees = (float) (angleInDegrees + .01);
//        } else if (angleInDegrees > 359) {
//            angleInDegrees = (float) (angleInDegrees - .01);
//        }


        Point endPlaceStatic = new Point (lineEndPlaceStaticX, lineEndPlaceStaticY);
        Point endMagNorth = new Point(lineEndMagNorthX,lineEndMagNorthY);


        Point smoothPointPlace = interpolate(currentPointPlaceStatic,endPlaceStatic);
        Point smoothPoint = interpolate(currentPoint,endMagNorth);

        canvas.rotate(angleInDegrees, lineStartX, lineStartY);
//        canvas.drawLine(lineStartX, lineStartY, smoothPointPlace.x, smoothPointPlace.y, paint);
//        currentPointPlaceStatic = endPlaceStatic;

        canvas.drawLine(lineStartX, lineStartY, smoothPoint.x, smoothPoint.y, paint);
        currentPoint = endMagNorth;

        invalidate();
//        canvas.drawCircle(w/2, h/2, r, paint);
//
//        paint.setColor(Color.RED);
//        canvas.drawLine(
//                w/2,
//                h/2,
//                (float)(w/2 + r * Math.sin(-direction)),
//                (float)(h/2 - r * Math.cos(-direction)),
//                paint);
    }

    private float computeTrueNorth(float heading) {
        if (mGeomagneticField != null) {
            return heading + mGeomagneticField.getDeclination();
        } else {
            return heading;
        }
    }

    public static float mod(float a, float b) {
        return (a % b + b) % b;
    }


    public double calculateDistanceBetweenPointsDegrees(double magNorthDegreesX, double magNorthDegreesY, double placeDegreesX, double placeDegreesY) {
        double calculateDistanceBetweenPoints;

        // Location of Destination in GPS coordinates
        double placeLatitudeEnd = placeDegreesX;
        double placeLongitudeEnd = placeDegreesY;


        double theta = magNorthDegreesY - placeLongitudeEnd;

        calculateDistanceBetweenPoints = Math.sin(degreesToRadians(magNorthDegreesX))
                * Math.sin(degreesToRadians(placeLatitudeEnd))
                + Math.cos(degreesToRadians(magNorthDegreesX))
                * Math.cos(degreesToRadians(placeLatitudeEnd))
                * Math.cos(degreesToRadians(theta));

        calculateDistanceBetweenPoints = Math.acos(calculateDistanceBetweenPoints);
        return radiansToDegrees(calculateDistanceBetweenPoints);
    }
        public static class Point{
        public float x;
        public float y;

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }


        }


    private static final float WEIGHT = 0.05f;
    private static Point interpolate(Point startPoint, Point endPoint){
        float changeX = endPoint.x - startPoint.x;
        float changeY = endPoint.y - startPoint.y;
        float distance = (float) Math.sqrt((changeX * changeX) + (changeY * changeY));
        if(distance > WEIGHT){
            float ratio = WEIGHT / distance;
            float xMove = ratio * changeX;
            float yMove = ratio * changeY;
            return new Point(startPoint.x + xMove, startPoint.y + yMove);

        }else{
            return new Point(endPoint.x, endPoint.y);
        }
    }




    private class Arrow{

        public double startX;
        public double startY;
        public double endX;
        public double endY;

    }


    public void setDirections(double startLong, double startLat, double endLong, double endLat){
        this.startDoubleLat = startLat;
        this.startDoubleLong = startLong;
        this.endDoubleLat = endLat;
        this.endDoubleLong = endLong;
    }

    //remember to make this public Arrow convertToUnitArrow(Location location)
    public Arrow getUnitArrow() {

        Arrow directorVector = new Arrow();
        directorVector.startX = startDoubleLat;
        directorVector.startY = startDoubleLong;
        directorVector.endX = endDoubleLat;
        directorVector.endY = endDoubleLong;
        // a^2 + b^2 = c^2 = vectorLength
        double a = directorVector.endX - directorVector.startX;
        double b = directorVector.endY - directorVector.startY;
        double vectorLength = Math.sqrt(a*a + b*b);

        double normalizedX =  a / Math.abs(vectorLength);
        double normalizedY =  b / Math.abs(vectorLength);

        Arrow drawingArrow = new Arrow();

        drawingArrow.startX = 0;
        drawingArrow.startY = 0;
        drawingArrow.endX = normalizedX;
        drawingArrow.endY = normalizedY;
        return drawingArrow;
    }

    public double determineDirection(Location location) {

        // Torchy's
        double placeLatitude = 30.25306507248696;
        double placeLongitude = -97.74808133834131;

        double x1 = location.getLatitude();
        double y1 = location.getLongitude();
        double x2 = placeLatitude;
        double y2 = placeLongitude;

//        double destinationX = (userLocation.getLatitude() - placeLatitude);
//        double destinationY = (userLocation.getLongitude() - placeLongitude);

        double theta = y1 - y2;
        double directionAndDistance = Math.sin(degreesToRadians(x1));
        directionAndDistance = directionAndDistance * Math.sin(degreesToRadians(x2));
        directionAndDistance = directionAndDistance + Math.cos(degreesToRadians(x1));
        directionAndDistance = directionAndDistance * Math.cos(degreesToRadians(y1));
        directionAndDistance = directionAndDistance * Math.cos(degreesToRadians(theta));

        directionAndDistance = Math.acos(directionAndDistance);

        directionAndDistance = radiansToDegrees(directionAndDistance);
        //TODO use this

        double directionInDegreesOnCircle = directionAndDistance;

        return directionInDegreesOnCircle;

    }


    private double degreesToRadians(double degree) {
        return (degree * (Math.PI / 180.0));
    }

    private double radiansToDegrees(double radians) {
        return (radians * (180 / Math.PI));
    }

    private double convertToDegreesOnCircle(double degree){
        if(degree <= 0) {
            return (degree+=360);
        }else{
            return degree;
        }
    }




}