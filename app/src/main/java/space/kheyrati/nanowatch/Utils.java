package space.kheyrati.nanowatch;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public static String getTime(long millisUntilFinished) {
        int sec = (int) (millisUntilFinished / 1000);
        int min = sec / 60;
        int sec2 = sec % 60;
        return ((sec2 < 10) ? "0" + sec2 : sec2) + " : " + min;
    }

    public static long getTimeStamp(String isoDate){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date startDate = new Date();
        try{
            startDate = format.parse(isoDate);
        } catch (Exception e){
            e.printStackTrace();
        }
        return startDate.getTime();
    }
}
