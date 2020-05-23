package fr.aureliejosephine.go4lunch.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fr.aureliejosephine.go4lunch.R;
import fr.aureliejosephine.go4lunch.models.Restaurant;
import fr.aureliejosephine.go4lunch.models.User;
import fr.aureliejosephine.go4lunch.ui.adapters.DetailsAdapter;
import fr.aureliejosephine.go4lunch.viewmodel.DetailsViewModel;
import fr.aureliejosephine.go4lunch.viewmodel.RestaurantViewModel;
import fr.aureliejosephine.go4lunch.viewmodel.UserViewModel;


public class DetailsActivity extends BaseActivity {

    private static final String TAG = "DetailsActivity" ;
    private ImageView picDetails;
    private TextView titleDetails;
    private TextView addressDetails;
    private ImageView chosenRestaurantFab;
    private ImageButton phoneCall;
    private ImageButton website;
    private ImageView starLike;
    private ImageView heartLike;
    private TextView likeTv;
    private RatingBar ratingBar;
    private User user;
    private Restaurant restaurant;
    private UserViewModel userViewModel;
    private DetailsViewModel detailsViewModel;
    private RestaurantViewModel restaurantViewModel;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference userRef = db.collection("users").document(getCurrentUser().getUid());
    private DocumentReference restaurantRef;
    private DocumentReference newRestaurantRef;
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter adapter;
    Intent intent;
    String placeId;
    List<User> userList = new ArrayList<>();
    private String likeString;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details_restaurant);
        initViews();

        intent = getIntent();
        placeId = intent.getStringExtra("placeId");

        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recycler_wm_details);

        configViewModel();
        configUI();
        greenCheck();
        ConfigFirestoreRecyclerAdapter();
        clickOnFabButton();
        clickOnPhoneButton();
        clickOnWebsiteButton();
        clickOnLikeButton();
        heartNotif();

        Date currentTime = Calendar.getInstance().getTime();
        Log.e(TAG, "onCreate: " + currentTime.toString());
    }

    

    public void initViews(){
        picDetails = findViewById(R.id.picDetailsIv);
        titleDetails = findViewById(R.id.titleDetailTv);
        addressDetails = findViewById(R.id.adresseDetailsTv);
        chosenRestaurantFab = findViewById(R.id.fabDetails);
        phoneCall = findViewById(R.id.phoneDetail);
        website = findViewById(R.id.websiteDetail);
        starLike = findViewById(R.id.starDetail);
        heartLike = findViewById(R.id.heartDetail);
        likeTv = findViewById(R.id.likeDetailTv);
        ratingBar = findViewById(R.id.ratingBarDetail);
    }

    public void configViewModel(){
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        detailsViewModel = ViewModelProviders.of(this).get(DetailsViewModel.class);
        restaurantViewModel = ViewModelProviders.of(this).get(RestaurantViewModel.class);
    }

    public void configUI() {
        detailsViewModel.getDetailsRestaurant(placeId).observe(this, detailRestaurant ->{
            if (detailRestaurant != null) {

                titleDetails.setText(detailRestaurant.getResult().getName());
                addressDetails.setText(detailRestaurant.getResult().getVicinity());

                if (detailRestaurant.getResult().getRating() != null){
                    double googleRating = detailRestaurant.getResult().getRating();
                    double rating = (googleRating * 3) / 5;
                    ratingBar.setRating((float)rating);
                    ratingBar.setVisibility(View.VISIBLE);
                    Log.e("ListAdapter", "getRating " + detailRestaurant.getResult().getPlaceId());
                }else{
                    ratingBar.setVisibility(View.GONE);
                }

                restaurantRef = db.collection("restaurants").document(detailRestaurant.getResult().getId());
                restaurantRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        restaurant = documentSnapshot.toObject(Restaurant.class);

                        Glide.with(getApplication()).load(restaurant.getUrlPhoto())
                                .into(picDetails);

                    }
                });
        }
    });}


    public void greenCheck(){

        detailsViewModel.getDetailsRestaurant(placeId).observe(this, detailRestaurant->{

            userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    user = documentSnapshot.toObject(User.class);

                    if(documentSnapshot.getData().get("placeId") != null){
                        if(documentSnapshot.getData().get("placeId").equals(detailRestaurant.getResult().getPlaceId())){
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

        });
    }

    public void clickOnFabButton(){
        chosenRestaurantFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                detailsViewModel.getDetailsRestaurant(placeId).observe(DetailsActivity.this, restaurantsResponse -> {
                    if (restaurantsResponse != null) {

                        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                user = documentSnapshot.toObject(User.class);

                                if((documentSnapshot.getData().get("placeId")) != null && (documentSnapshot.getData().get("placeId").equals(restaurantsResponse.getResult().getPlaceId()))){
                                    Toast.makeText(DetailsActivity.this, "Vous avez déjà sélectionné ce restaurant", Toast.LENGTH_SHORT).show();

                                } else {
                                    userViewModel.UpdateRestaurantChosen(getCurrentUser().getUid(), restaurantsResponse.getResult().getPlaceId(), restaurantsResponse.getResult().getName());
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


                        Map<String, Object> map = new HashMap<>();
                        map.put("date", FieldValue.serverTimestamp());
                        userRef.update(map);
                    }
                });
            }
        });
    }


    public void clickOnLikeButton(){
        starLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detailsViewModel.getDetailsRestaurant(placeId).observe(DetailsActivity.this, detailRestaurant -> {
                    String nameRestaurantLiked = detailRestaurant.getResult().getName();
                    Query query = db.collection("users").whereEqualTo("restaurantsLiked", detailRestaurant.getResult().getName());

                    query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            likeString = likeTv.getText().toString();
                            Log.e(TAG, "onEvent: " + likeString );
                            if(likeString.equalsIgnoreCase("LIKE")){
                                userRef.update("restaurantsLiked", FieldValue.arrayUnion(nameRestaurantLiked) ).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        heartLike.setVisibility(View.VISIBLE);
                                        likeTv.setText("UNLIKE");
                                    }
                                });
                            } else if(likeString.equalsIgnoreCase("UNLIKE")){
                                userRef.update("restaurantsLiked", FieldValue.arrayRemove(nameRestaurantLiked) ).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        heartLike.setVisibility(View.INVISIBLE);
                                        likeTv.setText("LIKE");
                                    }
                                });

                            }
                        }
                    });
                });
            }
        });
    }

    public void heartNotif(){
        detailsViewModel.getDetailsRestaurant(placeId).observe(DetailsActivity.this, detailRestaurant -> {

            Query query = db.collection("users").whereArrayContains("restaurantsLiked", detailRestaurant.getResult().getName());
            likeString = likeTv.getText().toString();

            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            heartLike.setVisibility(View.VISIBLE);
                            likeTv.setText("UNLIKE");
                        }

                    }else {
                        heartLike.setVisibility(View.INVISIBLE);
                        likeTv.setText("LIKE");
                    }
                }
            });
        });
    }

    public void clickOnPhoneButton(){
        phoneCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detailsViewModel.getDetailsRestaurant(placeId).observe(DetailsActivity.this, detailRestaurant -> {

                    String phoneNumber = detailRestaurant.getResult().getFormattedPhoneNumber();
                    Log.e("DetailsActivity", "phoneRestaurant: " + phoneNumber);

                    if(phoneNumber != null){
                        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
                        startActivity(callIntent);
                    } else {
                        Toast.makeText(getApplication(), "Ce restaurant ne peut pas être joint par téléphone", Toast.LENGTH_SHORT).show();

                    }

                });
            }
        });
    }

    public void clickOnWebsiteButton(){
        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                detailsViewModel.getDetailsRestaurant(placeId).observe(DetailsActivity.this, detailRestaurant -> {

                    String websiteUrl = detailRestaurant.getResult().getWebsite();
                    Log.e("DetailsActivity", "phoneRestaurant: " + websiteUrl);

                    if(websiteUrl != null){
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(websiteUrl));
                        startActivity(i);
                    } else {
                        Toast.makeText(getApplication(), "Ce restaurant n'a pas de website'", Toast.LENGTH_SHORT).show();

                    }

                });
            }
        });
    }

    public void ConfigFirestoreRecyclerAdapter(){

            Query query = firebaseFirestore.collectionGroup("users").whereEqualTo("placeId", placeId);
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


}



