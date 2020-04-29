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

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getRestaurantsCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE RESTAURANT ---

    public static Task<Void> createRestaurant(String id, String name, String urlPhoto, List<User> workmatesHere) {

        Restaurant restaurantToCreate = new Restaurant(id, name, urlPhoto, workmatesHere);

        return RestaurantRepository.getRestaurantsCollection().document(id).set(restaurantToCreate);
    }

    // --- GET RESTAURANT ---

    public static Task<DocumentSnapshot> getRestaurant(String id){
        return RestaurantRepository.getRestaurantsCollection().document(id).get();
    }


    public static Task<QuerySnapshot> getAllRestaurants(){
        return RestaurantRepository.getRestaurantsCollection().get();
    }

    // --- UPDATE RESTAURANT ---

    public static Task<Void> updateRestaurant(String id, List<User> userList) {

        return RestaurantRepository.getRestaurantsCollection().document(id).update("usersEatingHere", userList);
    }



    // --- DELETE RESTAURANT---

    public static Task<Void> deleteRestaurant(String id) {
        return RestaurantRepository.getRestaurantsCollection().document(id).delete();
    }

}
