package fr.aureliejosephine.go4lunch.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import fr.aureliejosephine.go4lunch.R;
import fr.aureliejosephine.go4lunch.models.places.Photo;
import fr.aureliejosephine.go4lunch.models.places.Result;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder>{

    private Context context;
    ArrayList<Result> restaurantsList;
    Photo photo;
    List<Photo> photosList = new ArrayList<>();
    String url;
    public static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/photo";
    public static final int MAX_WIDTH = 75;
    public static final int MAX_HEIGHT = 75;
    public static final int MAX_HEIGHT_LARGE = 250;
    private static final String OPEN = "OPEN";
    private static final String CLOSED = "CLOSED";
    private static final String CLOSING_SOON = "CLOSING_SOON";
    private static final String OPENING_HOURS_NOT_KNOW = "OPENING_HOURS_NOT_KNOW";
    String key = "AIzaSyASuNr6QZGHbqEtY1GEfoKlVdkaEMz1PBM";


    public ListAdapter(Context context, ArrayList<Result> restaurantsList) {
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

        holder.UpdateWithData(this.restaurantsList.get(position));

       /* holder.titleTv.setText(restaurantsList.get(position).getName());
        holder.addrTv.setText(restaurantsList.get(position).getVicinity());*/


        /*String photoReference = photo.getPhotoReference();
        url = "https://maps.googleapis.com/maps/api/place/photo" +
                "?maxwidth=100" +
                "&photoreference=" + photoReference +
                "&key=AIzaSyASuNr6QZGHbqEtY1GEfoKlVdkaEMz1PBM";

        Glide.with(holder.picIv.getContext())
                .load(url)
                .apply(RequestOptions.centerCropTransform())
                .into(holder.picIv);*/
    }


    @Override
    public int getItemCount() {
        return restaurantsList.size();
    }



    public class ListViewHolder extends RecyclerView.ViewHolder{

        TextView titleTv;
        TextView addrTv;
        ImageView picIv;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTv = itemView.findViewById(R.id.titleItemList);
            addrTv = itemView.findViewById(R.id.addressItemList);
            picIv = itemView.findViewById(R.id.picItemList);

        }

        public void UpdateWithData(Result result){
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
