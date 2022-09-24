package space.kheyrati.nanowatch;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {

   private static SharedPreferences manager;

   public static SharedPreferences getInstance(Context context){
      if(manager == null)
         manager = context.getSharedPreferences(MConstants.Strings.APP_NAME, Context.MODE_PRIVATE);
      return manager;
   }

}
