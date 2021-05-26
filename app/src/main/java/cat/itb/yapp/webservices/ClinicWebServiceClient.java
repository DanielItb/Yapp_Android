package cat.itb.yapp.webservices;

import cat.itb.yapp.models.clinic.ClinicDto;
import cat.itb.yapp.models.clinic.CreateUpdateClinicDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ClinicWebServiceClient {
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("clinic/{id}")
    Call<ClinicDto> getClinicById(@Path("id")int id);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @PUT("clinic/{id}")
    Call<ClinicDto> updateClinic(@Path("id")int id, @Body CreateUpdateClinicDto createUpdateClinicDto);
}
