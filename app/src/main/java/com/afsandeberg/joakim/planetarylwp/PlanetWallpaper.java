package com.afsandeberg.joakim.planetarylwp;

/**
 * Created by Joakim on 2016-02-04.
 */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
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

        private Bitmap sun = BitmapFactory.decodeResource(getResources(), R.mipmap.sun);
        public static final float SPEED = 2f;
        public static final float PLANET_RADIUS = 75f;
        public static final float ORBIT_RADIUS = 5f;
        public static final long RENDER_TIME_MS = 16;

        private final Paint mStroke = new Paint();
        private final Paint mSunPaint = new Paint();
        private final Planet [] planets = new Planet[6];

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

            // Create a Paint to draw the lines for our cube
            final Paint paint = mStroke;
            paint.setColor(Color.WHITE);
            paint.setAntiAlias(true);
            paint.setStrokeWidth(1);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStyle(Paint.Style.STROKE);

            mSunPaint.setColor(Color.YELLOW);
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);

            // By default we don't get touch events, so enable them.
            setTouchEventsEnabled(true);
            Resources resources = getResources();
            planets[0] = new Planet(Color.LTGRAY, 10, 27, 88f, R.mipmap.mercury, resources );
            planets[1] = new Planet(Color.RED, 18, 44, 224.7f, R.mipmap.venus, resources  );
            planets[2] = new Planet(Color.GREEN, 20, 61, 365.2f, R.mipmap.earth, resources  );
            planets[3] = new Planet(Color.RED, 22, 77, 687f, R.mipmap.mars, resources  );
            planets[4] = new Planet(Color.YELLOW, 50, 120, 4332f, R.mipmap.jupiter, resources  );
            planets[5] = new Planet(Color.YELLOW, 40, 160, 10760f, R.mipmap.saturn, resources );
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
            c.drawBitmap(sun, mCenterX-sun.getWidth()/2, mCenterY-sun.getHeight()/2, null);
//            for (Planet planet : planets) {
//                c.drawCircle(mCenterX, mCenterY, planet.getOrbitRadius() * ORBIT_RADIUS, mStroke);
//            }
            for (Planet planet : planets) {
                drawPlanetAtOrbit(c, mCenterX, mCenterY, angle, planet);
            }
            angle+= SPEED;
            c.restore();
        }

        private void drawPlanetAtOrbit(Canvas c, float centerX, float centerY, float angle, Planet planet) {
            float orbitRadius = planet.getOrbitRadius() * ORBIT_RADIUS;
            float realAngle = angle*planet.getOrbitTime();
            float realX = (float)Math.cos(realAngle)*orbitRadius + centerX;
            float realY = (float)Math.sin(realAngle)*orbitRadius + centerY;
//            Rect planetCoordinates = new Rect((int)(realX-planet.getPlanetRadius()/2),
//                    (int)(realY+planet.getPlanetRadius()/2),(int)( realX + planet.getPlanetRadius()/2),
//                    (int)(realY - planet.getPlanetRadius()/2));
            c.drawBitmap(planet.getImage(), realX-planet.getImage().getWidth()/2, realY-planet.getImage().getHeight()/2, null);
 //           c.drawBitmap(planet.getImage(), planet.getImageRect(),planetCoordinates , null);
        }

        void  drawCircleAtOrbit(Canvas c, float centerX, float centerY, float orbitRadius, float angle, float circleRadius, Paint paint){
            float realX = (float)Math.cos(angle)*orbitRadius + centerX;
            float realY = (float)Math.sin(angle)*orbitRadius + centerY;
            c.drawCircle(realX, realY, circleRadius, paint);
        }

    }
}