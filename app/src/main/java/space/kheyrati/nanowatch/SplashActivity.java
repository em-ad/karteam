package space.kheyrati.nanowatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    public static final long SPLASH_TIME_MILLIS = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        MAlerter.show(this, "خوش آمدید", "نرم افزار عبور و مرور عماد");
        new Handler().postDelayed(() -> {
            if(MSharedPreferences.getInstance().hasToken(this)){
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
            finish();
        }, SPLASH_TIME_MILLIS);
    }
}