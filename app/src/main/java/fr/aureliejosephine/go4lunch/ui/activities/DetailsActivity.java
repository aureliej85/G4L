package fr.aureliejosephine.go4lunch.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fr.aureliejosephine.go4lunch.R;
import fr.aureliejosephine.go4lunch.api.RestaurantHelper;
import fr.aureliejosephine.go4lunch.api.UserHelper;
import fr.aureliejosephine.go4lunch.models.Restaurant;
import fr.aureliejosephine.go4lunch.models.User;
import fr.aureliejosephine.go4lunch.models.details_places.DetailsResult;
import fr.aureliejosephine.go4lunch.models.places.Result;
import fr.aureliejosephine.go4lunch.ui.fragments.WorkmatesFragment;

public class DetailsActivity extends AppCompatActivity {

    private ImageView picDetails;
    private TextView titleDetails;
    private TextView addressDetails;
    private ImageView chosenRestaurantFab;
    private ImageView websiteIv;
    private ImageView phoneIv;

    private FirebaseFirestore firebaseFirestore;
    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter adapter;

    public static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/photo";
    private String key = "AIzaSyASuNr6QZGHbqEtY1GEfoKlVdkaEMz1PBM";
    private User user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference userRef = db.collection("users").document(getCurrentUser().getUid());


    private List<User> listWm = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details_restaurant);

        picDetails = findViewById(R.id.picDetailsIv);
        titleDetails = findViewById(R.id.titleDetailTv);
        addressDetails = findViewById(R.id.adresseDetailsTv);
        chosenRestaurantFab = findViewById(R.id.fabDetails);
        websiteIv = findViewById(R.id.websiteDetailIv);
        phoneIv = findViewById(R.id.phoneDetailIv);
        chosenRestaurantFab = findViewById(R.id.fabDetails);
        recyclerView = findViewById(R.id.recycler_wm_details);

        configUI();

        greenCheck();

        // Config recyclerview with firestore
        firebaseFirestore = FirebaseFirestore.getInstance();
        ConfigFirestoreRecyclerAdapter();

        chosenRestaurantFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                DetailsResult result = intent.getParcelableExtra("result");

                // PICK THIS RESTAURANT
                userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        user = documentSnapshot.toObject(User.class);

                        if(documentSnapshot.getData().get("restaurantName").equals(result.getName())){
                            Toast.makeText(DetailsActivity.this, "Vous avez déjà sélectionné ce restaurant", Toast.LENGTH_SHORT).show();

                        } else {
                            UserHelper.updateRestaurantChosen(getCurrentUser().getUid(), result.getName());
                            Toast.makeText(DetailsActivity.this, "Resto bien selectionné", Toast.LENGTH_SHORT).show();
                            System.out.println("nom resto dans User= " + user.getRestaurantName() + "nom resto dans result " + result.getName());

                            chosenRestaurantFab.setImageResource(R.drawable.ic_check_circle_green_24dp);

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("DetailsActivity", "onFailure: " + e.toString());
                    }
                });


                // ADD ME IN THIS RESTAURANT IN FIRESTORE
                db.collection("restaurants").document(result.getId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User newUser = new User(user.getUid(), user.getUsername(), user.getEmail());
                        listWm.add(newUser);
                        RestaurantHelper.updateRestaurant(result.getId(), listWm);

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

    public void ConfigFirestoreRecyclerAdapter(){
        Log.i("deyailsActivity", "ConfigFirestoreRecyclerAdapter: ");
        Query query = firebaseFirestore.collection("restaurants");
        FirestoreRecyclerOptions<Restaurant> options = new FirestoreRecyclerOptions.Builder<Restaurant>()
                .setQuery(query, Restaurant.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Restaurant, DetailsActivity.RestaurantsViewHolder>(options) {
            @NonNull
            @Override
            public DetailsActivity.RestaurantsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wm_details, parent, false);
                return new DetailsActivity.RestaurantsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull DetailsActivity.RestaurantsViewHolder holder, int position, @NonNull Restaurant model) {

                db.collection("restaurants").document(model.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.i("deTailsActivity", "onSuccess: ");




                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("DetailsActivity", "onFailure: " + e.toString());

                    }
                });




            }
        };

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL));
    }


    private class RestaurantsViewHolder extends RecyclerView.ViewHolder {

        private ImageView userPic;
        private TextView descrTv;

        public RestaurantsViewHolder(@NonNull View itemView) {
            super(itemView);

            userPic = itemView.findViewById(R.id.wmPicDetail);
            descrTv = itemView.findViewById(R.id.wmJoiningTv);
        }
    }


    public void configUI() {
        Intent intent = getIntent();
        DetailsResult result = intent.getParcelableExtra("result");

        titleDetails.setText(result.getName());
        addressDetails.setText(result.getVicinity());

        db.collection("restaurants").document(result.getId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String picUrl = documentSnapshot.getData().get("urlPhoto").toString();

                Glide.with(getApplication()).load(picUrl)
                        .into(picDetails);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("DetailsActivity", "onFailure: " + e.toString());

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

                if(documentSnapshot.getData().get("restaurantName").equals(result.getName())){
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


    @Nullable
    protected FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }


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
}



