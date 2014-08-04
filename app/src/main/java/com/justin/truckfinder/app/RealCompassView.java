package com.justin.truckfinder.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.View;

/**
 * Created by justindelta on 3/26/14.
 */
public class RealCompassView extends View {
    private static final boolean DEBUG = false;

    private Context myContext;
    private Paint paint;
    private Bitmap compassBitmap;
    private Bitmap newPointerBitmap;
    private final int STROKE_MAX = 18;
    private final int STROKE_MIN = 4;
    private boolean growStroke = true;
    private int glowStrokeWidth = STROKE_MIN;

    private double startDoubleLong;
    private double startDoubleLat;
    private double endDoubleLong;
    private double endDoubleLat;
    public float mTextAngleInDegrees;
    public float mTextPlaceAngleInDegrees;

    static Point currentPoint = new Point(1, 1);
    static float currentRotationInDegrees = 0.0f;


    public SensorDataRequestListener sensorDataCallback;

    public interface SensorDataRequestListener {
        public float getDirection();
    }

    public void setSensorDataCallback(SensorDataRequestListener callback) {
        sensorDataCallback = callback;
    }


    protected float getDirection() {
        return sensorDataCallback.getDirection();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        compassBitmap = BitmapFactory.decodeResource(myContext.getResources(), R.drawable.newcompass);
        compassBitmap = Bitmap.createScaledBitmap(compassBitmap, getWidth(), getHeight(), true);
        newPointerBitmap = BitmapFactory.decodeResource(myContext.getResources(), R.drawable.newpointermod);
        newPointerBitmap = Bitmap.createScaledBitmap(newPointerBitmap, getWidth(), getHeight(), true);
    }

    public RealCompassView(Context context) {
        super(context);
        myContext = context;
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.OVERLAY));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        /*
        redraw all previously touched coordinates
        e.g.

        canvas.drawPoint(x, y, paint);
        for (PointCoordinator p : drawnCoordinates){
            canvas.drawPoint(p.x, p.y, paint);
        }

        if we want to grow the width (boolean indicator is true)
        grow the width until we hit the max
        when we hit the max (reset the boolean to tell it to shrink the width)
        if we want to shrink the width and the width is greater than the min
        shrink the width until we hit the min
        when we hit the min, reset the boolean to tell it to grow
        */

        if (growStroke) {
            glowStrokeWidth++;
        } else {
            glowStrokeWidth--;
        }
        if (glowStrokeWidth >= STROKE_MAX || glowStrokeWidth <= STROKE_MIN) {
            growStroke = !growStroke;
        }

        // calculating all variables to draw what is necessary
        float centerCircleX = getWidth() / 2;
        float centerCircleY = getHeight() / 2;
        float centerRadius = getWidth() / 2;

        //
        // instance of private class Arrow to create Arrow object with the place direction
        // on a unit circle that is used to rotate the arrow drawable on the canvas
        //
        Arrow placeDirection = getPlaceDirectionUnitArrow();

        //  Calculate north
        float lineStartX = centerCircleX;
        float lineStartY = centerCircleY;

        //  North is negative to compensate for azimuth
        float lineEndMagNorthX = centerCircleX + (centerRadius * -((float) Math.sin(getDirection())));
        float lineEndMagNorthY = centerCircleY + (centerRadius * (float) Math.cos(getDirection()));

        //  Calculate vector to destination raw
        //
        //  Swapping x and y because the coordinate system is off
        //
        float lineEndPlaceStaticY = centerCircleX + ((float) placeDirection.endX * centerRadius);
        float lineEndPlaceStaticX = centerCircleY + ((float) placeDirection.endY * centerRadius);



        double angleInRadians = Math.atan2(lineEndMagNorthX - centerCircleX, lineEndMagNorthY - centerCircleY); //angle from -pi to +pi
        if (angleInRadians < 0) {
            angleInRadians += Math.PI * 2;
        }

        float angleInDegrees = (float) Math.toDegrees(angleInRadians);

        double placeInRadians = Math.atan2(lineEndPlaceStaticX - centerCircleX, lineEndPlaceStaticY - centerCircleY);
        if (placeInRadians < 0) {
            placeInRadians += Math.PI * 2;
        }

        float placeInDegrees = (float) Math.toDegrees(placeInRadians);
        mTextAngleInDegrees = angleInDegrees;
        mTextPlaceAngleInDegrees = placeInDegrees;

        //interpolate north smoothly
        Point endMagNorth = new Point(lineEndMagNorthX, lineEndMagNorthY);

        // drawing circle, line, etc.
        paint.setColor(Color.argb(255, 1, 1, 175));
        paint.setStrokeWidth(glowStrokeWidth);


        //
        //  Make y be the height - y, because y draws downward
        //
        double interpolatedDegreesDouble = interpolate(currentRotationInDegrees, angleInDegrees);
        float interpolatedDegrees = (float) interpolatedDegreesDouble;

        //
        // Subtracting 8 because
        //
        canvas.drawCircle(centerCircleX, getHeight() - centerCircleY, centerRadius - 8, paint);
        canvas.rotate(interpolatedDegrees, lineStartX, lineStartY);
        canvas.drawBitmap(compassBitmap, 0, 0, paint);
        canvas.rotate(placeInDegrees, lineStartX, lineStartY);
        canvas.drawBitmap(newPointerBitmap, 0, 0, paint);
        currentPoint = endMagNorth;
        currentRotationInDegrees = interpolatedDegrees;

        // draw debug info

