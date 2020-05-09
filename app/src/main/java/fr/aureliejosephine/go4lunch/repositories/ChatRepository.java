package fr.aureliejosephine.go4lunch.repositories;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChatRepository {

    private static final String COLLECTION_NAME = "chats";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getChatCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

}
