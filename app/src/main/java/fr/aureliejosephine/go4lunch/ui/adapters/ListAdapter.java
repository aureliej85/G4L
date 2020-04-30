package fr.aureliejosephine.go4lunch.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import fr.aureliejosephine.go4lunch.R;
import fr.aureliejosephine.go4lunch.models.Booking;
import fr.aureliejosephine.go4lunch.repositories.BookingRepository;
import fr.aureliejosephine.go4lunch.repositories.RestaurantRepository;
import fr.aureliejosephine.go4lunch.models.details_places.DetailsResult;
import fr.aureliejosephine.go4lunch.ui.activities.DetailsActivity;
import fr.aureliejosephine.go4lunch.viewmodel.BookingViewModel;

import static java.security.AccessController.getContext;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder>{

    private Context context;
    ArrayList<DetailsResult> restaurantsList;

    public static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/photo";
    public static final int MAX_WIDTH = 300;
    public static final int MAX_HEIGHT = 300;

    private BookingViewModel bookingViewModel;
    String key = "AIzaSyASuNr6QZGHbqEtY1GEfoKlVdkaEMz1PBM";


    public ListAdapter(Context context, ArrayList<DetailsResult> restaurantsList) {
        this.context = context;
        this.restaurantsList = restaurantsList;
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetailsActivity.class);
                intent.putExtra("result", result);
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
        ImageView picIv;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTv = itemView.findViewById(R.id.titleItemList);
            addrTv = itemView.findViewById(R.id.addressItemList);
            hoursTv = itemView.findViewById(R.id.hoursDetailsTv);
            picIv = itemView.findViewById(R.id.picItemList);

        }



        public void UpdateWithData(DetailsResult result){
            RequestManager glide = Glide.with(itemView);

            this.titleTv.setText(result.getName());
            this.addrTv.setText(result.getVicinity());

            String url = BASE_URL+"?maxwidth="+MAX_WIDTH+"&maxheight="+MAX_HEIGHT+"&photoreference="+result.getPhotos().get(0).getPhotoReference()+"&key="+key ;


            createRestaurantInFirestore(result, url);

            // Display Photos
            if (!(result.getPhotos() == null)){
                if (!(result.getPhotos().isEmpty())){
                    glide.load(url)
                            .apply(RequestOptions.centerCropTransform())
                            .into(picIv);
                }
            }else{
                glide.load(R.drawable.logo_g4l).apply(RequestOptions.centerCropTransform()).into(picIv);
            }


        }


        private void createRestaurantInFirestore(DetailsResult result, String url){

                String urlPicture = url;
                String restaurantName = result.getName();
                String uid = result.getId();

                if(result.getId() == null){
                    RestaurantRepository.createRestaurant(uid, restaurantName, urlPicture, null).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "wayaye", Toast.LENGTH_LONG).show();
                        }
                    });
                }
        }




        @Nullable
        protected FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }
    }
}
