package fr.aureliejosephine.go4lunch.ui.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import fr.aureliejosephine.go4lunch.R;
import fr.aureliejosephine.go4lunch.repositories.UserRepository;
import fr.aureliejosephine.go4lunch.models.User;
import fr.aureliejosephine.go4lunch.ui.fragments.ListFragment;
import fr.aureliejosephine.go4lunch.ui.fragments.MapsFragment;
import fr.aureliejosephine.go4lunch.ui.fragments.SettingsFragment;
import fr.aureliejosephine.go4lunch.ui.fragments.WorkmatesFragment;
import fr.aureliejosephine.go4lunch.viewmodel.UserViewModel;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
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
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView sideNav;
    private GoogleMap map;
    private FirebaseFirestore firebaseFirestore;

    private User user;

    private UserViewModel userViewModel;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference userRef = db.collection("users").document(getCurrentUser().getUid());

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
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new WorkmatesFragment()).commit();
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
                                System.out.println("Déja dans la BDD");
                        } else {
                            createUserInFirestore();
                        }
                    }
                });
    }

    private void createUserInFirestore(){

        if (this.getCurrentUser() != null){

            String urlPicture = (this.getCurrentUser().getPhotoUrl() != null) ? this.getCurrentUser().getPhotoUrl().toString() : null;
            String username = this.getCurrentUser().getDisplayName();
            String uid = this.getCurrentUser().getUid();
            String uEmail = this.getCurrentUser().getEmail();

            userViewModel.CreateUser(uid, username, urlPicture, uEmail, null);

            /*UserRepository.createUser(uid, username, urlPicture, uEmail, null).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "wayaye", Toast.LENGTH_LONG).show();
                }
            });*/
        }
    }



    @Nullable
    protected FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }
}
