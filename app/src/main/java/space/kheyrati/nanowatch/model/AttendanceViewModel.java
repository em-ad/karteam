package space.kheyrati.nanowatch.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AttendanceViewModel extends ViewModel {

    public MutableLiveData<Boolean> isIn = new MutableLiveData<>(null);
    public Long enterTime = 0L;

}
