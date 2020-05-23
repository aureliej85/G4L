package fr.aureliejosephine.go4lunch.ui.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import fr.aureliejosephine.go4lunch.R;
import fr.aureliejosephine.go4lunch.models.Restaurant;
import fr.aureliejosephine.go4lunch.models.User;
import fr.aureliejosephine.go4lunch.models.details_places.DetailsResult;
import fr.aureliejosephine.go4lunch.models.places.NearByApiResponse;
import fr.aureliejosephine.go4lunch.network.PlaceApi;
import fr.aureliejosephine.go4lunch.network.PlaceService;
import fr.aureliejosephine.go4lunch.repositories.RestaurantRepository;
import fr.aureliejosephine.go4lunch.ui.activities.DetailsActivity;
import fr.aureliejosephine.go4lunch.viewmodel.ListViewModel;
import fr.aureliejosephine.go4lunch.viewmodel.RestaurantViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_OK;

public class MapsFragment extends BaseFragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener{

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;
    private LocationCallback locationCallback;
    private int PERMISSION_ID = 44;
    private Marker marker;
    private LocationRequest locationRequest;
    private double latitude;
    private double longitude;
    private final float DEFAULT_ZOOM= 16;
    private View mapView;
    private ListViewModel listViewModel;
    private RestaurantViewModel restaurantViewModel;
    private List<Restaurant> restaurants = new ArrayList<>();
    private FirebaseFirestore firebaseFirestore;
    private Map<Marker, DetailsResult> mMarkers;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference userRef = db.collection("users").document(getCurrentUser().getUid());
    private DocumentReference restaurantRef;
    private Restaurant restaurant;
    private String userPosition;
    private User user;
    private double lat;
    private double lgt;
    private int REQUEST_CODE_LOCATION = 45;
    private String placeId;
    private DetailsResult detailsResult;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();
        CheckGooglePlayServices();
        listViewModel = ViewModelProviders.of(this).get(ListViewModel.class);
        restaurantViewModel = ViewModelProviders.of(this).get(RestaurantViewModel.class);
        getMarkersWithPosition();

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext()); // get current location of the device

        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i("MapsActivity", "onMapReady: ");

        mMap = googleMap;
        mMap.clear();

        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true); // GEOLOCATION BUTTON

        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION },
                    PERMISSION_ID);
        }


        // CUSTOM GEOLOCALISATION BUTTON
        if(mapView != null && mapView.findViewById(Integer.parseInt("1")) != null ){
            configGeolocationButton();
        }


        isGpsEnabled(); // CHECK IF GPS IS ENABLE OR NOT AND THEN REQUEST USER TO ENABLE IT

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(getContext());
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());
        task.addOnSuccessListener(Objects.requireNonNull(getActivity()), new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getDeviceLocation();
                //getMarkers();
            }
        });
        task.addOnFailureListener(getActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(e instanceof ResolvableApiException){
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    try {
                        resolvable.startResolutionForResult(getActivity(), 51);
                    } catch (IntentSender.SendIntentException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        mMap.setOnMarkerClickListener(this::onMarkerClick);
    }



   protected void  createMarker(double latitude, double longitude, String title, String placeId) {

       CollectionReference wmRef = db.collection("users");
       wmRef.whereEqualTo("placeName", title).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
           @Override
           public void onComplete(@NonNull Task<QuerySnapshot> task) {
               if(task.isSuccessful()){

                  marker =  mMap.addMarker(new MarkerOptions()
                           .position(new LatLng(latitude, longitude))
                           .title(title)
                           //.snippet(snippet)
                           .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

                   for (QueryDocumentSnapshot document : task.getResult()) {
                       Log.e("MapFragment", document.getId() + " => " + document.getData());

                                marker = mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(latitude, longitude))
                                        .title(title)
                                        //.snippet(snippet)
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                   }
               }
               marker.setTag(placeId);
           }

       });

    }


    private void getMarkersWithPosition() {
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
                                    lat = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                                    lgt = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                                    userPosition = lat + "," + lgt;

                                    // GET RESTAURANTS ACCORDING TO USER CURRENT LOCATION
                                    Log.e("ListFragment", "onSuccess: " );
                                    userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            user = documentSnapshot.toObject(User.class);

                                            //lat = user.getLatitude();
                                            //lgt = user.getLongitude();
                                            //userPosition = lat + "," + lgt;

                                            listViewModel.getRestaurants(userPosition).observe(getActivity(), restaurantsResponse -> {
                                                if (restaurantsResponse != null) {
                                                    Log.e("ListFragment", "onSuccess: " );
                                                    List<DetailsResult> restaurants = restaurantsResponse.getResults();

                                                    for(int i = 0;i < restaurants.size(); i++){
                                                        createMarker(restaurants.get(i).getGeometry().getLocation().getLat(), restaurants.get(i).getGeometry().getLocation().getLng(), restaurants.get(i).getName(), restaurants.get(i).getPlaceId());
                                                    }

                                                }
                                            });



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
                getMarkersWithPosition();
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == 51){
            if(resultCode == RESULT_OK){
                getDeviceLocation();
            }
        }
    }

    public void configGeolocationButton(){
        View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)locationButton.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        layoutParams.setMargins(0, 0, 40, 180);
    }

    // FETCH CURRENT LOCATION OF THE DEVICE
    private void getDeviceLocation(){
        if(checkPermissions()){
            if(isLocationEnabled()){

                mFusedLocationProviderClient.getLastLocation()
                        .addOnCompleteListener(Objects.requireNonNull(getActivity()), new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {

                                if (mLastKnownLocation != null) {
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                } else {
                                    isGpsEnabled();

                                    locationCallback = new LocationCallback() {
                                        @Override
                                        public void onLocationResult(LocationResult locationResult) {
                                            super.onLocationResult(locationResult);
                                            if(locationResult == null){
                                                return;
                                            }
                                            mLastKnownLocation = locationResult.getLastLocation();
                                            latitude = mLastKnownLocation.getLatitude();
                                            longitude = mLastKnownLocation.getLongitude();
                                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));

                                            userPosition = latitude + "," + longitude;

                                            mFusedLocationProviderClient.removeLocationUpdates(locationCallback);
                                        }
                                    };

                                    mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
                                }
                            }
                        });
            } else {
                Toast.makeText(getActivity(), "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }


    @Override
    public boolean onMarkerClick(Marker marker) {

        if (marker.getTag() != null){
            Log.e("MapFragment", "onClickMarker: " + marker.getTag() );
            Intent intent = new Intent(getActivity(), DetailsActivity.class);
            intent.putExtra("placeId", marker.getTag().toString());
            startActivity(intent);
            return true;
        }else{
            Log.e("MapFragemnt", "onClickMarker: ERROR NO TAG" );
            return false;
        }

    }
}
