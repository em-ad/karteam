package space.kheyrati.nanowatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class IntroActivity extends AppCompatActivity {

    private AppCompatImageView ivNext;
    private AppCompatImageView ivPrev;
    private TextView tvStart;
    private RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        recycler = findViewById(R.id.recycler);
        ivNext = findViewById(R.id.next);
        ivPrev = findViewById(R.id.prev);
        tvStart = findViewById(R.id.start);
        tvStart.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
        recycler.setAdapter(new IntroAdapter());
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        new PagerSnapHelper().attachToRecyclerView(recycler);
        ivNext.setOnClickListener(view -> recycler.smoothScrollToPosition(((LinearLayoutManager) recycler.getLayoutManager()).findFirstVisibleItemPosition() + 1));
        ivPrev.setOnClickListener(view -> recycler.smoothScrollToPosition(((LinearLayoutManager) recycler.getLayoutManager()).findFirstVisibleItemPosition() - 1));
        recycler.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (RecyclerView.SCROLL_STATE_IDLE == newState) {
                    int pos = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                    if(pos == 0){
                        ivNext.setVisibility(View.VISIBLE);
                        ivPrev.setVisibility(View.GONE);
                        tvStart.setVisibility(View.GONE);
                    } else if (pos == 3){
                        ivNext.setVisibility(View.GONE);
                        ivPrev.setVisibility(View.GONE);
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
}