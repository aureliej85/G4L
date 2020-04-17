package fr.aureliejosephine.go4lunch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import fr.aureliejosephine.go4lunch.models.places.NearByApiResponse;
import fr.aureliejosephine.go4lunch.repositories.ListRepository;

public class ListViewModel extends ViewModel {

    private MutableLiveData<NearByApiResponse> mutableLiveData;
    private ListRepository listRepository;

    public void init(){
        if (mutableLiveData != null){
            return;
        }
        listRepository = ListRepository.getInstance();
        mutableLiveData = listRepository.getRestaurants("-33.870775, 151.199025", "AIzaSyASuNr6QZGHbqEtY1GEfoKlVdkaEMz1PBM");

    }

    public LiveData<NearByApiResponse> getRestaurantsRepository() {
        return mutableLiveData;
    }

    public LiveData<NearByApiResponse> getRestaurants(String location, String key){
        return listRepository.getRestaurants(location, key);
    }
}
