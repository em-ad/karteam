package space.kheyrati.nanowatch;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class AreYouShortDialog extends Dialog {

    private String message = "آیا مطمئن هستید؟";
    private String positiveText = "تایید";
    private String negativeText = "انصراف";

    private TextView tvPositive;
    private TextView tvNegative;
    private TextView tvMessage;

    private AreYouShortCallback callback;

    public AreYouShortDialog(@NonNull Context context,
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

    public AreYouShortDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_are_you_short);
        tvMessage = findViewById(R.id.tvMessage);
        tvPositive = findViewById(R.id.tvPositive);
        tvNegative = findViewById(R.id.tvNegative);
        tvMessage.setText(message);
        tvNegative.setText(negativeText);
        tvPositive.setText(positiveText);
        tvPositive.setOnClickListener(view -> {
            callback.accept();
            dismiss();
        });
        tvNegative.setOnClickListener(view -> {
            callback.reject();
            dismiss();
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
        callback.dismiss();
    }
}
