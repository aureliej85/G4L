package fr.aureliejosephine.go4lunch.api;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import fr.aureliejosephine.go4lunch.models.User;
import fr.aureliejosephine.go4lunch.repositories.UserRepository;


public class UserHelper {
    

    private static final String COLLECTION_NAME = "users";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createUser(String uid, String username, String urlPicture, String uEmail) {
        // 1 - Create Obj
        User userToCreate = new User(uid, username, urlPicture, uEmail);

        return UserHelper.getUsersCollection().document(uid).set(userToCreate);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getUser(String uid){
        return UserHelper.getUsersCollection().document(uid).get();
    }

    public static Task<QuerySnapshot> getAllUsers(){
        return UserHelper.getUsersCollection().orderBy("username").get();
    }

    // --- UPDATE ---

    public static Task<Void> updateUsernameAndEmail(String username, String uEmail,  String uid) {
        User user = new User();
        user.setUsername(username);
        return UserHelper.getUsersCollection().document(uid).update("username", username, "email", uEmail);
    }


    public static Task<Void> updatePicture(String urlPicture, String uid) {
        return UserHelper.getUsersCollection().document(uid).update("picture", urlPicture);
    }


    // --- DELETE ---

    public static Task<Void> deleteUser(String uid) {
        return UserHelper.getUsersCollection().document(uid).delete();
    }

}
