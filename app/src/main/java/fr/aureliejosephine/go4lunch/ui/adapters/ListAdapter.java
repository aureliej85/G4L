package fr.aureliejosephine.go4lunch.ui.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import fr.aureliejosephine.go4lunch.R;
import fr.aureliejosephine.go4lunch.models.Booking;
import fr.aureliejosephine.go4lunch.repositories.BookingRepository;
import fr.aureliejosephine.go4lunch.repositories.RestaurantRepository;
import fr.aureliejosephine.go4lunch.models.details_places.DetailsResult;
import fr.aureliejosephine.go4lunch.ui.activities.DetailsActivity;
import fr.aureliejosephine.go4lunch.ui.fragments.ListFragment;
import fr.aureliejosephine.go4lunch.viewmodel.BookingViewModel;
import fr.aureliejosephine.go4lunch.viewmodel.DistanceViewModel;
import fr.aureliejosephine.go4lunch.viewmodel.RestaurantViewModel;

import static java.security.AccessController.getContext;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder>{

    private Context context;
    ArrayList<DetailsResult> restaurantsList;

    private RestaurantViewModel restaurantViewModel;
    private DistanceViewModel distanceViewModel;
    private String dist;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference restaurantRef;

    public static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/photo";
    public static final int MAX_WIDTH = 300;
    public static final int MAX_HEIGHT = 300;
    String key = "AIzaSyASuNr6QZGHbqEtY1GEfoKlVdkaEMz1PBM";




    public ListAdapter(Context context, ArrayList<DetailsResult> restaurantsList) {
        this.context = context;
        this.restaurantsList = restaurantsList;

        restaurantViewModel = ViewModelProviders.of((FragmentActivity) context).get(RestaurantViewModel.class);
        distanceViewModel = ViewModelProviders.of((FragmentActivity) context).get(DistanceViewModel.class);

    }

    @NonNull
    @Override
    public ListAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ListViewHolder holder, int position) {
        DetailsResult result = restaurantsList.get(position);

        holder.UpdateWithData(this.restaurantsList.get(position));

        //holder.nbOfUsersEatingHere(result);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetailsActivity.class);
                //intent.putExtra("result", result);
                intent.putExtra("placeId", result.getPlaceId());
                view.getContext().startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return restaurantsList.size();
    }



    public class ListViewHolder extends RecyclerView.ViewHolder{

        TextView titleTv;
        TextView addrTv;
        TextView hoursTv;
        TextView nbUserTv;
        ImageView picIv;
        TextView distance;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTv = itemView.findViewById(R.id.titleItemList);
            addrTv = itemView.findViewById(R.id.addressItemList);
            hoursTv = itemView.findViewById(R.id.hoursDetailsTv);
            nbUserTv = itemView.findViewById(R.id.nbUserHereTv);
            picIv = itemView.findViewById(R.id.picItemList);
            distance = itemView.findViewById(R.id.distTv);

        }



        public void UpdateWithData(DetailsResult result){
            RequestManager glide = Glide.with(itemView);

            this.titleTv.setText(result.getName());
            this.addrTv.setText(result.getVicinity());
            //this.distance.setText(dist);

            // Display Photos
            if (!(result.getPhotos() == null)){
                if (!(result.getPhotos().isEmpty())){
                    String url = BASE_URL+"?maxwidth="+MAX_WIDTH+"&maxheight="+MAX_HEIGHT+"&photoreference="+result.getPhotos().get(0).getPhotoReference()+"&key="+key ;
                    glide.load(url)
                            .apply(RequestOptions.centerCropTransform())
                            .into(picIv);
                    createRestaurantInFirestore(result, url);
                }
            }else{
                glide.load(R.drawable.logo_g4l).apply(RequestOptions.centerCropTransform()).into(picIv);
                createRestaurantInFirestore(result, null);
            }


            // Distance
           distanceViewModel.getDistance("48.858411,2.912251",result.getPlaceId()).observe((FragmentActivity) context, distanceResponse -> {
                if (distanceResponse != null) {

                    dist = distanceResponse.getRows().get(0).getElements().get(0).getDistance().getText();
                    this.distance.setText(dist);
                } else{
                    Toast.makeText(context, "Pas de distance", Toast.LENGTH_SHORT).show();
                }

            });





        }


       private void createRestaurantInFirestore(DetailsResult result, String url){

                String urlPicture = url;
                String restaurantName = result.getName();
                String address = result.getVicinity();
                String uid = result.getId();
                String phoneNumber = result.getFormattedPhoneNumber();
                String website = result.getWebsite();
                String placeId = result.getPlaceId();

            restaurantRef = db.collection("restaurants").document(result.getId());

            restaurantRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    if(!documentSnapshot.exists())
                    restaurantViewModel.CreateRestaurant(result.getId(), result.getName(), url, address, phoneNumber, website, placeId, null);
                }
            });

        }





        @Nullable
        protected FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }
    }
}
