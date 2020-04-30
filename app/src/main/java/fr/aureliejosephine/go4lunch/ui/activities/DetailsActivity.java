package fr.aureliejosephine.go4lunch.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import fr.aureliejosephine.go4lunch.R;
import fr.aureliejosephine.go4lunch.repositories.RestaurantRepository;
import fr.aureliejosephine.go4lunch.repositories.UserRepository;
import fr.aureliejosephine.go4lunch.models.User;
import fr.aureliejosephine.go4lunch.models.details_places.DetailsResult;
import fr.aureliejosephine.go4lunch.viewmodel.BookingViewModel;
import fr.aureliejosephine.go4lunch.viewmodel.UserViewModel;

public class DetailsActivity extends BaseActivity {

    private ImageView picDetails;
    private TextView titleDetails;
    private TextView addressDetails;
    private ImageView chosenRestaurantFab;
    private User user;
    private UserViewModel userViewModel;
    private BookingViewModel bookingViewModel;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference userRef = db.collection("users").document(getCurrentUser().getUid());


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details_restaurant);

        picDetails = findViewById(R.id.picDetailsIv);
        titleDetails = findViewById(R.id.titleDetailTv);
        addressDetails = findViewById(R.id.adresseDetailsTv);
        chosenRestaurantFab = findViewById(R.id.fabDetails);
        chosenRestaurantFab = findViewById(R.id.fabDetails);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        bookingViewModel = ViewModelProviders.of(this).get(BookingViewModel.class);

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

                            if((documentSnapshot.getData().get("restaurantName")) != null && (documentSnapshot.getData().get("restaurantName").equals(result.getName()))){
                                Toast.makeText(DetailsActivity.this, "Vous avez déjà sélectionné ce restaurant", Toast.LENGTH_SHORT).show();

                            } else {

                                userViewModel.UpdateRestaurantChosen(getCurrentUser().getUid(), result.getName());
                                Toast.makeText(DetailsActivity.this, "Resto bien selectionné", Toast.LENGTH_SHORT).show();
                                chosenRestaurantFab.setImageResource(R.drawable.ic_check_circle_green_24dp);
                                createBooking();
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

    public void createBooking(){
        Intent intent = getIntent();
        DetailsResult result = intent.getParcelableExtra("result");

        String bName = result.getName();
        String bUid = getCurrentUser().getUid();
        String bUsername = user.getUsername();

        bookingViewModel.createBooking(bName, null, bUid, bUsername, null);

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


}



