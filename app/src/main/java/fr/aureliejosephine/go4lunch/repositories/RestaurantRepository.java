package fr.aureliejosephine.go4lunch.repositories;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import fr.aureliejosephine.go4lunch.models.Restaurant;
import fr.aureliejosephine.go4lunch.models.User;

public class RestaurantRepository {

    private static final String COLLECTION_NAME = "restaurants";
    private CollectionReference restaurantCollection;
    private Restaurant restaurant;
    private static volatile RestaurantRepository INSTANCE;

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

    public Task<Void> createRestaurant(String id, String name, String urlPhoto, String address, String phoneNumber, String website, List<User> workmatesHere) {

        Restaurant restaurantToCreate = new Restaurant(id, name, urlPhoto, address, phoneNumber, website, workmatesHere);

        return restaurantCollection.document(id).set(restaurantToCreate);
    }

    // --- GET RESTAURANT ---

    public Task<DocumentSnapshot> getRestaurant(String id){
        return restaurantCollection.document(id).get();
    }


    public  Task<QuerySnapshot> getAllRestaurants(){
        return getRestaurantsCollection().get();
    }

    // --- UPDATE RESTAURANT ---

    public  Task<Void> updateRestaurant(String id, List<User> userList) {

        return restaurantCollection.document(id).update("usersEatingHere", userList);
    }



    // --- DELETE RESTAURANT---

    public Task<Void> deleteRestaurant(String id) {
        return restaurantCollection.document(id).delete();
    }

}
