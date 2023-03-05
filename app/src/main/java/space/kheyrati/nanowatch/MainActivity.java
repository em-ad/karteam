package space.kheyrati.nanowatch;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

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
import com.google.android.material.bottomnavigation.BottomNavigationView;

import space.kheyrati.nanowatch.api.KheyratiRepository;
import space.kheyrati.nanowatch.model.AttendanceViewModel;

public class MainActivity
        extends AppCompatActivity
        implements LocationAssistant.Listener {

    private BottomNavigationView bottomNav;
    private ProfileFragment profileFragment;
    private TrafficFragment trafficFragment;
    private RequestListFragment requestListFragment;
    private CartableFragment cartableFragment;
    private AttendeesFragment attendeesFragment;
    private AttendanceViewModel attendanceViewModel;
    private FusedLocationProviderClient fusedLocationClient;
    private KheyratiRepository repository = new KheyratiRepository();

    private LocationAssistant assistant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("TAG", "ACCESS TOKEN IS = " + MSharedPreferences.getInstance().getToken(this));
        if (MyApplication.role == null || !MyApplication.role.equalsIgnoreCase("admin")) {
            bottomNav = findViewById(R.id.bottom_navigation_view_employee);
            findViewById(R.id.bottom_navigation_view).setVisibility(View.INVISIBLE);
        } else {
            bottomNav = findViewById(R.id.bottom_navigation_view);
            findViewById(R.id.bottom_navigation_view_employee).setVisibility(View.INVISIBLE);
        }
        bottomNav.setSelectedItemId(R.id.trafficFragment);
        bottomNav.setOnItemSelectedListener(navListener);
        attendanceViewModel = new ViewModelProvider(this).get(AttendanceViewModel.class);
        assistant = new LocationAssistant(this, this, LocationAssistant.Accuracy.HIGH, 1000, false);
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
        assistant.start();
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
//                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            locationPermissionRequest.launch(new String[]{
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//            });
//        } else {
//            findUserLocation();
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        assistant.stop();
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
//        if (fusedLocationClient == null)
//            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        fusedLocationClient.requestLocationUpdates(getLocationRequest(), new LocationCallback() {
//            @Override
//            public void onLocationResult(@NonNull LocationResult locationResult) {
//                super.onLocationResult(locationResult);
//                if (locationResult.getLocations().size() > 0) {
//                    MyApplication.lastLocation = locationResult.getLocations().get(locationResult.getLocations().size() - 1);
//                }
//            }
//        }, getMainLooper());
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

    @Override
    public void onNeedLocationPermission() {
        locationPermissionRequest.launch(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }

    @Override
    public void onExplainLocationPermission() {
        MAlerter.show(this, "نیاز به دسترسی", "کارتیم نیاز به دسترسی موقعیت شما دارد");
    }

    @Override
    public void onLocationPermissionPermanentlyDeclined(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog) {
        MAlerter.show(this, "خطایی رخ داد", "دسترسی مکان برای ثبت ورود و خروج ضروری است");
    }

    @Override
    public void onNeedLocationSettingsChange() {
        locationPermissionRequest.launch(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }

    @Override
    public void onFallBackToSystemSettings(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog) {
        MAlerter.show(this, "خطایی رخ داد", "دسترسی مکان برای ثبت ورود و خروج ضروری است");
    }

    @Override
    public void onNewLocationAvailable(Location location) {
        Log.e("TAG", "onNewLocationAvailable: " + location.getLatitude() + " " + location.getLongitude() );
        MyApplication.lastLocation = location;
    }

    @Override
    public void onMockLocationsDetected(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog) {
        Log.e("TAG", "onMockLocationsDetected: " );
        MAlerter.show(this, "خطایی رخ داد", "FAKE LOCATION DETECTED");
    }

    @Override
    public void onError(LocationAssistant.ErrorType type, String message) {
        MAlerter.show(this, "خطایی رخ داد", "دسترسی مکان برای ثبت ورود و خروج ضروری است");
    }
}