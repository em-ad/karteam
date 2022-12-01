package space.kheyrati.nanowatch;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.auth0.android.jwt.JWT;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MSharedPreferences {

    private static MSharedPreferences instance;
    public static String PREF_KEY = "karteam_key";

    private boolean TEST = true;

    public static MSharedPreferences getInstance() {
        if (instance == null)
            instance = new MSharedPreferences();
        return instance;
    }

    public static void saveFcmToken(Context context, String token) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
        pref.edit().putString("fcm", token).apply();
    }

    public List<GeneralRequestListItem> getAllRequests(Context context) {
        ArrayList<GeneralRequestListItem> result = new ArrayList<>();
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
        String jsonRequests = pref.getString("requests", null);
        result.addAll(new Gson().fromJson(jsonRequests, result.getClass()));
        return result;
    }

    public void addRequest(Context context, GeneralRequestListItem item) {
        ArrayList<GeneralRequestListItem> result = new ArrayList<>();
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
        String jsonRequests = pref.getString("requests", null);
        result.addAll(new Gson().fromJson(jsonRequests, result.getClass()));
        result.add(item);
        pref.edit().putString("requests", new Gson().toJson(result)).apply();
    }

    public void saveToken(Context context, String token) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
        pref.edit().putString("token", token).apply();
    }

    public boolean hasToken(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
        if (!TEST)
            return pref.contains("token") && pref.getString("token", null) != null;
        return true;
    }

    public String getToken(Context context) {
        return context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE).getString("token", null);
    }

    public String getTokenHeader(Context context) {
        if (!TEST)
            return "Bearer " + context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE).getString("token", null);
        return "Bearer " + "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1aWQiOiI2MzRhOGIzNjZlNWRmOWVhNjkzMmZlNGMiLCJmaXJzdE5hbWUiOiJlbWFkIiwibGFzdE5hbWUiOiJtb2xsYWVpIiwicGhvbmVOdW1iZXIiOiIwOTEyMDMyNDc0MSIsImlhdCI6MTY2NzkyNTUyNX0.2YBcZaIjXF-6ae7H7xGPQof9lRwoOgJCWimmbqsgJHo";
    }

    public String getUserIdFromToken(Context context) {
        String rawToken = (!TEST) ? context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE).getString("token", "") :
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1aWQiOiI2MzRhOGIzNjZlNWRmOWVhNjkzMmZlNGMiLCJmaXJzdE5hbWUiOiJlbWFkIiwibGFzdE5hbWUiOiJtb2xsYWVpIiwicGhvbmVOdW1iZXIiOiIwOTEyMDMyNDc0MSIsImlhdCI6MTY2NzkyNTUyNX0.2YBcZaIjXF-6ae7H7xGPQof9lRwoOgJCWimmbqsgJHo";
        JWT jwt = new JWT(rawToken);
        return jwt.getClaims().get("uid").asString();
    }

    public String getNameFromToken(Context context) {
        String rawToken = (!TEST) ? context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE).getString("token", "") :
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1aWQiOiI2MzRhOGIzNjZlNWRmOWVhNjkzMmZlNGMiLCJmaXJzdE5hbWUiOiJlbWFkIiwibGFzdE5hbWUiOiJtb2xsYWVpIiwicGhvbmVOdW1iZXIiOiIwOTEyMDMyNDc0MSIsImlhdCI6MTY2NzkyNTUyNX0.2YBcZaIjXF-6ae7H7xGPQof9lRwoOgJCWimmbqsgJHo";
        JWT jwt = new JWT(rawToken);
        return jwt.getClaims().get("firstName").asString() + " " + jwt.getClaims().get("lastName").asString();
    }

    public void removeToken(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
        pref.edit().remove("token").commit();
    }

    public String whatIsLastTrafficEvent(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
        return pref.getString("last_event", "none");
    }

    public void saveLastTrafficEvent(Context context, String event) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
        pref.edit().putString("last_event", event).commit();
    }
}
