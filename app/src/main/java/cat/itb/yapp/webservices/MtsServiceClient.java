package cat.itb.yapp.webservices;

import java.util.List;

import cat.itb.yapp.models.auth.LoginDto;
import cat.itb.yapp.models.mts.MtsDto;
import cat.itb.yapp.models.user.UserDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface MtsServiceClient {

    @POST("login")
    Call<LoginDto> login(@Body LoginDto loginDto);

    // ROLE_ADMIN
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET()
    Call<List<MtsDto>> getUsers(@Url String url);

    // ROLE_USER
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET()
    Call<List<MtsDto>> getMtsBySpecialistId(@Url String url);

}
