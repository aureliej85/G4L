package fr.aureliejosephine.go4lunch.ui.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import fr.aureliejosephine.go4lunch.R;
import fr.aureliejosephine.go4lunch.models.Restaurant;
import fr.aureliejosephine.go4lunch.models.places.DetailApiResponse;
import fr.aureliejosephine.go4lunch.models.places.NearByApiResponse;
import fr.aureliejosephine.go4lunch.models.places.Result;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder>{

    Context context;
    ArrayList<Restaurant> restaurants;

    public ListAdapter(@NonNull Context context, ArrayList<Restaurant> restaurants) {
        this.context = context;
        this.restaurants = restaurants;
    }


    @NonNull
    @Override
    public ListAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ListViewHolder holder, int position) {

        holder.titleTv.setText(restaurants.get(position).getName());
        holder.addrTv.setText(restaurants.get(position).getAddress());

        Glide.with(holder.picIv.getContext())
                .load(restaurants.get(position).getUrlPhoto())
                .apply(RequestOptions.centerCropTransform())
                .into(holder.picIv);
    }


    @Override
    public int getItemCount() {
        return restaurants.size();
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
    }
}
