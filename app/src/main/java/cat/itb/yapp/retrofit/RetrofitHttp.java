package cat.itb.yapp.retrofit;

import android.util.Log;

import java.io.IOException;

import cat.itb.yapp.activities.MainActivity;
import cat.itb.yapp.utils.UtilsSharedPreferences;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
/**
 * Clase utils varios de conexión con la base de datos con la url y la peticion de cliente
 * @author David Lama, Kenneth Griñan, Daniel Acosta
 */
public class RetrofitHttp {
    public Retrofit retrofit;
    public OkHttpClient httpClient;
    public static String BASE_URL = "https://yapp-backend.herokuapp.com/api/";

    public RetrofitHttp() {
        super();


        httpClient = new OkHttpClient.Builder()


                //.addInterceptor(logging)
                .addInterceptor(new Interceptor() {
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

    }
}
