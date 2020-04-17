package fr.aureliejosephine.go4lunch.network;

import androidx.lifecycle.LiveData;
import fr.aureliejosephine.go4lunch.models.places.DetailApiResponse;
import fr.aureliejosephine.go4lunch.models.places.NearByApiResponse;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlaceApi {

    @GET("json?type=restaurant&radius=2000")
    Call<NearByApiResponse> getRestaurants(@Query("location") String location, @Query("key") String key);

    @GET("details/json?fields=vicinity,name,place_id,id,geometry,opening_hours,international_phone_number,website,photo")
    Call<DetailApiResponse> getRestaurantDetail(@Query("key") String key);

    /*@GET("autocomplete/json?strictbounds&types=establishment")
    Observable<AutoCompleteResult> getPlaceAutoComplete(@Query("input") String query, @Query("location") String location, @Query("radius") int radius, @Query("key") String apiKey );
*/


}
