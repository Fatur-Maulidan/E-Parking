package retrofit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {

    // Main URL untuk masuk kedalam API
    // 192.168.0.101 (IP dari Server)

    // server andes - wifi andes
    // private static String BASE_URL = "http://192.168.100.4/laravel-applications/backend-parkir/public/api/";

    // server fatur - wifi andes
    private static String BASE_URL = "http://192.168.83.107/laravel-applications/backend-parkir/public/api/";

    public static ApiEndpoint endpoint(){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit.create(ApiEndpoint.class);
    }

}
