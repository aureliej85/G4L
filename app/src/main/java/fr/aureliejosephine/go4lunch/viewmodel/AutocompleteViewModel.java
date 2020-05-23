package fr.aureliejosephine.go4lunch.viewmodel;

import android.app.Application;

import com.google.android.libraries.places.widget.Autocomplete;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import fr.aureliejosephine.go4lunch.models.details_places.DetailsApiResponse;
import fr.aureliejosephine.go4lunch.models.places.NearByApiResponse;
import fr.aureliejosephine.go4lunch.repositories.AutocompleteRepository;
import fr.aureliejosephine.go4lunch.repositories.ListRepository;

public class AutocompleteViewModel extends AndroidViewModel {

    private AutocompleteRepository autocompleteRepository;

    public AutocompleteViewModel(@NonNull Application application){
        super(application);
        autocompleteRepository = new AutocompleteRepository();
    }

    public LiveData<DetailsApiResponse> getAutocompleteRestaurant(String name){
        return autocompleteRepository.getAutocompleteRestaurants(name);
    }
}
