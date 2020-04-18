package fr.aureliejosephine.go4lunch.injections;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import fr.aureliejosephine.go4lunch.repositories.ListRepository;
import fr.aureliejosephine.go4lunch.repositories.UserRepository;

public class ViewModelFactory implements ViewModelProvider.Factory {


    private final ListRepository listRepository;


    public ViewModelFactory(ListRepository listRepository) {
        this.listRepository = listRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(fr.aureliejosephine.go4lunch.viewmodel.ListViewModel.class)) {
            return (T) new fr.aureliejosephine.go4lunch.viewmodel.ListViewModel(listRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
