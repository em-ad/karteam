package space.kheyrati.nanowatch;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

public class IntroActivity extends AppCompatActivity {

    private AppCompatImageView ivNext;
    private AppCompatImageView ivPrev;
    private TextView tvStart;
    private RecyclerView recycler;
    private ConstraintLayout root;
    private TransitionDrawable background;

    private static final int INTRO_COLOR_ANIM_DURATION_MILLIS = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        recycler = findViewById(R.id.recycler);
        root = findViewById(R.id.root);
        ivNext = findViewById(R.id.next);
        ivPrev = findViewById(R.id.prev);
        tvStart = findViewById(R.id.start);
        tvStart.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
        background = (TransitionDrawable) root.getBackground();
        doColorAnim();
        recycler.setAdapter(new IntroAdapter());
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        new PagerSnapHelper().attachToRecyclerView(recycler);
        ivNext.setOnClickListener(view -> {
            recycler.smoothScrollToPosition(Math.min(3, ((LinearLayoutManager) recycler.getLayoutManager()).findFirstVisibleItemPosition() + 1));
        });
        ivPrev.setOnClickListener(view -> {
            recycler.smoothScrollToPosition(Math.max(0, ((LinearLayoutManager) recycler.getLayoutManager()).findFirstVisibleItemPosition() - 1));
        });
        recycler.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (RecyclerView.SCROLL_STATE_IDLE == newState) {
                    int pos = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                    if (pos == 0) {
                        ivNext.setVisibility(View.VISIBLE);
                        ivPrev.setVisibility(View.GONE);
                        tvStart.setVisibility(View.GONE);
                    } else if (pos == 3) {
                        ivNext.setVisibility(View.GONE);
                        ivPrev.setVisibility(View.VISIBLE);
                        tvStart.setVisibility(View.VISIBLE);
                    } else {
                        ivNext.setVisibility(View.VISIBLE);
                        ivPrev.setVisibility(View.VISIBLE);
                        tvStart.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void doColorAnim() {
        background.setDrawable(0, getDrawable(R.drawable.green_gradient));
        background.setDrawable(1, getDrawable(R.drawable.blue_gradient));
        background.startTransition(INTRO_COLOR_ANIM_DURATION_MILLIS);
        root.postDelayed(() -> {
            background.setDrawable(0, getDrawable(R.drawable.yellow_gradient));
            background.reverseTransition(INTRO_COLOR_ANIM_DURATION_MILLIS);
            root.postDelayed(() -> {
                background.setDrawable(1, getDrawable(R.drawable.purple_gradient));
                background.startTransition(INTRO_COLOR_ANIM_DURATION_MILLIS);
                root.postDelayed(() -> {
                    background.setDrawable(0, getDrawable(R.drawable.green_gradient));
                    background.reverseTransition(INTRO_COLOR_ANIM_DURATION_MILLIS);
                    root.postDelayed(this::doColorAnim, INTRO_COLOR_ANIM_DURATION_MILLIS);
                }, INTRO_COLOR_ANIM_DURATION_MILLIS);
            }, INTRO_COLOR_ANIM_DURATION_MILLIS);
        }, INTRO_COLOR_ANIM_DURATION_MILLIS);
    }
}