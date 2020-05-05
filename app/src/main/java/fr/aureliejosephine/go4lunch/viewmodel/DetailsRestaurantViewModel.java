package fr.aureliejosephine.go4lunch.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import fr.aureliejosephine.go4lunch.models.details_places.DetailsApiResponse;
import fr.aureliejosephine.go4lunch.repositories.DetailsRestaurantRepository;

public class DetailsRestaurantViewModel extends AndroidViewModel {

    private DetailsRestaurantRepository detailsRestaurantRepository;
    private LiveData<DetailsApiResponse> getDetailsRestaurantLiveData;



    public DetailsRestaurantViewModel(@NonNull Application application){
        super(application);
        detailsRestaurantRepository = new DetailsRestaurantRepository();
    }

    public LiveData<DetailsApiResponse> getDetailsRestaurant(String id){
        return detailsRestaurantRepository.getDetailsRestaurants(id);

    }
}
