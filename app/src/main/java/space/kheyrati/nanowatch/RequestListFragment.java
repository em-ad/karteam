package space.kheyrati.nanowatch;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nambimobile.widgets.efab.FabOption;

public class RequestListFragment extends Fragment {

    private FabOption fabTraffic;
    private FabOption fabVacation;
    private FabOption fabMission;

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
        fabTraffic = view.findViewById(R.id.fabTraffic);
        fabVacation = view.findViewById(R.id.fabVacation);
        fabMission = view.findViewById(R.id.fabMission);
        fabVacation.setOnClickListener(view1 ->
                getChildFragmentManager()
                        .beginTransaction()
                        .add(R.id.root, new RequestFragment())
                        .addToBackStack(RequestFragment.class.getSimpleName())
                        .commit());
    }
}