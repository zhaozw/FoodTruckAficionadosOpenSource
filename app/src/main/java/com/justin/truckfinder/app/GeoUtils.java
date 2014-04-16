package com.justin.truckfinder.app;

import android.hardware.GeomagneticField;
import android.location.Location;

/**
 * Created by justinmac on 4/16/14.
 */
public class GeoUtils {

    public GeomagneticField mGeomagneticField;
    public Location mLocation;

    public static float mod(float a, float b) {

        return (a % b + b) % b;
    }

    public static float getBearing(double latitudeStart, double longitudeStart, double latitudeEnd,
                                   double longitudeEnd) {
        latitudeStart = Math.toRadians(latitudeStart);
        longitudeStart = Math.toRadians(longitudeStart);
        latitudeEnd = Math.toRadians(latitudeEnd);
        longitudeEnd = Math.toRadians(longitudeEnd);

        double dLon = longitudeEnd - longitudeStart;

        double y = Math.sin(dLon) * Math.cos(latitudeEnd);
        double x = Math.cos(latitudeStart) * Math.sin(latitudeEnd) - Math.sin(latitudeStart)
                * Math.cos(latitudeEnd) * Math.cos(dLon);

        double bearing = Math.atan2(y, x);
        return mod((float) Math.toDegrees(bearing), 360.0f);

    }

    private void updateGeomagneticField() {
        mGeomagneticField = new GeomagneticField((float) mLocation.getLatitude(),
                (float) mLocation.getLongitude(), (float) mLocation.getAltitude(),
                mLocation.getTime());

    }

    private float computeTrueNorth(float heading) {
        if (mGeomagneticField != null) {
            return heading + mGeomagneticField.getDeclination();
        } else {
            return heading;
        }
    }



}
