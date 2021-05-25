package cat.itb.yapp.retrofit;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//https://stackoverflow.com/questions/41078866/retrofit2-authorization-global-interceptor-for-access-token
public class RetrofitHttpLogin {
    public Retrofit retrofit;
    private HttpLoggingInterceptor loggingInterceptor;
    public OkHttpClient.Builder httpClient;
    public static String BASE_URL = "https://yapp-backend.herokuapp.com/api/";

    public RetrofitHttpLogin() {
        super();
        loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

        httpClient = new OkHttpClient.Builder().addInterceptor(loggingInterceptor);

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

    }

}
