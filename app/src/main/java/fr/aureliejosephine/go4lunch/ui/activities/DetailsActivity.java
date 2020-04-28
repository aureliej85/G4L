package fr.aureliejosephine.go4lunch.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import fr.aureliejosephine.go4lunch.R;
import fr.aureliejosephine.go4lunch.api.RestaurantHelper;
import fr.aureliejosephine.go4lunch.api.UserHelper;
import fr.aureliejosephine.go4lunch.models.User;
import fr.aureliejosephine.go4lunch.models.details_places.DetailsResult;
import fr.aureliejosephine.go4lunch.models.places.Result;

public class DetailsActivity extends AppCompatActivity {

    private ImageView picDetails;
    private TextView titleDetails;
    private TextView addressDetails;
    private ImageView chosenRestaurantFab;
    private ImageView websiteIv;
    private ImageView phoneIv;

    public static final int MAX_WIDTH = 75;
    public static final int MAX_HEIGHT = 75;

    public static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/photo";
    private String key = "AIzaSyASuNr6QZGHbqEtY1GEfoKlVdkaEMz1PBM";
    private String url;
    private DetailsResult result;
    private User user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference userRef = db.collection("users").document(getCurrentUser().getUid());
    //private DocumentReference restaurantRef;

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

        configUI();

        greenCheck();

        chosenRestaurantFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                DetailsResult result = intent.getParcelableExtra("result");

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

            }
        });


        phoneIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                DetailsResult result = intent.getParcelableExtra("result");
                if (result.getFormattedPhoneNumber() != null) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", result.getFormattedPhoneNumber(), null));
                    startActivity(callIntent);
                } else {
                    Toast.makeText(DetailsActivity.this, "N° de tel pas dispo", Toast.LENGTH_SHORT).show();
                    System.out.println(result.getFormattedPhoneNumber() +" "+ result.getName() +" "+ result.getWebsite() +" "+ result.getRating() +" "+ result.getVicinity() + " "+ result.getPlaceId());
                }
            }
        });

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
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}



