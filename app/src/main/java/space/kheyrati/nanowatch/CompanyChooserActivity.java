package space.kheyrati.nanowatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

public class CompanyChooserActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private MyCompanyAdapter adapter;
    private ProgressBar progress;
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
    }

    private void getMyCompanies() {
        progress.setVisibility(View.VISIBLE);
        repository.getMyCompanies(MSharedPreferences.getInstance().getTokenHeader(this), new ApiCallback() {
            @Override
            public void apiFailed(Object o) {
                MAlerter.show(CompanyChooserActivity.this, "خطا", "در دریافت لیست شرکت های شما خطایی رخ داد");
            }

            @Override
            public void apiSucceeded(Object o) {
                progress.setVisibility(View.GONE);
                List<CompanyResponseModel> data = ((List<CompanyResponseModel>) o);
                if(adapter != null){
                    adapter.setDataSet(data);
                }
            }
        });
    }

    private void findViews() {
        recycler = findViewById(R.id.recycler);
        progress = findViewById(R.id.progress);
        adapter = new MyCompanyAdapter(this::companyItemClicked);
        recycler.setAdapter(adapter);
    }

    private void companyItemClicked(CompanyResponseModel company) {
        MAlerter.show(this, "صبر کنید", "در حال دریافت مکان های مجاز..." + company.getName());
        progress.setVisibility(View.VISIBLE);
        MyApplication.company = company;
        repository.getCompanyLocation(MSharedPreferences.getInstance().getTokenHeader(this), company.getId(), new ApiCallback() {
            @Override
            public void apiFailed(Object o) {
                progress.setVisibility(View.GONE);
                MAlerter.show(CompanyChooserActivity.this, "خطا", "در دریافت اطلاعات مکان شرکت خطایی رخ داد");
            }

            @Override
            public void apiSucceeded(Object o) {
                progress.setVisibility(View.GONE);
                MyApplication.company.setLocation(((List<CompanyLocationResponseModel>) o));
                startActivity(new Intent(CompanyChooserActivity.this, MainActivity.class));
            }
        });

    }
}