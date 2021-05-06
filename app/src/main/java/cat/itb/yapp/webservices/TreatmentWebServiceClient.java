package cat.itb.yapp.webservices;

import java.util.List;

import cat.itb.yapp.models.treatment.TreatmentDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
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
}
