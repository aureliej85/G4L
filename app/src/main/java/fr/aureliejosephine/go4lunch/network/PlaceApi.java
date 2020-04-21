package fr.aureliejosephine.go4lunch.network;

import fr.aureliejosephine.go4lunch.models.places.DetailApiResponse;
import fr.aureliejosephine.go4lunch.models.places.NearByApiResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlaceApi {



    final static String API_KEY = "AIzaSyASuNr6QZGHbqEtY1GEfoKlVdkaEMz1PBM";

    @GET("nearbysearch/json?type=restaurant&radius=1500&key=" + API_KEY)
    Call<NearByApiResponse> getRestaurants(@Query("location") String location);


}
