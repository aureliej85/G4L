package fr.aureliejosephine.go4lunch.viewmodel;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import fr.aureliejosephine.go4lunch.repositories.UserRepository;


public class UserViewModel extends AndroidViewModel {

    private UserRepository userRepository;


    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository();
    }


    // -- CREATE USER IN FIRESTORE --

     public void CreateUser(String uid, String username, String urlPicture, String uEmail, String placeId, String placeName, List<String> restaurantsLiked, Double userLat, Double userLgt, String date){
        userRepository.createUser(uid, username, urlPicture, uEmail, placeId, placeName, restaurantsLiked, userLat, userLgt, date).addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {
                 Log.i("UserViewModel", "onFailure: " + e.toString());
             }
         });;
     }


     // -- UPDATE USER IN FIRESTORE --

    public void UpdateUser(String username, String uEmail,  String uid){
        userRepository.updateUsernameAndEmail(username, uEmail, uid).addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void updateTask) {
                        Log.e("SETTINGS_ACTIVITY", "saveSettings: DONE");
                        Toast.makeText(getApplication(), "update ok", Toast.LENGTH_SHORT).show();
                    }
                });;
    }


    // -- UPDATE RESTAURANT CHOSEN BY THE USER IN FIRESTORE --

    public void UpdateRestaurantChosen(String uid, String placeId, String placeName){
        userRepository.updateRestaurantChosen(uid, placeId, placeName).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("UserViewModel", "onFailure: UpdateRestaurantChosen " + e.toString());
            }
        });
    }

    public LiveData<String> UpdateRestaurantLiked(String id, List<String> userList){
        return userRepository.updateRestaurantLiked(id, userList);
    }



}
