package space.kheyrati.nanowatch;

import android.app.Application;
import android.location.Location;
import android.util.Log;

import java.util.Locale;


public class MyApplication extends Application {

    public static Location lastLocation;
    public static CompanyResponseModel company;
    public static String role = "employee";
    public static boolean isIn;

    public static boolean locationValid() {
        if (lastLocation == null || company == null || company.getLocation() == null || company.getLocation().isEmpty()) return false;
        double distance = Integer.MAX_VALUE;
        double radius = Integer.MIN_VALUE;
        for (int i = 0; i < company.getLocation().size(); i++) {
            if(distanceOf(lastLocation, company.getLocation().get(i)) < distance){
                distance = distanceOf(lastLocation, company.getLocation().get(i));
            }
            if(company.getLocation().get(i).getRadius() > radius)
                radius = company.getLocation().get(i).getRadius();
        }
        return distance < radius;
    }

    public static double distanceOf(Location point1, CompanyLocationResponseModel point2) {
        if(company == null || company.getLocation() == null || company.getLocation().isEmpty())
            return Integer.MAX_VALUE;
        double distance = Math.sqrt(
                Math.pow(point1.getLatitude() - point2.getLon(), 2) +
                        Math.pow(point1.getLongitude() - point2.getLat(), 2));
        return distance * 100000;
    }

    public static String getNearestCompanyLocationUri() {
        if (lastLocation == null || company == null || company.getLocation() == null || company.getLocation().isEmpty()) return null;
        double distance = Integer.MAX_VALUE;
        int answer = 0;
        for (int i = 0; i < company.getLocation().size(); i++) {
            if(distanceOf(lastLocation, company.getLocation().get(i)) < distance){
                distance = distanceOf(lastLocation, company.getLocation().get(i));
                answer = i;
            }
        }
        return String.format(Locale.ENGLISH, "geo:0,0?q=%f,%f(نزدیک ترین مکان تعیین شده شرکت)",
                MyApplication.company.getLocation().get(answer).getLon(), MyApplication.company.getLocation().get(answer).getLat());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isIn = MSharedPreferences.getInstance().whatIsLastTrafficEvent(this).equals("enter");
    }
}
