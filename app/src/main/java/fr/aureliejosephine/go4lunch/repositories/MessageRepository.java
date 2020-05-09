package fr.aureliejosephine.go4lunch.repositories;

import com.google.firebase.firestore.Query;

public class MessageRepository {

    private static final String COLLECTION_NAME = "messages";

    // --- GET ---

    public static Query getAllMessageForChat(String chat){
        return ChatRepository.getChatCollection()
                .document(chat)
                .collection(COLLECTION_NAME)
                .orderBy("dateCreated")
                .limit(50);
    }

}
