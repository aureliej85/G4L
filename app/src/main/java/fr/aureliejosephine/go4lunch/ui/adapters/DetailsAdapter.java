package fr.aureliejosephine.go4lunch.ui.adapters;

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

public class DetailsAdapter extends FirestoreRecyclerAdapter<User, DetailsAdapter.DetailsViewHolder> {


    public DetailsAdapter(@NonNull FirestoreRecyclerOptions<User> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull DetailsViewHolder holder, int position, @NonNull User model) {


        holder.descrTv.setText(model.getUsername() + " is joining");

        Glide.with(holder.userPic.getContext()).load(model.getPicture())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.userPic);
    }



    @NonNull
    @Override
    public DetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wm_details, parent, false);
        return new DetailsViewHolder(view);
    }

    public class DetailsViewHolder extends RecyclerView.ViewHolder {

        private ImageView userPic;
        private TextView descrTv;

        public DetailsViewHolder(@NonNull View itemView) {
            super(itemView);

            userPic = itemView.findViewById(R.id.wmPicDetail);
            descrTv = itemView.findViewById(R.id.wmJoiningTv);
        }
    }
}
