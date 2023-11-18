package space.kheyrati.nanowatch;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class AreYouShortDialogWithMessage extends Dialog {

    private String message = "آیا مطمئن هستید؟";
    private String positiveText = "تایید";
    private String negativeText = "انصراف";

    private TextView tvPositive;
    private TextView tvNegative;
    private TextView tvMessage;
    private TextView etMessage;

    private AreYouShortCallback callback;

    public AreYouShortDialogWithMessage(@NonNull Context context,
                                        String message,
                                        String positiveText,
                                        String negativeText,
                                        AreYouShortCallback callback) {
        super(context);
        this.message = message == null ? this.message : message;
        this.positiveText = positiveText == null ? this.positiveText : message;
        this.negativeText = negativeText == null ? this.negativeText : message;
        this.callback = callback;
    }

    public AreYouShortDialogWithMessage(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_are_you_short_with_message);
        tvMessage = findViewById(R.id.tvMessage);
        etMessage = findViewById(R.id.etMessage);
        tvPositive = findViewById(R.id.tvPositive);
        tvNegative = findViewById(R.id.tvNegative);
        tvMessage.setText(message);
        tvNegative.setText(negativeText);
        tvPositive.setText(positiveText);
        tvPositive.setOnClickListener(view -> {
            callback.accept();
            checkEditTextAndSendMessageIfNeeded();
            dismiss();
        });
        tvNegative.setOnClickListener(view -> {
            callback.reject();
            checkEditTextAndSendMessageIfNeeded();
            dismiss();
        });
    }

    private void checkEditTextAndSendMessageIfNeeded() {
        if(etMessage.getText().toString().trim().length() == 0)
            return;
        String message = etMessage.getText().toString().trim();
        callback.sendMessage(message);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        callback.dismiss();
    }
}
