package space.kheyrati.nanowatch;

import android.app.Application;
import android.location.Location;
import android.util.Log;


public class MyApplication extends Application {

    public static Location lastLocation;
    public static CompanyResponseModel company;

    public static boolean locationValid() {
        if (lastLocation == null || company == null || company.getLocation() == null) return false;
        if (BuildConfig.DEBUG)
            Log.e("MyApplication", "locationValid: " + isDistanceValid(lastLocation));
        return isDistanceValid(lastLocation) < company.getLocation().get(0).getRadius();
    }

    public static double isDistanceValid(Location point1) {
        if(company == null || company.getLocation() == null || company.getLocation().isEmpty())
            return Integer.MAX_VALUE;
        double distance = Math.sqrt(
                Math.pow(point1.getLatitude() - company.getLocation().get(0).getLat(), 2) +
                        Math.pow(point1.getLongitude() - company.getLocation().get(0).getLon(), 2));
        return distance * 100000;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
