package fr.aureliejosephine.go4lunch.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import fr.aureliejosephine.go4lunch.models.details_places.DetailsApiResponse;
import fr.aureliejosephine.go4lunch.repositories.DetailsRepository;

public class DetailsViewModel extends AndroidViewModel {

    private DetailsRepository detailsRepository;
    private LiveData<DetailsApiResponse> getDetailsRestaurantLiveData;



    public DetailsViewModel(@NonNull Application application){
        super(application);
        detailsRepository = new DetailsRepository();
    }

    public LiveData<DetailsApiResponse> getDetailsRestaurant(String id){
        return detailsRepository.getDetailsRestaurants(id);

    }
}
