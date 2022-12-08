package space.kheyrati.nanowatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ir.hamsaa.persiandatepicker.util.PersianCalendar;
import space.kheyrati.nanowatch.model.UserLogResponseModel;

public class DailyPresence {

    private float day;
    private float presence;

    public DailyPresence(float day, float presence) {
        this.day = day;
        this.presence = presence;
    }

    //hanoo maghzam kar mikone zaheran
    public static List<DailyPresence> fromLogs(List<UserLogResponseModel> data) {
        List<DailyPresence> result = new ArrayList<>();
        Map<String, Long> presence = new HashMap<>();
        for (int i = data.size() - 1; i >= 0; i--) {
            UserLogResponseModel log = data.get(i);
            PersianCalendar cal = new PersianCalendar(log.getDate());
            String day = cal.getPersianShortDate();
            if (presence.containsKey(day)) continue;
            List<UserLogResponseModel> dailyLogs = getDailyLogs(data, day);
            Long totalPresence = 0L;
            for (int j = 0; j < dailyLogs.size(); j++) {
                PersianCalendar incal = new PersianCalendar(dailyLogs.get(j).getDate());
                String rawTime = incal.getPersianShortDateTime().substring(incal.getPersianShortDateTime().indexOf(" ") + 1);
                long seconds = Long.parseLong(rawTime.split(":")[0]) * 3600 +
                        Long.parseLong(rawTime.split(":")[1]) * 60 + Long.parseLong(rawTime.split(":")[2]);
                if (dailyLogs.get(j).getType().equalsIgnoreCase("enter")) {
                    if (presence.containsKey(day)) {
                        presence.put(day, presence.get(day) - seconds);
                    } else {
                        presence.put(day, -1 * seconds);
                    }
                } else {
                    if (presence.containsKey(day)) {
                        presence.put(day, presence.get(day) + seconds);
                    } else {
                        presence.put(day, seconds);
                    }
                }
            }
            Iterator<Map.Entry<String, Long>> iterator = presence.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Long> entry = iterator.next();
                result.add(new DailyPresence(Float.parseFloat(entry.getKey().split("/")[2]), entry.getValue()));
            }
//            presence.put(day, totalPresence);
        }
        return result;
    }

    private static List<UserLogResponseModel> getDailyLogs(List<UserLogResponseModel> data, String day) {
        List<UserLogResponseModel> result = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            if (new PersianCalendar(data.get(i).getDate()).getPersianShortDate().equals(day)) {
                result.add(data.get(i));
            }
        }
        return result;
    }

    public float getDay() {
        return day;
    }

    public void setDay(float day) {
        this.day = day;
    }

    public float getPresence() {
        return presence;
    }

    public void setPresence(float presence) {
        this.presence = presence;
    }
}
