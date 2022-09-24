package space.kheyrati.nanowatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;

import space.kheyrati.nanowatch.utils.MAlerter;

public class SplashActivity extends AppCompatActivity {

    public static final long SPLASH_TIME_MILLIS = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        MAlerter.show(this, "خوش آمدید", "نرم افزار مدیریت رفت و آمد عماد");
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }, SPLASH_TIME_MILLIS);
    }
}