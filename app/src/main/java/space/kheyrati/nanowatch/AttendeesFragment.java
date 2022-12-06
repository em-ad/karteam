package space.kheyrati.nanowatch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

import space.kheyrati.nanowatch.api.KheyratiRepository;
import space.kheyrati.nanowatch.model.AttendeesRequestModel;
import space.kheyrati.nanowatch.model.AttendeesWithLogResponseModel;

public class AttendeesFragment extends Fragment {

    private RecyclerView recycler;
    private AttendeesAdapter adapter;
    private KheyratiRepository repository;
    private AppCompatImageView refresh;
    private ProgressBar progress;
    private SwipeRefreshLayout swipe;
    private Chip absentChip;
    private Chip presentChip;
    private List<AttendeesWithLogResponseModel> originalList = new ArrayList<>();

    public AttendeesFragment() {
    }

    public static AttendeesFragment newInstance() {
        AttendeesFragment fragment = new AttendeesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_attendees, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (MyApplication.role != null) {
            if (MyApplication.role.equalsIgnoreCase("employee")) {
                view.findViewById(R.id.blocker).setVisibility(View.VISIBLE);
                return;
            } else view.findViewById(R.id.blocker).setVisibility(View.GONE);
        }
        repository = new KheyratiRepository();
        recycler = view.findViewById(R.id.recycler);
        presentChip = view.findViewById(R.id.chipPresent);
        absentChip = view.findViewById(R.id.chipAbsent);
        refresh = view.findViewById(R.id.refresh);
        progress = view.findViewById(R.id.progress);
        swipe = view.findViewById(R.id.swipe);
        adapter = new AttendeesAdapter(this::itemClicked);
        recycler.setAdapter(adapter);
        refresh.setOnClickListener(view1 -> getApi());
        swipe.setOnRefreshListener(this::getApi);
        presentChip.setOnCheckedChangeListener((compoundButton, b) -> handleListItems());
        absentChip.setOnCheckedChangeListener((compoundButton, b) -> handleListItems());
    }

    private void handleListItems() {
        boolean showPresent = presentChip.isChecked();
        boolean showAbsent = absentChip.isChecked();
        if(!showPresent && !showAbsent){
            showAbsent = true;
            showPresent = true;
        }
        List<AttendeesWithLogResponseModel> list = originalList;
        if(list == null) list = new ArrayList<>();
        List<AttendeesWithLogResponseModel> filtered = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            if(showAbsent && (list.get(i).getLastState().equalsIgnoreCase("absent") || list.get(i).getLastState().equalsIgnoreCase("exit"))){
                filtered.add(list.get(i));
            }
            if(showPresent && list.get(i).getLastState().equalsIgnoreCase("enter")){
                filtered.add(list.get(i));
            }
        }
        adapter.setDataSet(filtered);
    }

    private void itemClicked(AttendeesWithLogResponseModel item) {
        new AttendeeLogDetailsDialog(getContext(), item).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        getApi();
    }

    private void getApi() {
        swipe.setRefreshing(true);
        if (repository == null)
            return;
        if (progress != null)
            progress.setVisibility(View.VISIBLE);
        if (refresh != null)
            refresh.setVisibility(View.GONE);
        AttendeesRequestModel model = new AttendeesRequestModel();
        model.setCompany(MyApplication.company.getCompany().getId());
        model.setDate(System.currentTimeMillis());
        repository.getAttendeesWithLog(MSharedPreferences.getInstance().getTokenHeader(getContext()), model, new ApiCallback() {
                    @Override
                    public void apiFailed(Object o) {
                        MAlerter.show(getActivity(), "خطا", "در دریافت لیست حاضرین خطایی رخ داد");
                        refresh.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);
                        swipe.setRefreshing(false);
                    }

                    @Override
                    public void apiSucceeded(Object o) {
                        List<AttendeesWithLogResponseModel> data = ((List<AttendeesWithLogResponseModel>) o);
                        originalList = data;
                        if (data.size() == 0) {
                            refresh.setVisibility(View.VISIBLE);
                            MAlerter.show(getActivity(), "خطا", "هنوز فردی ورود خود را امروز ثبت نکرده است");
                        } else refresh.setVisibility(View.GONE);
                        handleListItems();
                        progress.setVisibility(View.GONE);
                        swipe.setRefreshing(false);
                    }
                }
        );
    }
}