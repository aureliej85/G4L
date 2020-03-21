package fr.aureliejosephine.go4lunch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView sideNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // BOTTOM MENU
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(bottomNavListener);

        // NAVIGATION VIEW
        sideNav = findViewById((R.id.nav_view));
        sideNav.setNavigationItemSelectedListener(this);


        // TOOLBAR
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // DRAWER MENU
        drawer = findViewById(R.id.drawer_layout);
        configureDrawer();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MapFragment()).commit(); // FRAGMENT CONTAINER
    }



    private void configureDrawer(){
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
                            selectedFragment = new MapFragment();
                            break;
                        case R.id.nav_list:
                            selectedFragment = new ListFragment();
                            break;
                        case R.id.nav_workmates:
                            selectedFragment = new WorkmatesFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

                    return true;
                }
            };



    private void redirectAfterSignOut(){
        Intent in = new Intent(this, AuthActivity.class);
        startActivity(in);
        finish();
    }


    /*private NavigationView.OnNavigationItemSelectedListener navListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
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
    };*/

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
}
