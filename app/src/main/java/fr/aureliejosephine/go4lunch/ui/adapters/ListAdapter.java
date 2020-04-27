package fr.aureliejosephine.go4lunch.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import fr.aureliejosephine.go4lunch.R;
import fr.aureliejosephine.go4lunch.models.Restaurant;
import fr.aureliejosephine.go4lunch.models.User;
import fr.aureliejosephine.go4lunch.models.details_places.DetailsResult;
import fr.aureliejosephine.go4lunch.ui.activities.DetailsActivity;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder>{

    private Context context;
    ArrayList<DetailsResult> restaurantsList;

    public static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/photo";
    public static final int MAX_WIDTH = 75;
    public static final int MAX_HEIGHT = 75;

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


            // Display Photos
            if (!(result.getPhotos() == null)){
                if (!(result.getPhotos().isEmpty())){
                    glide.load(BASE_URL+"?maxwidth="+MAX_WIDTH+"&maxheight="+MAX_HEIGHT+"&photoreference="+result.getPhotos().get(0).getPhotoReference()+"&key="+key)
                            .apply(RequestOptions.centerCropTransform())
                            .into(picIv);
                }
            }else{
                glide.load(R.drawable.logo_g4l).apply(RequestOptions.centerCropTransform()).into(picIv);
            }


        }
    }
}
