package fr.aureliejosephine.go4lunch.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import fr.aureliejosephine.go4lunch.R;
import fr.aureliejosephine.go4lunch.models.User;
import fr.aureliejosephine.go4lunch.ui.activities.DetailsActivity;

public class WorkmatesAdapter extends FirestoreRecyclerAdapter<User, WorkmatesAdapter.UsersViewHolder> {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseFirestore firebaseFirestore;
    private User user;

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

        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy");
        String givenDateString = sdf.format(Calendar.getInstance().getTime());
                if(model.getDate().contains(givenDateString)){


                    System.out.println(model.getDate());
                    holder.descrTv.setText(model.getUsername() + " will go to " + model.getPlaceName());
                    holder.descrTv.setTypeface(null, Typeface.BOLD);
                } else {
                    System.out.println("model " +model.getDate());
                    System.out.println("givenDate " + givenDateString);
                    holder.descrTv.setText(model.getUsername() + " hasn't decided yet");
                    holder.descrTv.setTypeface(null, Typeface.ITALIC);
                    holder.descrTv.setTextColor(R.color.quantum_grey);
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



            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(model.getPlaceId() != null){
                        Intent intent = new Intent(view.getContext(), DetailsActivity.class);
                        //intent.putExtra("result", result);
                        intent.putExtra("placeId", model.getPlaceId());
                        view.getContext().startActivity(intent);
                    } else {
                        Log.i("wmFragment", "onClick: restaurant not chosen yet" + model.getDate());
                    }

                }
            });



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
