package fr.aureliejosephine.go4lunch.injections;

import android.content.Context;

import fr.aureliejosephine.go4lunch.repositories.ListRepository;
import fr.aureliejosephine.go4lunch.repositories.UserRepository;

public class Injection {

    public static ListRepository provideListRepository() {
        return ListRepository.getInstance();
    }


    public static ViewModelFactory provideViewModelFactory() {
        ListRepository listRepository = provideListRepository();
        return new ViewModelFactory(listRepository);
    }

}
