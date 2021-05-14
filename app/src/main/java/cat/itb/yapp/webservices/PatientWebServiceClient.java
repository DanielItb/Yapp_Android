package cat.itb.yapp.webservices;

import java.util.List;

import cat.itb.yapp.models.patient.PatientDto;
import cat.itb.yapp.models.report.ReportDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Url;

public interface PatientWebServiceClient {

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET()
    Call<List<PatientDto>> getPatientsByActiveAndClinicEntityId(@Url String url);


    // ROLE_ADMIN
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET()
    Call<List<PatientDto>> getPatientsByClinicId(@Url String url);

    // ROLE_USER
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET()
    Call<List<PatientDto>> getPatientsBySpecialistId(@Url String url);
}
