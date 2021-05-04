package cat.itb.yapp.webservices;

import cat.itb.yapp.models.auth.LoginDto;
import cat.itb.yapp.models.user.ProfileUserDto;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.Call;

public interface AuthWebServiceClient {
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("auth/login")
    Call<ProfileUserDto> login(@Body LoginDto loginDto);
}
