package fr.aureliejosephine.go4lunch.ui.fragments;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import fr.aureliejosephine.go4lunch.R;
import fr.aureliejosephine.go4lunch.models.User;


import android.app.AlertDialog;

import android.util.Pair;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WorkmatesAdapter extends RecyclerView.Adapter<WorkmatesAdapter.MyViewHolder> {

    private List<User> usersList = new ArrayList<>();


    public WorkmatesAdapter(Context context, List<User> usersList){
        this.usersList = usersList;
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_workmates, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        User user = usersList.get(position);

        Glide.with(holder.userPic.getContext())
                .load(user.getPicture())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.userPic);

        holder.descrUser.setText(user.getUsername() + " n'a pas encore fait son choix");
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView userPic;
        private TextView descrUser;


        public MyViewHolder(final View itemView) {
            super(itemView);

            userPic = itemView.findViewById(R.id.workmatesItemIv);
            descrUser = itemView.findViewById(R.id.descrTv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "Item cliqué", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

}
