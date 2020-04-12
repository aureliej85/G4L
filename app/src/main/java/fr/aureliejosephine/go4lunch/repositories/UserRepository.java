package fr.aureliejosephine.go4lunch.repositories;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import fr.aureliejosephine.go4lunch.api.UserHelper;
import fr.aureliejosephine.go4lunch.models.User;


public class UserRepository {

    //private CollectionReference userCollection;
    private static final String COLLECTION_NAME = "users";

    private static volatile UserRepository INSTANCE;

    public static UserRepository getInstance(){
        if(INSTANCE == null){
            INSTANCE = new UserRepository();
        }
        return INSTANCE;
    }

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public  Task<Void> createUser(String uid, String username, String urlPicture, String uEmail) {
        // 1 - Create Obj
        User userToCreate = new User(uid, username, urlPicture, uEmail);

        return this.getUsersCollection().document(uid).set(userToCreate);
    }

    // --- GET ---

    public  Task<DocumentSnapshot> getUser(String uid){
        return UserRepository.getUsersCollection().document(uid).get();
    }

    public Task<QuerySnapshot> getAllUsers(){
        return UserRepository.getUsersCollection().orderBy("username").get();
    }

    // --- UPDATE ---

    public  Task<Void> updateUsernameAndEmail(String username, String uEmail,  String uid) {
        User user = new User();
        user.setUsername(username);
        return UserRepository.getUsersCollection().document(uid).update("username", username, "email", uEmail);
    }


    public  Task<Void> updatePicture(String urlPicture, String uid) {
        return UserRepository.getUsersCollection().document(uid).update("picture", urlPicture);
    }


    // --- DELETE ---

    public  Task<Void> deleteUser(String uid) {
        return UserRepository.getUsersCollection().document(uid).delete();
    }

}
