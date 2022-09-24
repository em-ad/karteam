package space.kheyrati.nanowatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ir.hamsaa.persiandatepicker.util.PersianCalendar;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private ProfileFragment profileFragment;
    private TrafficFragment trafficFragment;
    private RequestListFragment requestListFragment;
    private AttendanceViewModel attendanceViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNav = findViewById(R.id.bottom_navigation_view);
        bottomNav.setSelectedItemId(R.id.trafficFragment);
        bottomNav.setOnItemSelectedListener(navListener);
        attendanceViewModel = new ViewModelProvider(this).get(AttendanceViewModel.class);
        PersianCalendar currentDate = new PersianCalendar(System.currentTimeMillis());
        boolean isIn;
        if (!PreferencesManager.getInstance(this).contains("last_enter"))
            isIn = false;
        else {
            PersianCalendar oldDate = new PersianCalendar(PreferencesManager.
            getInstance(this).getLong("last_enter", System.currentTimeMillis()));
            isIn = oldDate.getPersianShortDate().equals(currentDate.getPersianShortDate());
        }
        attendanceViewModel.isIn.postValue(isIn);
    }

    private void initFragmentsIfNeeded() {
        if (profileFragment == null)
            profileFragment = new ProfileFragment();
        if (requestListFragment == null)
            requestListFragment = new RequestListFragment();
        if (trafficFragment == null)
            trafficFragment = new TrafficFragment();
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @SuppressLint("NonConstantResourceId")
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    initFragmentsIfNeeded();

                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.profileFragment:
                            selectedFragment = profileFragment;
                            break;
                        case R.id.trafficFragment:
                            selectedFragment = trafficFragment;
                            break;
                        case R.id.requestFragment:
                            selectedFragment = requestListFragment;
                            break;
                    }
                    if (selectedFragment != null) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_fragment,
                                selectedFragment).commit();
                    }

                    return true;
                }
            };

}