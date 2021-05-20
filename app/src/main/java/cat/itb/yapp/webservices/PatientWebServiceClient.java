package cat.itb.yapp.webservices;

import java.util.List;

import cat.itb.yapp.models.patient.CreateUpdatePatientDto;
import cat.itb.yapp.models.patient.PatientDto;
import cat.itb.yapp.models.report.CreateUpdateReportDto;
import cat.itb.yapp.models.report.ReportDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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


    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("patient/")
    Call<PatientDto> addPatient(@Body CreateUpdatePatientDto createUpdatePatientDto);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @PUT()
    Call<PatientDto> updateDto(@Url String url, @Body CreateUpdatePatientDto createUpdatePatientDto);
}
