package cat.itb.yapp.webservices;

import java.util.List;


import cat.itb.yapp.models.user.UserDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Url;

public interface UserWebServiceClient {
    // ROLE_ADMIN
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET()
    Call<List<UserDto>> getUsers(@Url String url);

    // ROLE_USER
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET()
    Call<List<UserDto>> getUsersBySpecialistId(@Url String url);

}
