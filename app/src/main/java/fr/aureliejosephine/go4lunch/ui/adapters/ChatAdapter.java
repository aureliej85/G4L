package fr.aureliejosephine.go4lunch.ui.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import fr.aureliejosephine.go4lunch.R;

public class ChatAdapter {


public class ChatViewHolder extends RecyclerView.ViewHolder{

    private ImageView userPic;
    private TextView message;
    private TextView date;

    public ChatViewHolder(@NonNull View itemView) {
        super(itemView);

        userPic = itemView.findViewById(R.id.userPicChat);
        message = itemView.findViewById(R.id.messageChat);
        date = itemView.findViewById(R.id.dateChat);
    }
}
}
