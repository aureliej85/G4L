package fr.aureliejosephine.go4lunch.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import fr.aureliejosephine.go4lunch.models.details_places.DetailsApiResponse;
import fr.aureliejosephine.go4lunch.network.PlaceApi;
import fr.aureliejosephine.go4lunch.network.PlaceService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsRepository {

    private static final String TAG = DetailsRepository.class.getSimpleName();
    private PlaceApi placeApi;

    public DetailsRepository(){
        placeApi = PlaceService.getRetrofitInstance().create(PlaceApi.class);
    }

    public LiveData<DetailsApiResponse> getDetailsRestaurants(String id){

        final MutableLiveData<DetailsApiResponse> data = new MutableLiveData<>();

        placeApi.getDetailsRestaurants(id).enqueue(new Callback<DetailsApiResponse>() {

            @Override
            public void onResponse(Call<DetailsApiResponse> call, Response<DetailsApiResponse> response) {
                if (response.body() != null){
                    data.setValue(response.body());

                    Log.d(TAG, "articles total result: " + response.body().getResult());
                    //Log.d(TAG, "telephone: " + response.body().getResult().getFormattedPhoneNumber());
                    //Log.d(TAG, "website: " + response.body().getResult().getWebsite());
                    //Log.d(TAG, "address: " + response.body().getResult().getVicinity());

                }
            }


            @Override
            public void onFailure(Call<DetailsApiResponse> call, Throwable t) {
                data.setValue(null);
                Log.i(TAG, "onFailure: " + t.toString());
            }
        });
        return data;
    }


}
