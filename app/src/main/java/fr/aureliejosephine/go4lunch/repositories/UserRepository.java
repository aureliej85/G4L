package fr.aureliejosephine.go4lunch.repositories;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import fr.aureliejosephine.go4lunch.models.User;


public class UserRepository {
    

    private static final String COLLECTION_NAME = "users";
    private CollectionReference userCollection;
    private User user;
    private static volatile UserRepository INSTANCE;

    public static UserRepository getInstance(){
        if(INSTANCE == null){
            INSTANCE = new UserRepository();
        }
        return INSTANCE;
    }

    public UserRepository(){
        this.userCollection = getUsersCollection();
    }

    // --- COLLECTION REFERENCE ---

    private CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE USER ---

    public Task<Void> createUser(String uid, String username, String urlPicture, String uEmail, String restaurantName) {

        User userToCreate = new User(uid, username, urlPicture, uEmail, restaurantName);

        return userCollection.document(uid).set(userToCreate);
    }

    // --- GET USER ---

    public Task<DocumentSnapshot> getUser(String uid){
        return userCollection.document(uid).get();
    }


    public Task<QuerySnapshot> getAllUsers(){
        return userCollection.get();
    }

    // --- UPDATE USER ---

    public Task<Void> updateUsernameAndEmail(String username, String uEmail,  String uid) {
        User user = new User();
        user.setUsername(username);
        return userCollection.document(uid).update("username", username, "email", uEmail);
    }


    public Task<Void> updatePicture(String urlPicture, String uid) {
        return userCollection.document(uid).update("picture", urlPicture);
    }

    public Task<Void> updateRestaurantChosen(String uid, String restaurantName) {
        return userCollection.document(uid).update("restaurantName", restaurantName);
    }



    // --- DELETE USER ---

    public Task<Void> deleteUser(String uid) {
        return userCollection.document(uid).delete();
    }

}
