package fr.aureliejosephine.go4lunch.networking;

import fr.aureliejosephine.go4lunch.models.places.NearByApiResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NearByApi {

    @GET("api/place/nearbysearch/json?sensor=true&key=AIzaSyCe0L2pON1GBKGzTCYu6-T2d2cbt-OlHNo")
    Call<NearByApiResponse> getNearbyPlaces(@Query("type") String type, @Query("location") String location, @Query("radius") int radius);
}
