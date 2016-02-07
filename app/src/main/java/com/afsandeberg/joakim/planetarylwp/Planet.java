package com.afsandeberg.joakim.planetarylwp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by Joakim on 2016-02-06.
 */
public class Planet extends  PlanetWallpaper{
    private Paint color;
    private float planetRadius;
    private float orbitRadius;
    private float orbitTime;
    private Bitmap image;
    private Rect imageRect;


    public Planet(int color, float planetRadius, float orbitRadius, float orbitTime,int imageAsset, Resources res) {
        this.color = new Paint();
        this.color.setColor(color);
        this.planetRadius = planetRadius * PlanetEngine.PLANET_RADIUS;
        this.orbitRadius = orbitRadius;
        this.orbitTime = 1/orbitTime;
        BitmapFactory.Options options  = new BitmapFactory.Options();
        options.inSampleSize = (int)(1/planetRadius * PlanetEngine.PLANET_RADIUS);
        this.image = BitmapFactory.decodeResource(res, imageAsset, options);
        imageRect = new Rect(0,0, (int)planetRadius, (int)planetRadius);
    }

    public Rect getImageRect(){
        return imageRect;
    }
    public Paint getColor() {
        return color;
    }

    public Bitmap getImage(){
        return image;
    }

    public float getPlanetRadius() {
        return planetRadius;
    }

    public float getOrbitRadius() {
        return orbitRadius;
    }

    public float getOrbitTime() {
        return orbitTime;
    }
}
