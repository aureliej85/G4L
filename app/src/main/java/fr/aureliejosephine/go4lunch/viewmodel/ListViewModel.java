package fr.aureliejosephine.go4lunch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import fr.aureliejosephine.go4lunch.models.places.NearByApiResponse;
import fr.aureliejosephine.go4lunch.models.places.Result;
import fr.aureliejosephine.go4lunch.repositories.ListRepository;

public class ListViewModel extends ViewModel {

    private MutableLiveData<NearByApiResponse> mutableLiveData;
    private ListRepository listRepository;

    /*public ListViewModel(ListRepository listRepository) {
        this.listRepository = listRepository;
    }*/

    public void init(){

        if (mutableLiveData != null){
            return;
        }
        listRepository = ListRepository.getInstance();
        mutableLiveData = listRepository.getRestaurants("48.851932,2.377802");

    }

    public LiveData<NearByApiResponse> getRestaurantsRepository() {
        return mutableLiveData;
    }

    public LiveData<NearByApiResponse> getRestaurants(String location){
        return listRepository.getRestaurants(location);
    }
}
