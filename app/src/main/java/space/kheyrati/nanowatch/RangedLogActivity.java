package space.kheyrati.nanowatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import ir.hamsaa.persiandatepicker.api.PersianPickerDate;
import ir.hamsaa.persiandatepicker.api.PersianPickerListener;
import ir.hamsaa.persiandatepicker.util.PersianCalendar;
import space.kheyrati.nanowatch.api.KheyratiRepository;

public class RangedLogActivity extends AppCompatActivity {

    private TextView tvStart;
    private TextView tvEnd;
    private TextView tvSearch;
    private KheyratiRepository repository;
    private ProgressBar progress;
    private RecyclerView recycler;
    private TrafficAdapter adapter;
    private long start;
    private long end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranged_log);
        repository = new KheyratiRepository();
        findViews();
        initListeners();
    }

    private void initListeners() {
        tvStart.setOnClickListener(view -> {
            new PersianDatePickerDialog(this)
                    .setListener(new PersianPickerListener() {
                        @Override
                        public void onDateSelected(PersianPickerDate persianPickerDate) {
                            tvStart.setText(persianPickerDate.getPersianLongDate());
                            PersianCalendar cal = new PersianCalendar();
                            cal.setPersianDate(persianPickerDate.getPersianYear(), persianPickerDate.getPersianMonth(), persianPickerDate.getPersianDay());
                            cal.set(PersianCalendar.HOUR, 0);
                            cal.set(PersianCalendar.MINUTE, 0);
                            cal.set(PersianCalendar.SECOND, 0);
                            start = cal.getTimeInMillis();
                        }

                        @Override
                        public void onDismissed() {
                        }
                    })
                    .setPickerBackgroundDrawable(R.drawable.bottom_rounded_purple)
                    .setTypeFace(ResourcesCompat.getFont(this, R.font.app_font))
                    .setInitDate(System.currentTimeMillis())
                    .show();
        });
        tvEnd.setOnClickListener(view -> {
            new PersianDatePickerDialog(this)
                    .setListener(new PersianPickerListener() {
                        @Override
                        public void onDateSelected(PersianPickerDate persianPickerDate) {
                            tvEnd.setText(persianPickerDate.getPersianLongDate());
                            PersianCalendar cal = new PersianCalendar();
                            cal.setPersianDate(persianPickerDate.getPersianYear(), persianPickerDate.getPersianMonth(), persianPickerDate.getPersianDay());
                            cal.set(PersianCalendar.HOUR, 0);
                            cal.set(PersianCalendar.MINUTE, 0);
                            cal.set(PersianCalendar.SECOND, 0);
                            end = cal.getTimeInMillis();
                        }

                        @Override
                        public void onDismissed() {
                        }
                    })
                    .setPickerBackgroundDrawable(R.drawable.bottom_rounded_purple)
                    .setTypeFace(ResourcesCompat.getFont(this, R.font.app_font))
                    .setInitDate(System.currentTimeMillis())
                    .show();
        });
        tvSearch.setOnClickListener(view -> {
            if(end == 0 || start == 0){
                MAlerter.show(this, "خطا", "لطفا هر دو بازه ابتدا و انتها را مشخص کنید");
            } else {
                progress.setVisibility(View.VISIBLE);
                repository.getLogs(MSharedPreferences.getInstance().getTokenHeader(this), new ApiCallback() {
                    @Override
                    public void apiFailed(Object o) {
                        progress.setVisibility(View.GONE);
                        MAlerter.show(RangedLogActivity.this, "خطا", "در دریافت لیست عبور و مرور خطایی رخ داد");
                    }

                    @Override
                    public void apiSucceeded(Object o) {
                        progress.setVisibility(View.GONE);
                        List<UserLogResponseItem> data = (List<UserLogResponseItem>) o;
                        Collections.reverse(data);
                        try {
                            adapter.setRange(start, end);
                            adapter.setDataset(new ArrayList<>(data));
                        } catch (Exception e) {
                            MAlerter.show(RangedLogActivity.this, "خطا", "در دریافت لیست رفت و آمدها خطایی رخ داد!");
                        }
                    }
                });
            }
        });
    }

    private void findViews() {
        findViewById(R.id.arrow).setOnClickListener(view -> onBackPressed());
        ((TextView) findViewById(R.id.title)).setText("نمایش بازه ای عبور و مرور ها");
        tvStart = findViewById(R.id.tvStart);
        tvEnd = findViewById(R.id.tvEnd);
        tvSearch = findViewById(R.id.tvSearch);
        progress = findViewById(R.id.progress);
        recycler = findViewById(R.id.recycler);
        adapter = new TrafficAdapter();
        recycler.setAdapter(adapter);
    }
}