package space.kheyrati.nanowatch;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import space.kheyrati.nanowatch.api.KheyratiRepository;
import space.kheyrati.nanowatch.model.VersionResponseModel;

public class SplashActivity extends AppCompatActivity {

    public static final long SPLASH_TIME_MILLIS = 2500;
    long currentCode = -1;
    private KheyratiRepository repository;
    StateCheckDialog apiErrorDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        repository = new KheyratiRepository();
        currentCode = BuildConfig.VERSION_CODE;
        checkVersion();
    }

    private void checkVersion() {
        repository.getVersion(new ApiCallback() {
            @Override
            public void apiFailed(Object o) {
                new Handler().postDelayed(() -> continueApp(), 1000);
            }

            @Override
            public void apiSucceeded(Object o) {
                VersionResponseModel model = ((VersionResponseModel) o);
                if (Long.parseLong(model.getName()) > currentCode) {
                    new UpdateDialog(SplashActivity.this, model.getName()).show();
                } else {
                    new Handler().postDelayed(() -> continueApp(), 1000);
                }
            }
        });
    }

    private void continueApp() {
        if (MSharedPreferences.getInstance().hasToken(this)) {
            startActivity(new Intent(SplashActivity.this, CompanyChooserActivity.class));
        } else {
            startActivity(new Intent(SplashActivity.this, IntroActivity.class));
        }
        finish();
    }
}