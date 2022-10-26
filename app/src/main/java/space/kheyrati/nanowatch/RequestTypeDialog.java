package space.kheyrati.nanowatch;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

public class RequestTypeDialog extends Dialog {

    RequestTypeCallback callback;

    public RequestTypeDialog(@NonNull Context context, RequestTypeCallback callback) {
        super(context);
        this.callback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_request_type);
        findViewById(R.id.tvEnter).setOnClickListener(view -> {
            callback.typeSelected("Enter");
            dismiss();
        });
        findViewById(R.id.tvExit).setOnClickListener(view -> {
            callback.typeSelected("Exit");
            dismiss();
        });
        findViewById(R.id.tvMission).setOnClickListener(view -> {
            callback.typeSelected("Mission");
            dismiss();
        });
        findViewById(R.id.tvVacation).setOnClickListener(view -> {
            callback.typeSelected("Vacation");
            dismiss();
        });
    }
}
