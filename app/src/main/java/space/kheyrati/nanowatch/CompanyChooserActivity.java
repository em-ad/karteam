package space.kheyrati.nanowatch;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.installations.FirebaseInstallations;

import java.util.List;

import space.kheyrati.nanowatch.api.KheyratiRepository;
import space.kheyrati.nanowatch.model.CompanyLocationResponseModel;
import space.kheyrati.nanowatch.model.CompanyResponseModel;

public class CompanyChooserActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private MyCompanyAdapter adapter;
    private ProgressBar progress;
    private AppCompatImageView ivEmpty;
    private TextView tvEmpty;
    private TextView tvLogout;
    private KheyratiRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_chooser);
        findViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        repository = new KheyratiRepository();
        getMyCompanies();
//        FirebaseInstallations.getInstance().getToken(true)
//                .addOnCompleteListener(task -> {
//                    if (!task.isSuccessful()) {
//                        return;
//                    }
//                    String token = task.getResult().getToken();
//                    Log.e("TAG", "FCM TOKEN: " + token);
//                    new KheyratiRepository().sendToken(MSharedPreferences.getInstance().getTokenHeader(getApplication()), token);
//                });
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        return;
                    }
                    String token = task.getResult().getToken();
                    Log.e("TAG", "FCM TOKEN: " + token);
                    new KheyratiRepository().sendToken(MSharedPreferences.getInstance().getTokenHeader(getApplication()), token);
                });
    }

    private void getMyCompanies() {
        progress.setVisibility(View.VISIBLE);
        repository.getMyCompanies(MSharedPreferences.getInstance().getTokenHeader(this), new ApiCallback() {
            @Override
            public void apiFailed(Object o) {
                progress.setVisibility(View.GONE);
                MAlerter.show(CompanyChooserActivity.this, "خطا", "در دریافت لیست شرکت های شما خطایی رخ داد");
            }

            @Override
            public void apiSucceeded(Object o) {
                progress.setVisibility(View.GONE);
                List<CompanyResponseModel> data = ((List<CompanyResponseModel>) o);
                if (adapter != null) {
                    adapter.setDataSet(data);
                }
                if (data == null || data.size() == 0) {
                    ivEmpty.setVisibility(View.VISIBLE);
                    tvEmpty.setVisibility(View.VISIBLE);
                } else {
                    ivEmpty.setVisibility(View.GONE);
                    tvEmpty.setVisibility(View.GONE);
                }
            }
        });
    }

    private void findViews() {
        recycler = findViewById(R.id.recycler);
        progress = findViewById(R.id.progress);
        ivEmpty = findViewById(R.id.ivEmpty);
        tvEmpty = findViewById(R.id.tvEmpty);
        tvLogout = findViewById(R.id.tvLogout);
        adapter = new MyCompanyAdapter(this::companyItemClicked);
        recycler.setAdapter(adapter);
        tvLogout.setOnClickListener(view -> {
            MSharedPreferences.getInstance().removeToken(CompanyChooserActivity.this);
            startActivity(new Intent(CompanyChooserActivity.this, SplashActivity.class));
            finishAffinity();
        });
    }

    private void companyItemClicked(CompanyResponseModel company) {
        MAlerter.show(this, "صبر کنید", "در حال دریافت مکان های مجاز " + company.getCompany().getName());
        progress.setVisibility(View.VISIBLE);
        MyApplication.company = company;
        MyApplication.role = company.getRole().toLowerCase();
        repository.getCompanyLocation(MSharedPreferences.getInstance().getTokenHeader(this), company.getCompany().getId(), new ApiCallback() {
            @Override
            public void apiFailed(Object o) {
                progress.setVisibility(View.GONE);
                MAlerter.show(CompanyChooserActivity.this, "خطا", "در دریافت اطلاعات مکان شرکت خطایی رخ داد");
            }

            @Override
            public void apiSucceeded(Object o) {
                progress.setVisibility(View.GONE);
                if (o == null || ((List<CompanyLocationResponseModel>) o).isEmpty()) {
                    MAlerter.show(CompanyChooserActivity.this, "خطا", "برای " + company.getCompany().getName() + " محلی تعیین نشده است");
                    return;
                }
                MyApplication.company.setLocation(((List<CompanyLocationResponseModel>) o));
                startActivity(new Intent(CompanyChooserActivity.this, MainActivity.class));
            }
        });

    }

    boolean exitPressed = false;

    CountDownTimer exitTimer = new CountDownTimer(1500, 1500) {
        @Override
        public void onTick(long l) {

        }

        @Override
        public void onFinish() {
            exitPressed = false;
        }
    };

    @SuppressLint("RestrictedApi")
    @Override
    public void onBackPressed() {
        if (!exitPressed) {
            exitPressed = true;
            exitTimer.start();
            MAlerter.show(this, "", "برای خروج دوباره دکمه بازگشت را لمس کنید");
        } else {
            super.onBackPressed();
        }
    }
}