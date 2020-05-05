package fr.aureliejosephine.go4lunch.ui;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import fr.aureliejosephine.go4lunch.R;
import fr.aureliejosephine.go4lunch.models.Restaurant;
import fr.aureliejosephine.go4lunch.models.User;
import fr.aureliejosephine.go4lunch.models.details_places.DetailsResult;
import fr.aureliejosephine.go4lunch.repositories.RestaurantRepository;
import fr.aureliejosephine.go4lunch.repositories.UserRepository;
import fr.aureliejosephine.go4lunch.ui.activities.BaseActivity;
import fr.aureliejosephine.go4lunch.ui.fragments.BaseFragment;
import fr.aureliejosephine.go4lunch.viewmodel.RestaurantViewModel;

public class MessagingService extends FirebaseMessagingService {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference restaurantRef;
    private FirebaseFirestore firebaseFirestore;
    private DetailsResult result;
    //private Restaurant restaurant;
    //String messageBody;
    String message = "Voici la notif";
    private Context context;
    private UserRepository userRepository;
    private RestaurantRepository restaurantRepository;
    private List<User> users;

   /* public MessagingService(Context context){
        this.context = context;
        this.configRepo();
    }*/

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        showNotification(/*remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody()*/);
    }


    public void showNotification(/*String title, String message*/){

            //restaurantRef = db.collection("restaurants").document(result.getId());
        String messageBody = message;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplication(), "notifications")
                .setContentTitle(/*title*/"Title in code")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setContentText(/*message*/ "Substitle");


        NotificationManagerCompat manager = NotificationManagerCompat.from(getApplication());
        manager.notify(999, builder.build());


    }


    public void getInfosFromFirestore(){
        db.collection("restaurants").document().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.i("MessagingService", "onSuccess: showNotification ");
                Restaurant restaurant = documentSnapshot.toObject(Restaurant.class);

                if(restaurant != null) {
                    String messageBody = message + " " + restaurant.getName() + " " + restaurant.getAddress();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("MessagingService", "onFailure: " + e.toString());
            }
        });
    }

   /* private void configRepo(){
        userRepository = UserRepository.getInstance();
        restaurantRepository = RestaurantRepository.getInstance();
    }*/

    /*private void fetchUsers() {
        users = new ArrayList<>();
        userRepository.getAllUsers()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                        User user = documentSnapshot.toObject(User.class);
                        if(user != null && user. != null) {
                            if (user.getUid().equals(currentUserId)) {
                                currentUser = user;
                            } else {
                                users.add(user);
                            }
                        }
                    }
                    fetchUsersGoing();
                });

    }*/



    @Nullable
    protected FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }



}
