package space.kheyrati.nanowatch;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nambimobile.widgets.efab.FabOption;

import java.util.ArrayList;

public class RequestListFragment extends Fragment {

    private FabOption fabTraffic;
    private FabOption fabVacation;
    private FabOption fabMission;
    private RecyclerView recycler;
    private RequestListAdapter adapter;

    public RequestListFragment() {
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
        mockItems();
    }

    private void mockItems() {
        ArrayList<GeneralRequestListItem> requests = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            GeneralRequestListItem item = new GeneralRequestListItem();
            int type = (int) ((Math.random() * 1000) % 3);
            int status = (int) ((Math.random() * 1000) % 3);
            switch (type){
                case 0:
                    item.setRequestType(GeneralRequestListItem.RequestType.MISSION);
                    break;
                case 1:
                    item.setRequestType(GeneralRequestListItem.RequestType.TRAFFIC);
                    break;
                case 2:
                    item.setRequestType(GeneralRequestListItem.RequestType.VACATION);
                    break;
            }
            switch (status){
                case 0:
                    item.setRequestStatus(GeneralRequestListItem.RequestStatus.PENDING);
                    break;
                case 1:
                    item.setRequestStatus(GeneralRequestListItem.RequestStatus.FAIL);
                    break;
                case 2:
                    item.setRequestStatus(GeneralRequestListItem.RequestStatus.SUCCESS);
                    break;
            }
            item.setId(String.valueOf((int)((Math.random() * 1000) % 100)));
            item.setTitle("آیتم تستی");
            item.setDate("12/12/1400");
            item.setTime("1 روز");
            requests.add(item);
        }
        adapter.submitList(requests);
    }

    private void initListeners() {
        fabVacation.setOnClickListener(view1 ->
                getChildFragmentManager()
                        .beginTransaction()
                        .add(R.id.root, new RequestFragment())
                        .addToBackStack(RequestFragment.class.getSimpleName())
                        .commit());
    }

    private void findViews(View view) {
        fabTraffic = view.findViewById(R.id.fabTraffic);
        fabVacation = view.findViewById(R.id.fabVacation);
        fabMission = view.findViewById(R.id.fabMission);
        recycler = view.findViewById(R.id.recycler);
        adapter = new RequestListAdapter();
        recycler.setAdapter(adapter);
    }
}