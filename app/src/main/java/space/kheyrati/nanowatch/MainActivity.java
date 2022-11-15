package space.kheyrati.nanowatch;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
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
import com.google.gson.Gson;

import java.util.Collections;
import java.util.List;

import ir.hamsaa.persiandatepicker.util.PersianCalendar;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private ProfileFragment profileFragment;
    private TrafficFragment trafficFragment;
    private RequestListFragment requestListFragment;
    private CartableFragment cartableFragment;
    private AttendeesFragment attendeesFragment;
    private AttendanceViewModel attendanceViewModel;
    private FusedLocationProviderClient fusedLocationClient;
    private KheyratiRepository repository = new KheyratiRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("TAG", "ACCESS TOKEN IS = " + MSharedPreferences.getInstance().getToken(this));
        bottomNav = findViewById(R.id.bottom_navigation_view);
        bottomNav.setSelectedItemId(R.id.trafficFragment);
        bottomNav.setOnItemSelectedListener(navListener);
        attendanceViewModel = new ViewModelProvider(this).get(AttendanceViewModel.class);
        PersianCalendar currentDate = new PersianCalendar(System.currentTimeMillis());
        repository.getLogs(MSharedPreferences.getInstance().getTokenHeader(this), new ApiCallback() {
            @Override
            public void apiFailed(Object o) {
                MAlerter.show(MainActivity.this, "خطا", "در دریافت اطلاعات ورود شما خطایی رخ داد");
            }

            @Override
            public void apiSucceeded(Object o) {
                List<UserLogResponseItem> data = (List<UserLogResponseItem>) o;
                Collections.reverse(data);
                for (UserLogResponseItem item: data) {
                    if(item.getUser().getId().equals(MSharedPreferences.getInstance().getUserIdFromToken(MainActivity.this))){
                        // user peyda shod
                        Log.e("TAG", "apiSucceeded: " + item.getType());
                        if(item.getType().equalsIgnoreCase("enter")){
                            attendanceViewModel.isIn.postValue(true);
                            attendanceViewModel.enterTime = item.getDate();
                        } else {
                            attendanceViewModel.isIn.postValue(false);
                        }
                        break;
                    }
                }

            }
        });
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        String token = task.getResult().getToken();
                        Log.e("TAG", "FCM TOKEN: " + token );
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
        if (cartableFragment == null)
            cartableFragment = new CartableFragment();
        if (attendeesFragment == null)
            attendeesFragment = new AttendeesFragment();
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
                        case R.id.cartableFragment:
                            selectedFragment = cartableFragment;
                            break;
                        case R.id.profileFragment:
                            selectedFragment = profileFragment;
                            break;
                        case R.id.trafficFragment:
                            selectedFragment = trafficFragment;
                            break;
                        case R.id.requestFragment:
                            selectedFragment = requestListFragment;
                            break;
                        case R.id.attendeesFragment:
                            selectedFragment = attendeesFragment;
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