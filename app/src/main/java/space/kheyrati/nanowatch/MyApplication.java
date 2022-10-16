package space.kheyrati.nanowatch;

import android.app.Application;
import android.location.Location;


public class MyApplication extends Application {

    public static Location lastLocation;

    public static final double sattariLat = 35.7015075;
    public static final double sattariLon = 51.353377;

    public static boolean locationValid() {
        if (lastLocation == null) return false;
        return isDistanceValid(lastLocation) < 3.5;
    }

    public static double isDistanceValid(Location point1) {
        double distance = Math.sqrt(
                Math.pow(point1.getLatitude() - sattariLat, 2) +
                        Math.pow(point1.getLongitude() - sattariLon, 2));
        return distance * 100000;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
