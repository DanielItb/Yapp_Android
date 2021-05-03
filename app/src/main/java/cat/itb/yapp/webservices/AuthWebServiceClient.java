package cat.itb.yapp.webservices;

import cat.itb.yapp.models.auth.LoginDto;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.Call;

public interface AuthWebServiceClient {

    @POST("login")
    Call<LoginDto> login(@Body LoginDto loginDto);
}
