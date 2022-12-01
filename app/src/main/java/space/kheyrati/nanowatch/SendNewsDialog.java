package space.kheyrati.nanowatch;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import space.kheyrati.nanowatch.api.KheyratiRepository;

public class SendNewsDialog extends Dialog {

    KheyratiRepository repository = new KheyratiRepository();
    TextView tvSend;
    EditText etMessage;
    ProgressBar progress;

    public SendNewsDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_send_news);
        tvSend = findViewById(R.id.tvSend);
        etMessage = findViewById(R.id.etMessage);
        progress = findViewById(R.id.progress);
        tvSend.setOnClickListener(view -> {
            if (etMessage.getText().toString().trim().length() < 3) {
                Toast.makeText(getContext(), "متن اطلاعیه را کامل بنویسید", Toast.LENGTH_SHORT).show();
                return;
            }
            sendNews();
        });
    }

    private void sendNews() {
        progress.setVisibility(View.VISIBLE);
        repository.sendNews(
                MSharedPreferences.getInstance().getTokenHeader(getContext()),
                etMessage.getText().toString().trim(),
                MyApplication.company.getCompany().getId(),
                new ApiCallback() {
                    @Override
                    public void apiFailed(Object o) {
                        progress.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "در ارسال اطلاعیه خطایی رخ داد!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void apiSucceeded(Object o) {
                        progress.setVisibility(View.GONE);
                        dismiss();
                    }
                });
    }
}
