package cat.itb.yapp.webservices;

import java.util.List;


import cat.itb.yapp.models.patient.CreateUpdatePatientDto;
import cat.itb.yapp.models.patient.PatientDto;
import cat.itb.yapp.models.user.UpdateUserDto;
import cat.itb.yapp.models.user.UserDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET()
    Call<UserDto> getUserById(@Url String url);


    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("processregisteruser/")
    Call<UserDto> addUser(@Body UpdateUserDto updateUserDto);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @PUT()
    Call<UserDto> updateDto(@Url String url, @Body UpdateUserDto updateUserDto);

}
