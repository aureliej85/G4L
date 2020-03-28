package fr.aureliejosephine.go4lunch;

import android.app.Application;

import java.util.concurrent.TimeUnit;

import fr.aureliejosephine.go4lunch.networking.NearByApi;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyApplication extends Application {

    NearByApi nearByApi = null;
    static MyApplication app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }


    public NearByApi getApiService() {
        if (nearByApi == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().retryOnConnectionFailure(true).readTimeout(80, TimeUnit.SECONDS).connectTimeout(80, TimeUnit.SECONDS).addInterceptor(interceptor).build();

            Retrofit retrofit = new Retrofit.Builder().baseUrl(Constant.PLACE_API_BASE_URL).addConverterFactory(getApiConvertorFactory()).client(client).build();

            nearByApi = retrofit.create(NearByApi.class);
            return nearByApi;
        } else {
            return nearByApi;
        }
    }

    private static GsonConverterFactory getApiConvertorFactory() {
        return GsonConverterFactory.create();
    }


    public static MyApplication getApp() {
        return app;
    }

}
