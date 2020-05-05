package fr.aureliejosephine.go4lunch.viewmodel;

import android.app.Application;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import fr.aureliejosephine.go4lunch.models.User;
import fr.aureliejosephine.go4lunch.repositories.RestaurantRepository;

public class RestaurantViewModel extends AndroidViewModel {

    private RestaurantRepository restaurantRepository;

    public RestaurantViewModel(@NonNull Application application) {
        super(application);
        this.restaurantRepository = new RestaurantRepository();
    }

    public void CreateRestaurant(String id, String name, String urlPhoto, String address, String phoneNumber, String website, List<User> workmatesHere){
        restaurantRepository.createRestaurant(id, name, urlPhoto, address, phoneNumber, website, workmatesHere).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("RestaurantViewModel", "onFailure: createRestaurant " + e.toString());
            }
        });
    }

    public void UpdateUserRestaurant(String id, List<User> userList){
        restaurantRepository.updateRestaurant(id, userList).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("RestaurantViewModel", "onFailure: updateUserRestaurant" + e.toString());
            }
        });
    }

    public  void getRestaurant(String id){
        restaurantRepository.getRestaurant(id).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("restaurantViewModel", "onFailure: getRestaurant " + e.toString());
            }
        });
    }


}
