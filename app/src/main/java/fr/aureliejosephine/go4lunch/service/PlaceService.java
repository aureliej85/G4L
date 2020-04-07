package fr.aureliejosephine.go4lunch.service;

import fr.aureliejosephine.go4lunch.models.places.NearByApiResponse;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlaceService {

    /*@GET("nearbysearch/json?")
    Observable<MapPlacesInfo> getRestaurantsPlaces(@Query("location") String location, @Query("radius") int radius, @Query("type") String type, @Query("key") String key);

    @GET("details/json")
    Observable<PlaceDetailsInfo> getRestaurantInfo(@Query("placeid") String placeId, @Query("key") String key);

    @GET("autocomplete/json?strictbounds&types=establishment")
    Observable<AutoCompleteResult> getPlaceAutoComplete(@Query("input") String query, @Query("location") String location, @Query("radius") int radius, @Query("key") String apiKey );

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/place/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(new OkHttpClient.Builder().addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)).build())
            .build();*/
}
