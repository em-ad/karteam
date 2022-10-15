package space.kheyrati.nanowatch;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MSharedPreferences {

   private static MSharedPreferences instance;
   public static String PREF_KEY = "karteam_key";

   public static MSharedPreferences getInstance() {
      if(instance == null)
         instance = new MSharedPreferences();
      return instance;
   }

   public List<GeneralRequestListItem> getAllRequests(Context context){
      ArrayList<GeneralRequestListItem> result = new ArrayList<>();
      SharedPreferences pref = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
      String jsonRequests = pref.getString("requests", null);
      result.addAll(new Gson().fromJson(jsonRequests, result.getClass()));
      return result;
   }

   public void addRequest(Context context, GeneralRequestListItem item){
      ArrayList<GeneralRequestListItem> result = new ArrayList<>();
      SharedPreferences pref = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
      String jsonRequests = pref.getString("requests", null);
      result.addAll(new Gson().fromJson(jsonRequests, result.getClass()));
      result.add(item);
      pref.edit().putString("requests", new Gson().toJson(result)).apply();
   }

    public void saveToken(Context context, String token) {
    }
}
