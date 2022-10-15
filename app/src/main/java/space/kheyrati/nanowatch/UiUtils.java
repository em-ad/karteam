package space.kheyrati.nanowatch;

public class UiUtils {
    private String getTime(long millisUntilFinished) {
        int sec = (int) (millisUntilFinished / 1000);
        int min = sec / 60;
        int sec2 = sec % 60;
        return ((sec2 < 10) ? "0" + sec2 : sec2) + " : " + min;
    }
}
