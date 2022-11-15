package space.kheyrati.nanowatch;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.List;

public class AttendeesFragment extends Fragment {

    private RecyclerView recycler;
    private AttendeesAdapter adapter;
    private KheyratiRepository repository;
    private AppCompatImageView refresh;
    private ProgressBar progress;

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
        refresh = view.findViewById(R.id.refresh);
        progress = view.findViewById(R.id.progress);
        adapter = new AttendeesAdapter();
        recycler.setAdapter(adapter);
        refresh.setOnClickListener(view1 -> getApi());
    }

    @Override
    public void onResume() {
        super.onResume();
        getApi();
    }

    private void getApi() {
        if (progress != null)
            progress.setVisibility(View.VISIBLE);
        if (refresh != null)
            refresh.setVisibility(View.GONE);
        AttendeesRequestModel model = new AttendeesRequestModel();
        model.setCompany(MyApplication.company.getCompany().getId());
        model.setDate(System.currentTimeMillis());
        repository.getAttendees(MSharedPreferences.getInstance().getTokenHeader(getContext()), model, new ApiCallback() {
                    @Override
                    public void apiFailed(Object o) {
                        MAlerter.show(getActivity(), "خطا", "در دریافت لیست حاضرین خطایی رخ داد");
                        refresh.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);
                    }

                    @Override
                    public void apiSucceeded(Object o) {
                        List<AttendeesResponseModel> data = ((List<AttendeesResponseModel>) o);
                        if (data.size() == 0) {
                            refresh.setVisibility(View.VISIBLE);
                            MAlerter.show(getActivity(), "خطا", "هنوز فردی ورود خود را امروز ثبت نکرده است");
                        } else refresh.setVisibility(View.GONE);
                        adapter.setDataSet(data);
                        progress.setVisibility(View.GONE);
                    }
                }
        );
    }
}