//        if (DEBUG) {
//            //draw magNorth
//            canvas.rotate(-interpolatedDegrees, lineStartX, lineStartY);
//            paint.setColor(Color.argb(255, 255, 0, 0));
//            paint.setStrokeWidth(10);
//
//            canvas.drawLine(lineStartX, getHeight() - lineStartY, lineEndMagNorthX, getHeight() - lineEndMagNorthY, paint);
//            paint.setColor(Color.argb(255, 0, 255, 0));
//            paint.setStrokeWidth(10);
//            canvas.drawLine(0, getHeight(), 40, getHeight() - 40, paint);
//        }
        invalidate();
    }

    //
    //
    //
    public static class Point {
        public float x;
        public float y;

        // Constructor setting necessary x and y values upon creation of Point object
        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    //
    // WEIGHT = 0.015f; pretty good weight for canvas rotation
    // when using "delay game" refresh speed for sensors
    //
    private static final float WEIGHT = .05f;

    //
    // Interpolation method that smoothes out canvas rotation
    // AND overcomes Gimbal Lock
    //
    protected static double interpolate(double startDegree, double endDegree) {

        double changeAbs = Math.abs (endDegree - startDegree);
        if(changeAbs > 180){
            if (endDegree > startDegree){
                startDegree += 360;
            }else {
                endDegree += 360;
            }
        }

        double interpChange =  startDegree + ((endDegree - startDegree) * WEIGHT);
        float rangeZero = 360;
        if (interpChange >= 0 && interpChange < 360)
            return interpChange;

        return (interpChange % rangeZero);
//        Log.v("TAG", String.format("%.2f", startDegree) + ":" + String.format("%.2f", endDegree) + ":" + String.format("%.2f", (interpChange + startDegree)));
    }

    //
    // Private nested class containing the start and end point for the Arrow drawable
    // Creating Arrow object will allow for simpler implementation for canvas rotation
    //
    private class Arrow {
        public double startX;
        public double startY;
        public double endX;
        public double endY;
    }


    //
    // Set double for statLat, startLong (User location)
    // Set double for endLat, endLong (Food Truck location)
    //
    public void setDirections(double startLat, double startLong, double endLat, double endLong) {
        this.startDoubleLat = startLat;
        this.startDoubleLong = startLong;
        this.endDoubleLat = endLat;
        this.endDoubleLong = endLong;
    }

    //
    //  Remember to make this public Arrow convertToUnitArrow(Location location)
    //
    public Arrow getPlaceDirectionUnitArrow() {

        Arrow directorVector = new Arrow();
        directorVector.startX = startDoubleLat;
        directorVector.startY = startDoubleLong;
        directorVector.endX = endDoubleLat;
        directorVector.endY = endDoubleLong;

        //
        // a^2 + b^2 = c^2 = vectorLength, courtesy of Pythagorus of Samos
        //
        double a = directorVector.endX - directorVector.startX;
        double b = directorVector.endY - directorVector.startY;
        double vectorLength = Math.sqrt(a * a + b * b);

        double normalizedX = a / Math.abs(vectorLength);
        double normalizedY = b / Math.abs(vectorLength);

        Arrow drawingArrow = new Arrow();

        drawingArrow.startX = 0;
        drawingArrow.startY = 0;
        drawingArrow.endX = normalizedX;
        drawingArrow.endY = normalizedY;
        return drawingArrow;
    }

}