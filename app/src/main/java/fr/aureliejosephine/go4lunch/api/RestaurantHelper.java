package fr.aureliejosephine.go4lunch.api;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import androidx.annotation.Nullable;
import fr.aureliejosephine.go4lunch.models.Restaurant;
import fr.aureliejosephine.go4lunch.models.User;

public class RestaurantHelper {

    private static final String COLLECTION_NAME = "restaurants";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getRestaurantsCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createRestaurants(String id, String name, String urlPhoto, List<User> workmatesHere) {
        // 1 - Create Obj
        Restaurant restaurantToCreate = new Restaurant(id, name, urlPhoto, workmatesHere);

        return RestaurantHelper.getRestaurantsCollection().document(id).set(restaurantToCreate);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getRestaurant(String id){
        return RestaurantHelper.getRestaurantsCollection().document(id).get();
    }


    public static Task<QuerySnapshot> getAllRestaurants(){
        return RestaurantHelper.getRestaurantsCollection().get();
    }

    // --- UPDATE ---

    public static Task<Void> updateRestaurant(String name) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(name);
        return UserHelper.getUsersCollection().document(name).update("name", name);
    }

    public static Task<Void> updateWmRestaurant(String name, List<User> wm) {
        Restaurant restaurant = new Restaurant();
        restaurant.setUserGoingEating(wm);
        return RestaurantHelper.getRestaurantsCollection().document(name).update("workmatesHere", wm);
    }





    // --- DELETE ---

    public static Task<Void> deleteRestaurant(String name) {
        return UserHelper.getUsersCollection().document(name).delete();
    }

}
