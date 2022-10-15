package space.kheyrati.nanowatch;

import android.app.Activity;
import android.view.Gravity;

import androidx.core.content.res.ResourcesCompat;

import com.tapadoo.alerter.Alerter;

import java.util.Objects;

import space.kheyrati.nanowatch.R;

public class MAlerter {

   public static void show(Activity activity, String title, String msg) {
      if (msg == null)
         msg = "خطا";
      if(title == null)
         title = "نامشخص";

      Alerter.create(activity)
              .setIcon(android.R.drawable.ic_dialog_alert)
              .setTitle(title)
              .setText(msg)
              .setDuration(2000)
              .setTitleTypeface(Objects.requireNonNull(ResourcesCompat.getFont(activity, R.font.app_font)))
              .setTextTypeface(Objects.requireNonNull(ResourcesCompat.getFont(activity, R.font.app_font)))
              .setBackgroundColorRes(R.color.primary)
              .setTextAppearance(R.style.AppTheme_AlerterTextView_Base)
              .setContentGravity(Gravity.RIGHT)
              .showRightIcon(false)
              .show();
   }

}
