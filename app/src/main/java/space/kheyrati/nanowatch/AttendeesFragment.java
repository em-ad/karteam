package space.kheyrati.nanowatch;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class AttendeesFragment extends Fragment {

    private RecyclerView recycler;
    private AttendeesAdapter adapter;
    private KheyratiRepository repository;

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
        if(MyApplication.role != null){
            if(MyApplication.role.equalsIgnoreCase("employee")){
                view.findViewById(R.id.blocker).setVisibility(View.VISIBLE);
                return;
            } else view.findViewById(R.id.blocker).setVisibility(View.GONE);
        }
        repository = new KheyratiRepository();
        recycler = view.findViewById(R.id.recycler);
        adapter = new AttendeesAdapter();
        recycler.setAdapter(adapter);
        AttendeesRequestModel model = new AttendeesRequestModel();
        model.setCompany(MyApplication.company.getCompany().getId());
        model.setDate(System.currentTimeMillis());
        repository.getAttendees(MSharedPreferences.getInstance().getTokenHeader(getContext()), model, new ApiCallback() {
                    @Override
                    public void apiFailed(Object o) {
                        MAlerter.show(getActivity(), "خطا", "در دریافت لیست حاضرین خطایی رخ داد");
                    }

                    @Override
                    public void apiSucceeded(Object o) {
                        List<AttendeesResponseModel> data = ((List<AttendeesResponseModel>) o);
                        adapter.setDataSet(data);
                    }
                }
        );
    }
}