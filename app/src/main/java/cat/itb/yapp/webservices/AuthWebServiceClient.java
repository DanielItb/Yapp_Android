package cat.itb.yapp.webservices;

import cat.itb.yapp.models.auth.LoginDto;
import cat.itb.yapp.models.user.ProfileUserDto;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.Call;

public interface AuthWebServiceClient {

    @POST("auth/login")
    Call<ProfileUserDto> login(@Body LoginDto loginDto);
}
