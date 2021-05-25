package cat.itb.yapp.webservices;

import java.util.List;

import cat.itb.yapp.models.report.ReportDto;
import cat.itb.yapp.models.treatment.CreateUpdateTreatmentDto;
import cat.itb.yapp.models.treatment.TreatmentDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface TreatmentWebServiceClient {

    // ROLE_ADMIN
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET()
    Call<List<TreatmentDto>> getTreatmentsByClinicId(@Url String url);

    // ROLE_USER
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET()
    Call<List<TreatmentDto>> getTreatmentsBySpecialistId(@Url String url);


    // SUPER ADMIN, SOLAMENTE PARA ROLE_SUPERADMIN EN WEB
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("treatment/")
    Call<List<TreatmentDto>> getTreatments();

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @PUT()
    Call<TreatmentDto> updateTreatment(@Url String url, @Body CreateUpdateTreatmentDto createUpdateTreatmentDto);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("treatment/")
    Call<TreatmentDto> addTreatment(@Body CreateUpdateTreatmentDto createUpdateTreatmentDto);


    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @DELETE("treatment/deactivate/{id}")
    Call<TreatmentDto> deleteTreatmentDto(@Path("id") int id);
}
