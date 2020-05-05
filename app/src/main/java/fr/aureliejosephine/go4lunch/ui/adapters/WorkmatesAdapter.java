package fr.aureliejosephine.go4lunch.ui.adapters;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import fr.aureliejosephine.go4lunch.R;
import fr.aureliejosephine.go4lunch.models.User;

public class WorkmatesAdapter extends FirestoreRecyclerAdapter<User, WorkmatesAdapter.UsersViewHolder> {

    public WorkmatesAdapter(@NonNull FirestoreRecyclerOptions<User> options) {
        super(options);
    }

    @NonNull
    @Override
    public WorkmatesAdapter.UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workmates, parent, false);
        return new UsersViewHolder(view);
    }


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onBindViewHolder(@NonNull UsersViewHolder holder, int position, @NonNull User model) {

        if(model.getRestaurantName() == null){
            holder.descrTv.setText(model.getUsername() + " hasn't decided yet");
            holder.descrTv.setTypeface(null, Typeface.ITALIC);
            holder.descrTv.setTextColor(R.color.quantum_grey);

        } else {
            holder.descrTv.setText(model.getUsername() + " is eating " + model.getRestaurantName());
            holder.descrTv.setTypeface(null, Typeface.BOLD);
        }


        if(model.getPicture() != null){
            Glide.with(holder.userPic.getContext())
                    .load(model.getPicture())
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.userPic);
        } else {
            Glide.with(holder.userPic.getContext())
                    .load(R.drawable.logo_g4l)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.userPic);
        }

    }


    public class UsersViewHolder extends RecyclerView.ViewHolder {

        private ImageView userPic;
        private TextView descrTv;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);

            userPic = itemView.findViewById(R.id.workmatesItemIv);
            descrTv = itemView.findViewById(R.id.descrTv);
        }
    }
}
