package fr.aureliejosephine.go4lunch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{



    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView sideNav;
    private GoogleMap map;

    //FOR DATA
    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        configureBottomMenu();

        configureNavigationView();

        configureToolbar();

        configureDrawer();

        configureHeaderNavigationView();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment(R.layout.activity_maps)).commit(); // FRAGMENT CONTAINER

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
        setSupportActionBar(toolbar);
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
        getMenuInflater().inflate(R.menu.search_menu, menu);
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
        return super.onCreateOptionsMenu(menu);
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
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment(R.layout.activity_maps)).commit();
                            Log.i("MainActivity", "onNavigationItemSelected: activity_maps ");
                            break;
                        case R.id.nav_list:
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ListFragment()).commit();
                            Log.i("MainActivity", "onNavigationItemSelected: ListFragment ");
                            break;
                        case R.id.nav_workmates:
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new WorkmatesFragment()).commit();
                            Log.i("MainActivity", "onNavigationItemSelected: WorkmatesFragment ");
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
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new WorkmatesFragment()).commit();
                break;
            case R.id.nav_settings:
                Log.i("MainACtivity", "onNavigationItemSelected: navSettings ");
                Toast toast2 = Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT);
                toast2.show();
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
        /*Intent in = new Intent(this, AuthActivity.class);
        startActivity(in);
        finish();*/
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(
                                Arrays.asList(
                                        new AuthUI.IdpConfig.FacebookBuilder().build(), //GOOGLE
                                        new AuthUI.IdpConfig.GoogleBuilder().build(),
                                        new AuthUI.IdpConfig.EmailBuilder().build())) //EMAIL)) // FACEBOOK)) // SUPPORT GOOGLE))
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.drawable.logo_g4l)
                        .build(),
                RC_SIGN_IN);
        Log.i("AuthActivity", "email Auth");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            Log.i("AuthActivity", "onActivityResult: RC sign in");

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // ...
                Log.i("AuthActivity", "onActivityResult: result OK");
                Intent in = new Intent(this, MainActivity.class);
                startActivity(in);
                finish();
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                if (response == null) {
                    Log.e("Login","Login canceled by User");
                    return;
                }
                /*if (response.getError() == ErrorCodes.NO_NETWORK) {
                    Log.e("Login","No Internet Connection");
                    return;
                }
                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Log.e("Login","Unknown Error");
                    return;
                }*/
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

            //Get email & username from Firebase
            String email = TextUtils.isEmpty(this.getCurrentUser().getEmail()) ? getString(R.string.info_no_email_found) : this.getCurrentUser().getEmail();
            String username = TextUtils.isEmpty(this.getCurrentUser().getDisplayName()) ? getString(R.string.info_no_username_found) : this.getCurrentUser().getDisplayName();

            //Update views with data
            uNameTv.setText(username);
            uEmailTv.setText(email);
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


    @Nullable
    protected FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }
}
