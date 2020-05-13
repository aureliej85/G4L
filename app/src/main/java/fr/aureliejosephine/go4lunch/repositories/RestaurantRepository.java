package fr.aureliejosephine.go4lunch.repositories;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import fr.aureliejosephine.go4lunch.models.Restaurant;
import fr.aureliejosephine.go4lunch.models.User;

public class RestaurantRepository {

    private static final String COLLECTION_NAME = "restaurants";
    private CollectionReference restaurantCollection;
    private Restaurant restaurant;
    private static volatile RestaurantRepository INSTANCE;
    //private Object MutableLiveData;

    public static RestaurantRepository getInstance(){
        if(INSTANCE == null){
            INSTANCE = new RestaurantRepository();
        }
        return INSTANCE;
    }

    public RestaurantRepository(){
        this.restaurantCollection = getRestaurantsCollection();
    }




    // --- COLLECTION REFERENCE ---

    public  CollectionReference getRestaurantsCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE RESTAURANT ---

    public Task<Void> createRestaurant(String id, String name, String urlPhoto, String address, String phoneNumber, String website, String placeId, List<User> workmatesHere) {

        Restaurant restaurantToCreate = new Restaurant(id, name, urlPhoto, address, phoneNumber, website, placeId,  workmatesHere);

        return restaurantCollection.document(id).set(restaurantToCreate);
    }

    // --- GET RESTAURANT ---

    public Task<DocumentSnapshot> getRestaurant(String id){
        return restaurantCollection.document(id).get();
    }

    public Task<DocumentSnapshot> getAllRestaurant(){
        return restaurantCollection.document().get();
    }


    public  LiveData<List<Restaurant>> getAllRestaurants(){
        MutableLiveData<List<Restaurant>> getAllRestaurantLiveData = new MutableLiveData<List<Restaurant>>();
        restaurantCollection.get();
        return getAllRestaurantLiveData;
    }

    // --- UPDATE RESTAURANT ---

    public LiveData <Restaurant> updateRestaurant(String id, List<User> userList) {
        MutableLiveData<Restaurant> updateRestaurantLiveData = new MutableLiveData<Restaurant>();
        restaurantCollection.document(id).update("usersEatingHere", userList);
        return  updateRestaurantLiveData;
    }



    // --- DELETE RESTAURANT---

    public Task<Void> deleteRestaurant(String id) {
        return restaurantCollection.document(id).delete();
    }

}
