package fr.aureliejosephine.go4lunch.network;

import fr.aureliejosephine.go4lunch.models.details_places.DetailsApiResponse;
import fr.aureliejosephine.go4lunch.models.distance.DistanceApiResponse;
import fr.aureliejosephine.go4lunch.models.places.NearByApiResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlaceApi {


    final static String API_KEY = "AIzaSyASuNr6QZGHbqEtY1GEfoKlVdkaEMz1PBM";

    @GET("place/nearbysearch/json?type=restaurant&radius=1500&key=" + API_KEY)
    Call<NearByApiResponse> getRestaurants(@Query("location") String location);

    @GET("place/details/json?fields=place_id,vicinity,name,id,geometry,opening_hours,formatted_phone_number,website,rating,utc_offset,photo&key=" + API_KEY)
    Call<DetailsApiResponse> getDetailsRestaurants(@Query("place_id") String placeId);

    @GET("distancematrix/json?units=metric&key=" + API_KEY)
    Call<DistanceApiResponse> getDistance(@Query("origins") String origins, @Query("destinations") String destinations);


}
