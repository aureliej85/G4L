package fr.aureliejosephine.go4lunch.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fr.aureliejosephine.go4lunch.R;
import fr.aureliejosephine.go4lunch.models.Booking;
import fr.aureliejosephine.go4lunch.models.Restaurant;
import fr.aureliejosephine.go4lunch.models.details_places.DetailsApiResponse;
import fr.aureliejosephine.go4lunch.models.User;
import fr.aureliejosephine.go4lunch.models.details_places.DetailsResult;
import fr.aureliejosephine.go4lunch.ui.adapters.DetailsAdapter;
import fr.aureliejosephine.go4lunch.viewmodel.BookingViewModel;
import fr.aureliejosephine.go4lunch.viewmodel.DetailsRestaurantViewModel;
import fr.aureliejosephine.go4lunch.viewmodel.RestaurantViewModel;
import fr.aureliejosephine.go4lunch.viewmodel.UserViewModel;

import static com.google.zxing.client.result.ParsedResultType.TEL;

public class DetailsActivity extends BaseActivity {

    private ImageView picDetails;
    private TextView titleDetails;
    private TextView addressDetails;
    private ImageView chosenRestaurantFab;
    private ImageView phoneIv;
    private ImageView websiteIv;
    private ImageView likeIv;
    private User user;
    private Restaurant restaurant;
    private UserViewModel userViewModel;
    private DetailsRestaurantViewModel detailsRestaurantViewModel;
    private RestaurantViewModel restaurantViewModel;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference userRef = db.collection("users").document(getCurrentUser().getUid());
    private DocumentReference restaurantRef;
    private DocumentReference bookingRef;
    private Button bouton;


    private FirebaseFirestore firebaseFirestore;
    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter adapter;

    public static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/photo";
    public static final int MAX_WIDTH = 300;
    public static final int MAX_HEIGHT = 300;
    String key = "AIzaSyASuNr6QZGHbqEtY1GEfoKlVdkaEMz1PBM";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details_restaurant);

        picDetails = findViewById(R.id.picDetailsIv);
        titleDetails = findViewById(R.id.titleDetailTv);
        addressDetails = findViewById(R.id.adresseDetailsTv);
        chosenRestaurantFab = findViewById(R.id.fabDetails);
        /*phoneIv = findViewById(R.id.phoneDetail);
        websiteIv = findViewById(R.id.websiteDetailIv);
        likeIv = findViewById(R.id.likeDetailIv);*/
        bouton = findViewById(R.id.bouton);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        detailsRestaurantViewModel = ViewModelProviders.of(this).get(DetailsRestaurantViewModel.class);
        restaurantViewModel = ViewModelProviders.of(this).get(RestaurantViewModel.class);

        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recycler_wm_details);


        configUI();
        greenCheck();
        ConfigFirestoreRecyclerAdapter();

        chosenRestaurantFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                DetailsResult result = intent.getParcelableExtra("result");

                userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        user = documentSnapshot.toObject(User.class);

                            if((documentSnapshot.getData().get("restaurantName")) != null && (documentSnapshot.getData().get("restaurantName").equals(result.getName()))){
                                Toast.makeText(DetailsActivity.this, "Vous avez déjà sélectionné ce restaurant", Toast.LENGTH_SHORT).show();

                            } else {

                                userViewModel.UpdateRestaurantChosen(getCurrentUser().getUid(), result.getName());
                                Toast.makeText(DetailsActivity.this, "Resto bien selectionné", Toast.LENGTH_SHORT).show();
                                chosenRestaurantFab.setImageResource(R.drawable.ic_check_circle_green_24dp);

                            }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("DetailsActivity", "onFailure: " + e.toString());
                    }
                });
            }
        });

    }


    public void configUI() {
        Intent intent = getIntent();
        DetailsResult result = intent.getParcelableExtra("result");

        titleDetails.setText(result.getName());
        addressDetails.setText(result.getVicinity());

        restaurantRef = db.collection("restaurants").document(result.getId());
        restaurantRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                restaurant = documentSnapshot.toObject(Restaurant.class);

                Glide.with(getApplication()).load(restaurant.getUrlPhoto())
                        .into(picDetails);
            }
        });



    }




    private void greenCheck(){
        Intent intent = getIntent();
        DetailsResult result = intent.getParcelableExtra("result");

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);

                if(documentSnapshot.getData().get("restaurantName") != null){
                    if(documentSnapshot.getData().get("restaurantName").equals(result.getName())){
                        chosenRestaurantFab.setImageResource(R.drawable.ic_check_circle_green_24dp);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("DetailsActivity", "onFailure: " + e.toString());
            }
        });
    }

    public void ConfigFirestoreRecyclerAdapter(){
        Intent intent = getIntent();
        DetailsResult result = intent.getParcelableExtra("result");

        Query query = firebaseFirestore.collectionGroup("users").whereEqualTo("restaurantName", result.getName());
        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();

        adapter = new DetailsAdapter(options);

        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL));
    }



   @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }


    public void phoneRestaurant(View view) {
        Intent intent = getIntent();
        DetailsResult result = intent.getParcelableExtra("result");

        String phoneNumber = result.getFormattedPhoneNumber();
        Log.e("DetailsActivity", "phoneRestaurant: " + phoneNumber);

        if(phoneNumber != null){
            Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
            startActivity(callIntent);
        } else {
            Toast.makeText(this, "Ce restaurant ne peut pas être joint par téléphone", Toast.LENGTH_SHORT).show();

        }



    }
}



