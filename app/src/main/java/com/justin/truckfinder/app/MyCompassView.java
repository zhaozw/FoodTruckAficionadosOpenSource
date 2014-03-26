package com.justin.truckfinder.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by justindelta on 3/26/14.
 */
public class MyCompassView extends View implements View.OnTouchListener{

        private float x;//instantiated at 0.0
        private float y;
        private Paint paint;
        private ArrayList<PointCoordinator> drawnCoordinates = new ArrayList<PointCoordinator>();
        private ArrayList<Path> pathCoordinates = new ArrayList<Path>();


        private final int STROKE_MAX = 40;
        private final int STROKE_MIN = 5;
        private boolean growStroke = true;
        private int glowStrokeWidth = STROKE_MIN;



        public MyCompassView(Context context) {
            super(context);
            setFocusable(true);
            setFocusableInTouchMode(true);
            setOnTouchListener(this);
            paint = new Paint();
            paint.setAntiAlias(true);

            paint.setStyle(Paint.Style.STROKE);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.ADD));
            drawnCoordinates.add(new PointCoordinator(x, y));

        }

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

            if (growStroke){
                glowStrokeWidth++;
            } else  {
                glowStrokeWidth--;
            }


            if (glowStrokeWidth >= STROKE_MAX || glowStrokeWidth <= STROKE_MIN){
                growStroke = !growStroke;
            }






            for (Path path : pathCoordinates){
                paint.setColor(Color.argb(200, 50, 200, 50));
                paint.setStrokeWidth(4);
                canvas.drawPath(path, paint);
//            canvas.drawText();

                paint.setColor(Color.argb(80, 50, 200, 50));
                paint.setStrokeWidth(glowStrokeWidth);
                canvas.drawPath(path, paint);

            }

            invalidate();

        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            Path currentPath;
            switch(event.getAction()){
                case MotionEvent.ACTION_DOWN:{
                    currentPath = new Path();
                    currentPath.moveTo(event.getX(), event.getY());
                    pathCoordinates.add(currentPath);
                    break;
                }
                case MotionEvent.ACTION_MOVE:{
                    int lastPath = pathCoordinates.size()-1;
                    if(lastPath < 0){
                        break;
                    }
                    pathCoordinates.get(lastPath).lineTo(event.getX(), event.getY());
                    break;
                }
                default:
                    break;
            }


//        x = event.getX();
//        y = event.getY();
//        invalidate();
//
//        Path p = new Path();
//        int lastCoord = drawnCoordinates.size()-1;
//        p.moveTo(drawnCoordinates.get(lastCoord).x,drawnCoordinates.get(lastCoord).y);
//        p.lineTo(x,y);
//        pathCoordinates.add(p);
//
//        drawnCoordinates.add(new PointCoordinator(x, y));

            return true;
        }
        private class PointCoordinator {

            public float x;
            public float y;

            public PointCoordinator(float x, float y) {
                this.x = x;
                this.y = y;
            }

        }
        public void clearCanvas(){
            pathCoordinates.clear();
        }

    }
