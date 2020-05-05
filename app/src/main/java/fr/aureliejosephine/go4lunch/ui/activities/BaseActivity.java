package fr.aureliejosephine.go4lunch.ui.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import fr.aureliejosephine.go4lunch.R;

public class BaseActivity extends FragmentActivity {

    @Nullable
    protected FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }

    protected Boolean isCurrentUserLogged(){ return (this.getCurrentUser() != null); }

    protected void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifications", "notifications", importance);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    protected void subscribeTopicNotification(){
        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "successfull";
                        if (!task.isSuccessful()) {
                            msg = "failed";
                        }
                        //Toast.makeText(getApplication(), msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    /*protected void messageNotification(){
        // The topic name can be optionally prefixed with "/topics/".
        String topic = "highScores";

// See documentation on defining a message payload.
        Message message = Message.builder()
                .putData("score", "850")
                .putData("time", "2:45")
                .setTopic(topic)
                .build();

// Send a message to the devices subscribed to the provided topic.
        String response = FirebaseMessaging.getInstance().send(message);
// Response is a message ID string.
        System.out.println("Successfully sent message: " + response);

    }*/

}
