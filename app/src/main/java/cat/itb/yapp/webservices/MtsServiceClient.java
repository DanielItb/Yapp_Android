package cat.itb.yapp.webservices;

import java.util.List;

import cat.itb.yapp.models.mts.MtsCreateUpdateDto;
import cat.itb.yapp.models.mts.MtsDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface MtsServiceClient {

    // ROLE_ADMIN
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET()
    Call<List<MtsDto>> getUsers(@Url String url);

    // ROLE_USER
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET()
    Call<List<MtsDto>> getMtsBySpecialistId(@Url String url);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("medicalsheet/patient/{id}")
    Call<MtsDto> getMtsByPatientId(@Path("id") int id);


    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @PUT()
    Call<MtsDto> updateMts(@Url String url, @Body MtsCreateUpdateDto mtsCreateUpdateDto);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("medicalsheet/")
    Call<MtsDto> addMts(@Body MtsCreateUpdateDto mtsCreateUpdateDto);


    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @DELETE("medicalsheet/{id}")
    Call<MtsDto> deleteMtsDto(@Path("id") int id);

}
