package space.kheyrati.nanowatch;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProfileFragment extends Fragment {

    private RecyclerView recycler;
    private TrafficAdapter adapter;
    private KheyratiRepository repository;
    private TextView logout;
    private BarChart chart;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        repository = new KheyratiRepository();
        getMyLogs();
        logout.setOnClickListener(v -> {
            if (getContext() == null || getActivity() == null) return;
            new AreYouShortDialog(getContext(), null, null, null, new AreYouShortCallback() {

                @Override
                public void accept() {
                    MSharedPreferences.getInstance().removeToken(getContext());
                    startActivity(new Intent(getContext(), SplashActivity.class));
                    getActivity().finishAffinity();
                }

                @Override
                public void reject() {

                }

                @Override
                public void dismiss() {

                }
            }).show();
        });
        view.findViewById(R.id.changeCompany).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
    }

    private void getMyLogs() {
        if (getContext() == null) return;
        repository.getLogs(MSharedPreferences.getInstance().getTokenHeader(getContext()),
                new ApiCallback() {
                    @Override
                    public void apiFailed(Object o) {
                        MAlerter.show(getActivity(), "خطا", "در دریافت لیست رفت و آمدها خطایی رخ داد!");
                    }

                    @Override
                    public void apiSucceeded(Object o) {
                        List<UserLogResponseItem> data = (List<UserLogResponseItem>) o;
                        Collections.reverse(data);
                        try {
                            adapter.setDataset(new ArrayList<>(data));
                        } catch (Exception e) {
                            MAlerter.show(getActivity(), "خطا", "در دریافت لیست رفت و آمدها خطایی رخ داد!");
                        }
                        try {
                            setNewDataToChart(data);
                        } catch (Exception e) {
                            MAlerter.show(getActivity(), "خطا", "در دریافت لیست رفت و آمدها خطایی رخ داد!");
                        }
                    }
                });
    }

    private void setNewDataToChart(List<UserLogResponseItem> data) {
        int limit = 0;
        List<BarEntry> entries = new ArrayList<>();
        List<DailyPresence> presenceList = DailyPresence.fromLogs(data);
        for (DailyPresence item : presenceList) {
            float value = Math.min(24, Math.max(item.getPresence() / 3600, 0));
            entries.add(new BarEntry(item.getDay(), value));
            if (value > 0)
                entries.add(new BarEntry(item.getDay(), 8));
            limit++;
            if (limit == 30) break;
        }
        BarDataSet set = new BarDataSet(entries, "مجموع میزان حضور در مجموعه در یک ماه اخیر (ساعت)");
        set.setColors(R.color.green_dark);
        set.setValueTextColor(R.color.black);
        set.setDrawValues(false);
        BarData barData = new BarData(set);
        barData.setValueTextSize(13);
        barData.setValueTypeface(Typeface.SANS_SERIF);
        chart.setData(barData);
        chart.invalidate();
    }

    private void findViews(View view) {
        recycler = view.findViewById(R.id.recycler);
        adapter = new TrafficAdapter();
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        logout = view.findViewById(R.id.logout);
        chart = view.findViewById(R.id.chart);
        view.findViewById(R.id.fab).setOnClickListener(view1 -> MAlerter.show(getActivity(), "در حال پیاده سازی", "این قابلیت هنوز آماده نشده است"));
    }


}