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

public class SendReplyDialog extends Dialog {

    KheyratiRepository repository = new KheyratiRepository();
    TextView tvSend;
    TextView tvReply;
    EditText etMessage;
    ProgressBar progress;
    String textToReply;
    String userId;

    public SendReplyDialog(@NonNull Context context, String textToReply, String userId) {
        super(context);
        this.textToReply = textToReply;
        this.userId = userId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_send_reply);
        tvSend = findViewById(R.id.tvSend);
        tvReply = findViewById(R.id.tvReplyText);
        tvReply.setText(textToReply);
        etMessage = findViewById(R.id.etMessage);
        progress = findViewById(R.id.progress);
        tvSend.setOnClickListener(view -> {
            if (etMessage.getText().toString().trim().length() < 3) {
                Toast.makeText(getContext(), "متن پاسخ را کامل بنویسید", Toast.LENGTH_SHORT).show();
                return;
            }
            sendReply();
        });
    }

    private void sendReply() {
        progress.setVisibility(View.VISIBLE);
        repository.sendReply(
                MSharedPreferences.getInstance().getTokenHeader(getContext()),
                etMessage.getText().toString().trim(),
                MyApplication.company.getCompany().getId(),
                userId,
                new ApiCallback() {
                    @Override
                    public void apiFailed(Object o) {
                        progress.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "در ارسال پاسخ خطایی رخ داد!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void apiSucceeded(Object o) {
                        progress.setVisibility(View.GONE);
                        dismiss();
                    }
                });
    }
}
