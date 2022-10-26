package space.kheyrati.nanowatch;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import ir.hamsaa.persiandatepicker.util.PersianCalendar;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private ProfileFragment profileFragment;
    private TrafficFragment trafficFragment;
    private RequestListFragment requestListFragment;
    private AttendanceViewModel attendanceViewModel;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("TAG", "ACCESS TOKEN IS = " + MSharedPreferences.getInstance().getToken(this) );
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
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        String token = task.getResult().getToken();
                        new KheyratiRepository().sendToken(MSharedPreferences.getInstance().getTokenHeader(getApplication()), token);
                    }
                });
    }

    private void initFragmentsIfNeeded() {
        if (profileFragment == null)
            profileFragment = new ProfileFragment();
        if (requestListFragment == null)
            requestListFragment = new RequestListFragment();
        if (trafficFragment == null)
            trafficFragment = new TrafficFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationPermissionRequest.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        } else {
            findUserLocation();
        }
    }

    private LocationRequest getLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(500);
        locationRequest.setFastestInterval(100);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    @SuppressLint("MissingPermission")
    public void findUserLocation() {
        if (fusedLocationClient == null)
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.requestLocationUpdates(getLocationRequest(), new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult.getLocations().size() > 0) {
                    MyApplication.lastLocation = locationResult.getLocations().get(locationResult.getLocations().size() - 1);
                }
            }
        }, getMainLooper());
    }

    ActivityResultLauncher<String[]> locationPermissionRequest =
            registerForActivityResult(new ActivityResultContracts
                            .RequestMultiplePermissions(), result -> {
                        Boolean fineLocationGranted = result.get(Manifest.permission.ACCESS_FINE_LOCATION);
                        Boolean coarseLocationGranted = result.get(Manifest.permission.ACCESS_COARSE_LOCATION);
                        if (fineLocationGranted != null && fineLocationGranted) {
                            findUserLocation();
                        } else if (coarseLocationGranted != null && coarseLocationGranted) {
                            findUserLocation();
                        } else {
                            MAlerter.show(this, "خطایی رخ داد", "دسترسی مکان برای ثبت ورود و خروج ضروری است");
                        }
                    }
            );

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