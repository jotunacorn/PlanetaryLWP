package com.afsandeberg.joakim.planetarylwp;

import android.graphics.Paint;

/**
 * Created by Joakim on 2016-02-06.
 */
public class Planet {
    private Paint color;
    private float planetRadius;
    private float orbitRadius;
    private float orbitTime;

    public Planet(int color, float planetRadius, float orbitRadius, float orbitTime) {
        this.color = new Paint();
        this.color.setColor(color);
        this.planetRadius = planetRadius;
        this.orbitRadius = orbitRadius;
        this.orbitTime = 1/orbitTime;
    }

    public Paint getColor() {
        return color;
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
