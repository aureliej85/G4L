package fr.aureliejosephine.go4lunch.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import fr.aureliejosephine.go4lunch.models.places.NearByApiResponse;
import fr.aureliejosephine.go4lunch.network.PlaceApi;
import fr.aureliejosephine.go4lunch.network.PlaceService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListRepository {

    private static final String TAG = ListRepository.class.getSimpleName();
    private PlaceApi placeApi;

    public ListRepository(){
        placeApi = PlaceService.getRetrofitInstance().create(PlaceApi.class);
    }

    public LiveData<NearByApiResponse> getRestaurants(String location){
        final MutableLiveData<NearByApiResponse> data = new MutableLiveData<>();
        placeApi.getRestaurants(location).enqueue(new Callback<NearByApiResponse>() {

            @Override
            public void onResponse(Call<NearByApiResponse> call, Response<NearByApiResponse> response) {
                if (response.body() != null){
                    data.setValue(response.body());

                    Log.d(TAG, "articles total result:: " + response.body().getResults());
                    Log.d(TAG, "articles size:: " + response.body().getResults().size());
                    Log.d(TAG, "articles title pos 0:: " + response.body().getResults().get(0).getName());
                }
            }


            @Override
            public void onFailure(Call<NearByApiResponse> call, Throwable t) {
                data.setValue(null);
                Log.i(TAG, "onFailure: " + t.toString());
            }
        });
        return data;
    }
}




