package space.kheyrati.nanowatch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import space.kheyrati.nanowatch.api.KheyratiRepository;
import space.kheyrati.nanowatch.model.RequestResponseModel;

public class CartableFragment extends Fragment implements RequestCallback {

    private RecyclerView recyclerView;
    private OthersRequestListAdapter adapter;
    private ProgressBar progress;
    private View blocker;
    private KheyratiRepository repository = new KheyratiRepository();

    public CartableFragment() {
        // Required empty public constructor
    }

    public static CartableFragment newInstance() {
        CartableFragment fragment = new CartableFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cartable, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler);
        progress = view.findViewById(R.id.progress);
        blocker = view.findViewById(R.id.blocker);
        adapter = new OthersRequestListAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        getItems();
    }

    private void getItems() {
        if (MyApplication.role != null) {
            if (MyApplication.role.equalsIgnoreCase("employee")) {
                blocker.setVisibility(View.VISIBLE);
                return;
            } else blocker.setVisibility(View.GONE);
        }
        progress.setVisibility(View.VISIBLE);
        repository.getAllRequests(MSharedPreferences.getInstance().getTokenHeader(getContext()), new ApiCallback() {
            @Override
            public void apiFailed(Object o) {
                progress.setVisibility(View.GONE);
                MAlerter.show(getActivity(), "خطا", "در دریافت اطلاعات خطایی رخ داد");
            }

            @Override
            public void apiSucceeded(Object o) {
                progress.setVisibility(View.GONE);
                List<RequestResponseModel> data = ((List<RequestResponseModel>) o);
                Collections.reverse(data);
                adapter.submitList(data);
                recyclerView.postDelayed(() -> recyclerView.smoothScrollToPosition(0), 500);
            }
        });
    }

    @Override
    public void onAccept(RequestResponseModel model) {
        new AreYouShortDialogWithMessage(getContext(), null, null, null, new AreYouShortCallback() {
            @Override
            public void accept() {
                progress.setVisibility(View.VISIBLE);
                repository.acceptRequest(MSharedPreferences.getInstance().getTokenHeader(getContext()), model, new ApiCallback() {

                    @Override
                    public void apiFailed(Object o) {
                        progress.setVisibility(View.GONE);
                        MAlerter.show(getActivity(), "خطا", "در قبول درخواست خطایی رخ داد");
                    }

                    @Override
                    public void apiSucceeded(Object o) {
                        progress.setVisibility(View.GONE);
                        getItems();
                    }
                });
            }

            @Override
            public void reject() {

            }

            @Override
            public void dismiss() {

            }

            @Override
            public void sendMessage(String text) {
                sendInboxMessage(model, text);
            }
        }).show();
    }

    private void sendInboxMessage(RequestResponseModel model, String text) {
        progress.setVisibility(View.VISIBLE);
        repository.sendNewsForUser(
                MSharedPreferences.getInstance().getTokenHeader(getContext()),
                model.getType() + "\n" + "پاسخ مدیر: " + text,
                MyApplication.company.getCompany().getId(),
                model.getUser().getId(),
                new ApiCallback() {
                    @Override
                    public void apiFailed(Object o) {
                        progress.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "در ارسال پاسخ خطایی رخ داد!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void apiSucceeded(Object o) {
                        Toast.makeText(getContext(), "پیام با موفقیت ارسال شد.", Toast.LENGTH_SHORT).show();
                        progress.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public void onReject(RequestResponseModel model) {
        new AreYouShortDialogWithMessage(getContext(), null, null, null, new AreYouShortCallback() {
            @Override
            public void accept() {
                progress.setVisibility(View.VISIBLE);
                repository.rejectRequest(MSharedPreferences.getInstance().getTokenHeader(getContext()), model, new ApiCallback() {

                    @Override
                    public void apiFailed(Object o) {
                        progress.setVisibility(View.GONE);
                        MAlerter.show(getActivity(), "خطا", "در رد درخواست خطایی رخ داد");
                    }

                    @Override
                    public void apiSucceeded(Object o) {
                        progress.setVisibility(View.GONE);
                        getItems();
                    }
                });
            }

            @Override
            public void reject() {

            }

            @Override
            public void dismiss() {

            }

            @Override
            public void sendMessage(String text) {
                sendInboxMessage(model, text);
            }
        }).show();
    }
}