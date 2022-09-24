package space.kheyrati.nanowatch;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AttendanceViewModel extends ViewModel {

    public MutableLiveData<Boolean> isIn = new MutableLiveData<>(null);
}
