package fr.aureliejosephine.go4lunch.ui.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import fr.aureliejosephine.go4lunch.R;
import fr.aureliejosephine.go4lunch.models.details_places.DetailsResult;
import fr.aureliejosephine.go4lunch.models.User;
import fr.aureliejosephine.go4lunch.ui.fragments.ListFragment;
import fr.aureliejosephine.go4lunch.ui.fragments.MapsFragment;
import fr.aureliejosephine.go4lunch.ui.fragments.SettingsFragment;
import fr.aureliejosephine.go4lunch.ui.fragments.WorkmatesFragment;
import fr.aureliejosephine.go4lunch.viewmodel.UserViewModel;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{


    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView sideNav;
    private GoogleMap map;
    private FirebaseFirestore firebaseFirestore;
    private User user;
    private UserViewModel userViewModel;
    private static final int RC_SIGN_IN = 123;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference userRef = db.collection("users").document(getCurrentUser().getUid());
    ArrayList<DetailsResult> restaurantsList;
    private DetailsResult result;
    private int REQUEST_CODE_LOCATION = 44;
    private double latitude;
    private double longitude;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        configureBottomMenu();
        configureNavigationView();
        configureToolbar();
        configureDrawer();
        configureHeaderNavigationView();
        createNotificationChannel();
        subscribeTopicNotification();

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        firebaseFirestore = FirebaseFirestore.getInstance();

        canWeCreateNewUser();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MapsFragment()).commit(); // FRAGMENT CONTAINER

    }



    private void configureBottomMenu(){
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(bottomNavListener);
    }

    private void configureNavigationView(){
        sideNav = findViewById((R.id.nav_view));
        sideNav.setNavigationItemSelectedListener(this);
    }

    private void configureToolbar(){
        toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
    }

    private void configureDrawer(){
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem mSearch = menu.findItem(R.id.appSearchBar);
        final SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Search");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = mSearchView.getQuery().toString();
                List<Address> addressList = null;
                if(location != null || !location.equals("")){
                    Geocoder geocoder = new Geocoder(MainActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location,1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Address address = addressList.get(0);


                   LatLng latlng = new LatLng(address.getLatitude(), address.getLongitude());
                   map.addMarker(new MarkerOptions().position(latlng).title(location));
                   map.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 18));

                }

                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                //adapter.getFilter().filter(newText);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);*/
        return false;
    }



    // -----------
    // BOTTOM MENU
    // -----------

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch(menuItem.getItemId()) {

                        case R.id.nav_map:
                            Log.i("MainActivity", "onNavigationItemSelected: fragment_maps ");
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MapsFragment()).commit();
                            break;
                        case R.id.nav_list:
                            Log.i("MainActivity", "onNavigationItemSelected: ListFragment ");
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ListFragment()).commit();
                            break;
                        case R.id.nav_workmates:
                            Log.i("MainActivity", "onNavigationItemSelected: WorkmatesFragment ");
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new WorkmatesFragment()).commit();
                            break;
                    }

                    ;

                    return true;
                }
            };


    // ----------
    // SIDE MENU
    //----------


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Log.i("MainACtivity", "onNavigationItemSelected");

        switch(menuItem.getItemId()) {

            case R.id.nav_lunch:
                Log.i("MainACtivity", "onNavigationItemSelected: navLunch ");
                this.launchChosenRestaurant();
                break;
            case R.id.nav_settings:
                Log.i("MainACtivity", "onNavigationItemSelected: navSettings ");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
                break;
            case R.id.nav_logout:
                Log.i("MainACtivity", "onNavigationItemSelected: navLogout ");
                FirebaseAuth.getInstance().signOut();
                redirectAfterSignOut();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void redirectAfterSignOut(){

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(
                                Arrays.asList(
                                        new AuthUI.IdpConfig.FacebookBuilder().build(), //GOOGLE
                                        new AuthUI.IdpConfig.GoogleBuilder().build(),
                                        new AuthUI.IdpConfig.TwitterBuilder().build(),
                                        new AuthUI.IdpConfig.EmailBuilder().build())) //EMAIL)) // FACEBOOK)) // SUPPORT GOOGLE))
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.drawable.logo_g4l)
                        .build(),
                RC_SIGN_IN);
        Log.i("AuthActivity", "email Auth");
    }

    private void launchChosenRestaurant(){
        firebaseFirestore.collection("users").document(getCurrentUser().getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if(documentSnapshot != null){

                            User wmUser = documentSnapshot.toObject(User.class);
                            String placeId = wmUser.getPlaceId();

                            if(wmUser.getPlaceId() != null){
                                Intent intent = new Intent(getApplication(), DetailsActivity.class);
                                intent.putExtra("placeId", placeId);
                                startActivity(intent);
                            } else {
                                Toast.makeText(MainActivity.this, "Vous n'avez pas sectionné de restaurant", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("MainActivity", "onFailure: launchChosenRestaurant" + e.toString());
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            Log.i("AuthActivity", "onActivityResult: RC sign in");

            if (resultCode == RESULT_OK) {
                Log.i("AuthActivity", "onActivityResult: result OK");
                Intent in = new Intent(this, MainActivity.class);
                startActivity(in);
                finish();
            } else {
                if (response == null) {
                    Log.e("Login","Login canceled by User");
                    return;
                }
            }
        }
    }

    private void configureHeaderNavigationView(){
        View hView =  sideNav.getHeaderView(0);

        TextView uNameTv = hView.findViewById(R.id.uNameTv);
        TextView uEmailTv = hView.findViewById(R.id.uEmailTv);
        ImageView uPicIv = hView.findViewById(R.id.uPicIv);

        if (this.getCurrentUser() != null){

            //Get picture URL from Firebase
            if (this.getCurrentUser().getPhotoUrl() != null) {
                Glide.with(this)
                        .load(this.getCurrentUser().getPhotoUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(uPicIv);
            }


            // Realtime Name and email update : snapShotListener
            userRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                    if (e != null) {
                        Log.w("MainActivity", "Listen failed.", e);
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        //Log.d(TAG, "Current data: " + snapshot.getData());
                        user = documentSnapshot.toObject(User.class);

                        String uName = user.getUsername();
                        String uEmail = user.getEmail();

                        uNameTv.setText(uName);
                        uEmailTv.setText(uEmail);
                    } else {
                        Log.d("MainActivity", "Current data: null");
                    }
                }
            });

        }
    }



    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    private void canWeCreateNewUser(){
        firebaseFirestore.collection("users").document(getCurrentUser().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult().exists()){
                                System.out.println("User already in Firestore");
                        } else {
                            createUserWithLocation();
                        }
                    }
                });
    }


    /*private void createUserInFirestore(){

        if (this.getCurrentUser() != null){

            String urlPicture = (this.getCurrentUser().getPhotoUrl() != null) ? this.getCurrentUser().getPhotoUrl().toString() : null;
            String username = this.getCurrentUser().getDisplayName();
            String uid = this.getCurrentUser().getUid();
            String uEmail = this.getCurrentUser().getEmail();

            userViewModel.CreateUser(uid, username, urlPicture, uEmail, null, null, null, null, null);

        }
    }*/


    private void createUserWithLocation() {
        Log.i("ListFragment", "getRestaurants: ");

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(locationRequest, new LocationCallback(){

                            @Override
                            public void onLocationResult(LocationResult locationResult){
                                super.onLocationResult(locationResult);

                                LocationServices.getFusedLocationProviderClient(getApplication())
                                        .removeLocationUpdates(this);

                                if(locationResult != null && locationResult.getLocations().size() > 0){

                                    int latestLocationIndex = locationResult.getLocations().size() -1;
                                    latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                                    longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();

                                    // GET RESTAURANTS ACCORDING TO USER CURRENT LOCATION

                                    if (getCurrentUser() != null){

                                        String urlPicture = (getCurrentUser().getPhotoUrl() != null) ? getCurrentUser().getPhotoUrl().toString() : null;
                                        String username = getCurrentUser().getDisplayName();
                                        String uid = getCurrentUser().getUid();
                                        String uEmail = getCurrentUser().getEmail();

                                        userViewModel.CreateUser(uid, username, urlPicture, uEmail, null, "none", null, latitude, longitude, null);

                                    }



                                } }},
                        Looper.getMainLooper() );

    }


    // ---------------
    // -- PERMISSIONS
    // ---------------


    public void CheckPermission(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String [] {
                    Manifest.permission.ACCESS_FINE_LOCATION }, REQUEST_CODE_LOCATION);
        }}

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION) {
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                createUserWithLocation();
            }
        }
    }


}
