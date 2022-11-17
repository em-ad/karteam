package space.kheyrati.nanowatch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.Collections;
import java.util.List;


public class NewsFragment extends Fragment {

    private RecyclerView recycler;
    private ProgressBar progress;
    private KheyratiRepository repository;
    private NewsAdapter adapter;
    private ExtendedFloatingActionButton fab;

    public NewsFragment() {
    }

    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
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
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        repository = new KheyratiRepository();
        recycler = view.findViewById(R.id.recycler);
        progress = view.findViewById(R.id.progress);
        fab = view.findViewById(R.id.fab);
        ((TextView) view.findViewById(R.id.title)).setText("صندوق پیام");
        view.findViewById(R.id.arrow).setOnClickListener(view1 -> getParentFragmentManager().popBackStackImmediate());
        adapter = new NewsAdapter();
        recycler.setAdapter(adapter);
        fab.setOnClickListener(view12 -> {
            if (getContext() == null) return;
            SendNewsDialog dialog = new SendNewsDialog(getContext());
            dialog.show();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getNews();
        if (MyApplication.role != null) {
            if (MyApplication.role.equalsIgnoreCase("employee")) {
                fab.setVisibility(View.GONE);
                return;
            } else fab.setVisibility(View.VISIBLE);
        }
    }

    private void getNews() {
        if (repository == null)
            repository = new KheyratiRepository();
        progress.setVisibility(View.VISIBLE);
        repository.getNews(MSharedPreferences.getInstance().getTokenHeader(getContext()),
                MyApplication.company.getCompany().getId(), new ApiCallback() {
                    @Override
                    public void apiFailed(Object o) {
                        progress.setVisibility(View.GONE);
                        MAlerter.show(getActivity(), "خطا", "در دریافت پیام ها خطایی رخ داد");
                    }

                    @Override
                    public void apiSucceeded(Object o) {
                        progress.setVisibility(View.GONE);
                        List<NewsResponseModel> data = ((List<NewsResponseModel>) o);
                        Collections.reverse(data);
                        adapter.setData(data);
                    }
                });
    }
}