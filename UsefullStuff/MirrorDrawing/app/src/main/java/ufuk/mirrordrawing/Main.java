package ufuk.mirrordrawing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import Jama.*;

public class Main extends AppCompatActivity {

    DrawingView dv ;

    private Paint drawingPaint;
    private Paint mirrorPaint;

    public int maxWidth;
    public int maxHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        dv = new DrawingView(this);
        setContentView(dv);

        mirrorPaint = new Paint();
        mirrorPaint.setAntiAlias(true);
        mirrorPaint.setDither(true);
        mirrorPaint.setColor(Color.BLACK);
        mirrorPaint.setStyle(Paint.Style.STROKE);
        mirrorPaint.setStrokeJoin(Paint.Join.ROUND);
        mirrorPaint.setStrokeCap(Paint.Cap.ROUND);
        mirrorPaint.setStrokeWidth(0.1f);

        drawingPaint = new Paint();
        drawingPaint.setAntiAlias(true);
        drawingPaint.setDither(true);
        drawingPaint.setColor(Color.BLACK);
        drawingPaint.setStyle(Paint.Style.STROKE);
        drawingPaint.setStrokeJoin(Paint.Join.ROUND);
        drawingPaint.setStrokeCap(Paint.Cap.ROUND);
        drawingPaint.setStrokeWidth(12);
    }

    public class DrawingView extends View {

        private Bitmap bitmap;
        private Canvas canvas;
        private Path p1;
        private Path p2;
        private Path p3;
        private Path p4;

        private Paint bitmapPaint;
        Context context;
        private Paint circlePaint;
        private Path circlePath;

        public DrawingView(Context c) {
            super(c);
            context=c;

            p1 = new Path();
            p2 = new Path();
            p3 = new Path();
            p4 = new Path();

            bitmapPaint = new Paint(Paint.DITHER_FLAG);
            circlePaint = new Paint();
            circlePath = new Path();

            circlePaint.setAntiAlias(true);
            circlePaint.setColor(Color.BLUE);
            circlePaint.setStyle(Paint.Style.STROKE);
            circlePaint.setStrokeJoin(Paint.Join.MITER);
            circlePaint.setStrokeWidth(4f);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);

            maxWidth = w;
            maxHeight = h;

            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            canvas = new Canvas(bitmap);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvas.drawBitmap(bitmap, 0, 0, bitmapPaint);

            canvas.drawPath(p1, drawingPaint);
            canvas.drawPath(p2, drawingPaint);
            canvas.drawPath(p3, drawingPaint);
            canvas.drawPath(p4, drawingPaint);

            canvas.drawPath(circlePath,  circlePaint);
            canvas.drawLine(maxWidth/2, 0, maxWidth/2, maxHeight, mirrorPaint);
            canvas.drawLine(0, maxHeight/2, maxWidth, maxHeight/2, mirrorPaint);
        }

        private float mX, mY;
        private static final float TOUCH_TOLERANCE = 4;

        private void touch_start(float x, float y) {
            p1.reset();
            p2.reset();
            p3.reset();
            p4.reset();

            p1.moveTo(x, y);
            p2.moveTo(maxWidth-x, y);
            p3.moveTo(x, maxHeight-y);
            p4.moveTo(maxWidth-x, maxHeight-y);

            mX = x;
            mY = y;
        }

        private void touch_move(float x, float y) {
            float dx = Math.abs(x - mX);

            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                p1.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
                p2.quadTo(maxWidth-mX, mY, maxWidth-((x + mX)/2), (y + mY)/2);
                p3.quadTo(mX, maxHeight-mY, (x + mX)/2, maxHeight-((y + mY)/2));
                p4.quadTo(maxWidth-mX, maxHeight-mY, maxWidth-((x + mX)/2), maxHeight-((y + mY)/2));

                mX = x;
                mY = y;

                circlePath.reset();
                circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
            }
        }

        private void touch_up() {
            p1.lineTo(mX, mY);
            p2.lineTo(maxWidth-mX, mY);
            p3.lineTo(mX, maxHeight-mY);
            p4.lineTo(maxWidth-mX, maxHeight-mY);

            circlePath.reset();
            // commit the path to our offscreen
            canvas.drawPath(p1, drawingPaint);
            canvas.drawPath(p2, drawingPaint);
            canvas.drawPath(p3, drawingPaint);
            canvas.drawPath(p4, drawingPaint);

            // kill this so we don't double draw
            p1.reset();
            p2.reset();
            p3.reset();
            p4.reset();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    break;
            }
            return true;
        }
    }
}