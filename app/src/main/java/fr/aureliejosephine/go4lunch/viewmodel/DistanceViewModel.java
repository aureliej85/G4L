package fr.aureliejosephine.go4lunch.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import fr.aureliejosephine.go4lunch.models.distance.Distance;
import fr.aureliejosephine.go4lunch.models.distance.DistanceApiResponse;
import fr.aureliejosephine.go4lunch.models.places.NearByApiResponse;
import fr.aureliejosephine.go4lunch.repositories.DistanceRepository;
import fr.aureliejosephine.go4lunch.repositories.ListRepository;

public class DistanceViewModel extends AndroidViewModel {

    private DistanceRepository distanceRepository;

    private LiveData<DistanceApiResponse> getDistanceLiveData;


    public DistanceViewModel(@NonNull Application application){
        super(application);
        distanceRepository = new DistanceRepository();
    }

    public LiveData<DistanceApiResponse> getDistance(String origins, String destinations){
        return distanceRepository.getDistance(origins, destinations);

    }

}
