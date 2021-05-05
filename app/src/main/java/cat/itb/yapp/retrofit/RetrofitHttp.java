package cat.itb.yapp.retrofit;

import android.util.Log;

import java.io.IOException;

import cat.itb.yapp.activities.MainActivity;
import cat.itb.yapp.utils.UtilsSharedPreferences;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHttp {
    public Retrofit retrofit;
    private HttpLoggingInterceptor loggingInterceptor;
    public OkHttpClient httpClient;
    public static String BASE_URL = "http://10.0.2.2:8080/api/";

    public RetrofitHttp() {
        super();

        httpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Log.e("login", UtilsSharedPreferences.getToken(MainActivity.getActivity()));
                Request newRequest  = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + UtilsSharedPreferences.getToken(MainActivity.getActivity()))
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();

         retrofit = new Retrofit.Builder()
                .client(httpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

//        loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
//
//        httpClient = new OkHttpClient.Builder().addInterceptor(loggingInterceptor);
//
//        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(httpClient.build())
//                .build();


    }
}
