package fr.aureliejosephine.go4lunch.viewmodel;

import fr.aureliejosephine.go4lunch.models.User;
import fr.aureliejosephine.go4lunch.repositories.ListRepository;
import fr.aureliejosephine.go4lunch.repositories.UserRepository;

public class ViewModel extends androidx.lifecycle.ViewModel {

    private final UserRepository userRepository;

    public ViewModel(UserRepository userRepository, ListRepository listRepository) {
        this.userRepository = userRepository;
    }


    public void createUser(String uid, String username, String urlPicture, String uEmail){
        userRepository.createUser(uid, username, urlPicture, uEmail);
    }

    public void getUser(String uid){
        userRepository.getUser(uid);
    }

    public void getAllUsers(){
        userRepository.getAllUsers();
    }

    public void updateUsernameAndEmail(String username, String uEmail,  String uid){
        userRepository.updateUsernameAndEmail(username, uEmail, uid);
    }

}
