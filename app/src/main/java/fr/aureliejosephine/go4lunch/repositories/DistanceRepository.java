package fr.aureliejosephine.go4lunch.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import fr.aureliejosephine.go4lunch.models.distance.Distance;
import fr.aureliejosephine.go4lunch.models.distance.DistanceApiResponse;
import fr.aureliejosephine.go4lunch.models.places.NearByApiResponse;
import fr.aureliejosephine.go4lunch.network.PlaceApi;
import fr.aureliejosephine.go4lunch.network.PlaceService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DistanceRepository {

    private static final String TAG = DistanceRepository.class.getSimpleName();
    private PlaceApi placeApi;

    public DistanceRepository(){
        placeApi = PlaceService.getRetrofitInstance().create(PlaceApi.class);
    }

    public LiveData<DistanceApiResponse> getDistance(String origins, String destinations){

        final MutableLiveData<DistanceApiResponse> data = new MutableLiveData<>();

        placeApi.getDistance(origins, destinations).enqueue(new Callback<DistanceApiResponse>() {

            @Override
            public void onResponse(Call<DistanceApiResponse> call, Response<DistanceApiResponse> response) {
                if (response.body() != null){
                    data.setValue(response.body());

                    Log.d(TAG, "articles total result:: " + response.body().toString());
                    //Log.d(TAG, "articles size:: " + response.body().getResults().size());
                    //  Log.d(TAG, "articles title pos 0:: " + response.body().getResults().get(0));
                }
            }


            @Override
            public void onFailure(Call<DistanceApiResponse> call, Throwable t) {
                data.setValue(null);
                Log.i(TAG, "onFailure: " + t.toString());
            }
        });
        return data;
    }


}
