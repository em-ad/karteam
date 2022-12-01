package space.kheyrati.nanowatch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.nambimobile.widgets.efab.FabOption;

import java.util.Collections;
import java.util.List;

import space.kheyrati.nanowatch.api.KheyratiRepository;
import space.kheyrati.nanowatch.model.RequestResponseModel;

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
        MAlerter.show(getActivity(), "توجه", "میتوانید برای ویرایش درخواست روی آن کلیک کنید");
    }

    @Override
    public void onResume() {
        super.onResume();
        getItems();
    }

    private void getItems() {
        if (getContext() == null || repository == null) {
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
                        .add(R.id.root, new RequestFragment(this, "vacation"))
                        .addToBackStack(RequestFragment.class.getSimpleName())
                        .commit());
        fabMission.setOnClickListener(view1 ->
                getChildFragmentManager()
                        .beginTransaction()
                        .add(R.id.root, new RequestFragment(this, "mission"))
                        .addToBackStack(RequestFragment.class.getSimpleName())
                        .commit());
        fabTraffic.setOnClickListener(view1 ->
                getChildFragmentManager()
                        .beginTransaction()
                        .add(R.id.root, new RequestFragment(this, "traffic"))
                        .addToBackStack(RequestFragment.class.getSimpleName())
                        .commit());
    }

    private void findViews(View view) {
        fabTraffic = view.findViewById(R.id.fabTraffic);
        fabVacation = view.findViewById(R.id.fabVacation);
        fabMission = view.findViewById(R.id.fabMission);
        recycler = view.findViewById(R.id.recycler);
        adapter = new MyRequestListAdapter(this::requestClicked);
        recycler.setAdapter(adapter);
    }

    private void requestClicked(RequestResponseModel item) {
        if (item.getStatus().equalsIgnoreCase("accept")) {
            MAlerter.show(getActivity(), "خطا", "این درخواست تایید شده و امکان ویرایش آن وجود ندارد");
            return;
        }
        if (item.getStatus().equalsIgnoreCase("reject")) {
            MAlerter.show(getActivity(), "خطا", "این درخواست رد شده و امکان ویرایش آن وجود ندارد");
            return;
        }
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.root, new RequestFragment(this, item))
                .addToBackStack(RequestFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public void refresh() {
        getItems();
    }
}