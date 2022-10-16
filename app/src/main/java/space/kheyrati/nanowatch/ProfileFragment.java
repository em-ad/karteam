package space.kheyrati.nanowatch;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProfileFragment extends Fragment {

    private RecyclerView recycler;
    private TrafficAdapter adapter;
    private KheyratiRepository repository;
    private TextView logout;

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
            MSharedPreferences.getInstance().removeToken(getContext());
            startActivity(new Intent(getContext(), SplashActivity.class));
            getActivity().finishAffinity();
        });
    }

    private void getMyLogs() {
        if (getContext() == null) return;
        repository.getLogs(MSharedPreferences.getInstance().getTokenHeader(getContext()),
                MSharedPreferences.getInstance().getUserIdFromToken(getContext()),
                new ApiCallback() {
                    @Override
                    public void apiFailed(Object o) {
                        MAlerter.show(getActivity(), "خطا", "در دریافت لیست رفت و آمدها خطایی رخ داد!");
                    }

                    @Override
                    public void apiSucceeded(Object o) {
                        List<UserLogResponseItem> data = (List<UserLogResponseItem>) o;
                        Collections.reverse(data);
                        adapter.setDataset(new ArrayList<>(data));
                    }
                });
    }

    private void findViews(View view) {
        recycler = view.findViewById(R.id.recycler);
        adapter = new TrafficAdapter();
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        logout = view.findViewById(R.id.logout);
    }


}