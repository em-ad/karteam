package space.kheyrati.nanowatch;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nambimobile.widgets.efab.FabOption;

import java.util.Collections;
import java.util.List;

public class RequestListFragment extends Fragment implements RefreshCallback {

    private FabOption fabTraffic;
    private FabOption fabVacation;
    private FabOption fabMission;
    private RecyclerView recycler;
    private MyRequestListAdapter adapter;
    private KheyratiRepository repository;

    public RequestListFragment() {
        repository = new KheyratiRepository();
        // Required empty public constructor
    }

    public static RequestListFragment newInstance() {
        RequestListFragment fragment = new RequestListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_request_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        initListeners();
    }

    @Override
    public void onResume() {
        super.onResume();
        getItems();
    }

    private void getItems() {
        if(getContext() == null || repository == null){
            return;
        }
        repository.getMyRequests(MSharedPreferences.getInstance().getTokenHeader(getContext()), new ApiCallback() {
            @Override
            public void apiFailed(Object o) {
                MAlerter.show(getActivity(), "خطا", "در دریافت لیست درخواست ها خطایی پیش آمد");
            }

            @Override
            public void apiSucceeded(Object o) {
                List<RequestResponseModel> data = ((List<RequestResponseModel>) o);
                Collections.reverse(data);
                adapter.submitList(data);
                recycler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recycler.smoothScrollToPosition(0);
                    }
                }, 500);
            }
        });
    }

    private void initListeners() {
        fabVacation.setOnClickListener(view1 ->
                getChildFragmentManager()
                        .beginTransaction()
                        .add(R.id.root, new RequestFragment(this))
                        .addToBackStack(RequestFragment.class.getSimpleName())
                        .commit());
        fabMission.setOnClickListener(view1 ->
                getChildFragmentManager()
                        .beginTransaction()
                        .add(R.id.root, new RequestFragment(this))
                        .addToBackStack(RequestFragment.class.getSimpleName())
                        .commit());
        fabTraffic.setOnClickListener(view1 ->
                getChildFragmentManager()
                        .beginTransaction()
                        .add(R.id.root, new RequestFragment(this))
                        .addToBackStack(RequestFragment.class.getSimpleName())
                        .commit());
    }

    private void findViews(View view) {
        fabTraffic = view.findViewById(R.id.fabTraffic);
        fabVacation = view.findViewById(R.id.fabVacation);
        fabMission = view.findViewById(R.id.fabMission);
        recycler = view.findViewById(R.id.recycler);
        adapter = new MyRequestListAdapter();
        recycler.setAdapter(adapter);
    }

    @Override
    public void refresh() {
        getItems();
    }
}