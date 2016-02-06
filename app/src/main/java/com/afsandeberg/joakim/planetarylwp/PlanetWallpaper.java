package com.afsandeberg.joakim.planetarylwp;

/**
 * Created by Joakim on 2016-02-04.
 */

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.SystemClock;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

/*
 * This animated wallpaper draws a rotating wireframe cube.
 */
public class PlanetWallpaper extends WallpaperService {

    private final Handler mHandler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public Engine onCreateEngine() {
        return new PlanetEngine();
    }

    class PlanetEngine extends Engine {


        private static final float SPEED = 2f;
        private static final float PLANET_RADIUS = 1f;
        private static final float ORBIT_RADIUS = 5f;
        private static final long RENDER_TIME_MS = 16;

        private final Paint mStroke = new Paint();
        private final Paint mSunPaint = new Paint();
        private final Planet [] planets = new Planet[8];

        private float mCenterX;
        private float mCenterY;
        private float angle = 0;

        private final Runnable mDrawCube = new Runnable() {
            public void run() {
                drawFrame();
            }
        };
        private boolean mVisible;

        PlanetEngine() {
            planets[0] = new Planet(Color.LTGRAY, 10, 27, 88f );
            planets[1] = new Planet(Color.RED, 18, 44, 224.7f );
            planets[2] = new Planet(Color.GREEN, 20, 61, 365.2f );
            planets[3] = new Planet(Color.RED, 22, 77, 687f );
            planets[4] = new Planet(Color.YELLOW, 50, 120, 4332f );
            planets[5] = new Planet(Color.YELLOW, 40, 160, 10760f );
            planets[6] = new Planet(Color.BLUE, 30, 240, 30700f );
            planets[7] = new Planet(Color.GRAY, 26, 320, 60200f );
            // Create a Paint to draw the lines for our cube
            final Paint paint = mStroke;
            paint.setColor(Color.WHITE);
            paint.setAntiAlias(true);
            paint.setStrokeWidth(3);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStyle(Paint.Style.STROKE);

            mSunPaint.setColor(Color.YELLOW);
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);

            // By default we don't get touch events, so enable them.
            setTouchEventsEnabled(true);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            mHandler.removeCallbacks(mDrawCube);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            mVisible = visible;
            if (visible) {
                drawFrame();
            } else {
                mHandler.removeCallbacks(mDrawCube);
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format,
                                     int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            // store the center of the surface, so we can draw the cube in the
            // right spot
            mCenterX = width / 2.0f;
            mCenterY = height / 2.0f;
            drawFrame();
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            mVisible = false;
            mHandler.removeCallbacks(mDrawCube);
        }


        /*
         * Store the position of the touch event so we can use it for drawing
         * later
         */

        /*
         * Draw one frame of the animation. This method gets called repeatedly
         * by posting a delayed Runnable. You can do any drawing you want in
         * here. This example draws a wireframe cube.
         */
        void drawFrame() {
            final SurfaceHolder holder = getSurfaceHolder();

            Canvas c = null;
            try {
                c = holder.lockCanvas();
                if (c != null) {
                    // draw something
                    drawCircles(c);
                }
            } finally {
                if (c != null)
                    holder.unlockCanvasAndPost(c);
            }

            // Reschedule the next redraw
            mHandler.removeCallbacks(mDrawCube);
            if (mVisible) {
                mHandler.postDelayed(mDrawCube, RENDER_TIME_MS);
            }
        }

        void drawCircles(Canvas c){
            c.save();
            c.drawColor(Color.BLACK);
            c.drawCircle(mCenterX, mCenterY, 12 * ORBIT_RADIUS, mSunPaint);
            for (Planet planet : planets) {
                c.drawCircle(mCenterX, mCenterY, planet.getOrbitRadius() * ORBIT_RADIUS, mStroke);
            }
            for (Planet planet : planets) {
                drawCircleAtOrbit(c, mCenterX, mCenterY,
                        planet.getOrbitRadius() * ORBIT_RADIUS, angle * planet.getOrbitTime(),
                        planet.getPlanetRadius() * PLANET_RADIUS, planet.getColor());
            }
            angle+= SPEED;
            c.restore();
        }
        void  drawCircleAtOrbit(Canvas c, float centerX, float centerY, float orbitRadius, float angle, float circleRadius, Paint paint){
            float realX = (float)Math.cos(angle)*orbitRadius + centerX;
            float realY = (float)Math.sin(angle)*orbitRadius + centerY;
            c.drawCircle(realX, realY, circleRadius, paint);
        }

    }
}