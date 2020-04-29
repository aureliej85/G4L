package fr.aureliejosephine.go4lunch.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import fr.aureliejosephine.go4lunch.models.places.NearByApiResponse;
import fr.aureliejosephine.go4lunch.repositories.ListRepository;

public class ListViewModel extends AndroidViewModel {

    private ListRepository listRepository;
    private LiveData<NearByApiResponse> nearbyResponseLiveData;


    public ListViewModel(@NonNull Application application) {
        super(application);
      listRepository = new ListRepository();
      this.nearbyResponseLiveData = listRepository.getRestaurants("48.851932,2.377802");
    }



    public LiveData<NearByApiResponse> getNearbyResponseLiveData() {
        return nearbyResponseLiveData;
    }


}
