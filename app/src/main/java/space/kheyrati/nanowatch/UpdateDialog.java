package space.kheyrati.nanowatch;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class UpdateDialog extends Dialog {

    private String version;

    public UpdateDialog(@NonNull Context context, String version) {
        super(context);
        setCancelable(false);
        this.version = version;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_force_update);
        if (version != null) {
            ((TextView) findViewById(R.id.tvDownload)).setText("دانلود نسخه " + version);
        }
        findViewById(R.id.tvDownload).setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://karteam.kheyrati.space/version/download"));
            getContext().startActivity(intent);
        });
    }
}
