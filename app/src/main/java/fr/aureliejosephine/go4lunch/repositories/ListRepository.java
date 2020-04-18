package fr.aureliejosephine.go4lunch.repositories;

import androidx.lifecycle.MutableLiveData;
import fr.aureliejosephine.go4lunch.models.places.NearByApiResponse;
import fr.aureliejosephine.go4lunch.models.places.Result;
import fr.aureliejosephine.go4lunch.network.PlaceApi;
import fr.aureliejosephine.go4lunch.network.PlaceService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListRepository {

    private static ListRepository listRepository;
    private PlaceApi placeApi;


    public static ListRepository getInstance(){
        if( listRepository == null){
            listRepository = new ListRepository();
        }
        return listRepository;
    }


    public ListRepository(){
        placeApi = PlaceService.cteateService(PlaceApi.class);
    }


    public MutableLiveData<NearByApiResponse> getRestaurants(String location){
        MutableLiveData<NearByApiResponse> restaurantsData = new MutableLiveData<>();
        placeApi.getRestaurants(location).enqueue(new Callback<NearByApiResponse>() {
            @Override
            public void onResponse(Call<NearByApiResponse> call,
                                   Response<NearByApiResponse> response) {
                if (response.isSuccessful()){
                    restaurantsData.setValue(response.body());
                }
            }


            @Override
            public void onFailure(Call<NearByApiResponse> call, Throwable t) {
                restaurantsData.setValue(null);
            }
        });
        return restaurantsData;
    }
}




