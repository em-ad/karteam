package space.kheyrati.nanowatch;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

public class VacationTypeDialog extends Dialog {

   RequestTypeCallback callback;

   public VacationTypeDialog(@NonNull Context context, RequestTypeCallback callback) {
      super(context);
      this.callback = callback;
   }

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.dialog_vacation_type);
      findViewById(R.id.tvHoured).setOnClickListener(view -> {
         callback.typeSelected("استحقاقی (ساعتی):");
         dismiss();
      });
      findViewById(R.id.tvDayed).setOnClickListener(view -> {
         callback.typeSelected("استحقاقی (روزانه):");
         dismiss();
      });
      findViewById(R.id.tvMedical).setOnClickListener(view -> {
         callback.typeSelected("استعلاجی:");
         dismiss();
      });
      findViewById(R.id.tvPayless).setOnClickListener(view -> {
         callback.typeSelected("بدون حقوق:");
         dismiss();
      });
      findViewById(R.id.tvOthers).setOnClickListener(view -> {
         callback.typeSelected("دلیل خاص:");
         dismiss();
      });
   }
}
