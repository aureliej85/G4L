package fr.aureliejosephine.go4lunch.ui.fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fr.aureliejosephine.go4lunch.R;
import fr.aureliejosephine.go4lunch.models.details_places.DetailsResult;
import fr.aureliejosephine.go4lunch.ui.adapters.ListAdapter;
import fr.aureliejosephine.go4lunch.viewmodel.ListViewModel;



public class ListFragment extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private ListAdapter adapter;
    private ArrayList<DetailsResult> restaurantsList = new ArrayList<>();
    private ListViewModel listViewModel;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private int REQUEST_CODE_LOCATION = 44;



    public ListFragment(){ }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_list);

        initialization();
        getRestaurants();

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());


        Log.i("ListFragment", "onCreateView: ");
        return rootView;
    }



    private void initialization() {
        Log.i("ListFragment", "initialization: ");

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new ListAdapter(getActivity(), restaurantsList);
        recyclerView.setAdapter(adapter);

        listViewModel = ViewModelProviders.of(this).get(ListViewModel.class);
    }


    private void getRestaurants() {
        Log.i("ListFragment", "getRestaurants: ");

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(getActivity())
                .requestLocationUpdates(locationRequest, new LocationCallback(){

                    @Override
                    public void onLocationResult(LocationResult locationResult){
                        super.onLocationResult(locationResult);

                        LocationServices.getFusedLocationProviderClient(getActivity())
                                .removeLocationUpdates(this);

                        if(locationResult != null && locationResult.getLocations().size() > 0){

                            int latestLocationIndex = locationResult.getLocations().size() -1;
                            double latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            double longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();


                            // GET RESTAURANTS ACCORDING TO USER CURRENT LOCATION
                            listViewModel.getRestaurants(String.format("%s,%s",latitude,longitude)).observe(getActivity(), restaurantsResponse -> {
                            if (restaurantsResponse != null) {

                            List<DetailsResult> restaurants = restaurantsResponse.getResults();
                            restaurantsList.addAll(restaurants);
                            adapter.notifyDataSetChanged();
                            }


                             });
                        } }},
                        Looper.getMainLooper() );

                    }




    // ---------------
    // -- PERMISSIONS
    // ---------------


   public void CheckPermission(){
        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String [] {
                    Manifest.permission.ACCESS_FINE_LOCATION }, REQUEST_CODE_LOCATION);
        }}

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION) {
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getRestaurants();
            }
        }
    }







    // ---------------
    // -- UTILS
    // ---------------

    @Nullable
    protected FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }

    protected Boolean isCurrentUserLogged(){ return (this.getCurrentUser() != null); }



}
