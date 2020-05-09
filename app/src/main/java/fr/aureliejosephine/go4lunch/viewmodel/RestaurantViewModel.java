package fr.aureliejosephine.go4lunch.viewmodel;

import android.app.Application;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import fr.aureliejosephine.go4lunch.models.Restaurant;
import fr.aureliejosephine.go4lunch.models.User;
import fr.aureliejosephine.go4lunch.models.details_places.DetailsApiResponse;
import fr.aureliejosephine.go4lunch.repositories.RestaurantRepository;

public class RestaurantViewModel extends AndroidViewModel {

    private RestaurantRepository restaurantRepository;
    private LiveData<Restaurant> getRestaurantLiveData;

    public RestaurantViewModel(@NonNull Application application) {
        super(application);
        this.restaurantRepository = new RestaurantRepository();
    }

    public void CreateRestaurant(String id, String name, String urlPhoto, String address, String phoneNumber, String website, String placeId, List<User> workmatesHere){
        restaurantRepository.createRestaurant(id, name, urlPhoto, address, phoneNumber, website, placeId, workmatesHere).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("RestaurantViewModel", "onFailure: createRestaurant " + e.toString());
            }
        });
    }

    public LiveData<Restaurant> UpdateUserRestaurant(String id, List<User> userList){
        return restaurantRepository.updateRestaurant(id, userList);
    }

    public  void getRestaurant(String id){
        restaurantRepository.getRestaurant(id).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("restaurantViewModel", "onFailure: getRestaurant " + e.toString());
            }
        });
    }


    public  void getAllRestaurant(){
        restaurantRepository.getAllRestaurant().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("restaurantViewModel", "onFailure: getAllRestaurant " + e.toString());
            }
        });
    }


}
