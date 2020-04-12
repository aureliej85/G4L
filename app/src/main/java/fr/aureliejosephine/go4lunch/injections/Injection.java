package fr.aureliejosephine.go4lunch.injections;

import android.content.Context;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import fr.aureliejosephine.go4lunch.repositories.UserRepository;

public class Injection {

    public static UserRepository provideUserRepository() {
        return UserRepository.getInstance();
    }


    public static ViewModelFactory provideViewModelFactory() {
        UserRepository userRepository = provideUserRepository();
        return new ViewModelFactory(userRepository);
    }

}
