package cat.itb.yapp.webservices;

import java.util.List;

import cat.itb.yapp.models.auth.LoginDto;
import cat.itb.yapp.models.report.CreateReportDto;
import cat.itb.yapp.models.report.ReportDto;
import cat.itb.yapp.models.treatment.TreatmentDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ReportServiceClient {
    // ROLE_ADMIN
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET()
    Call<List<ReportDto>> getReportsByClinicId(@Url String url);

    // ROLE_USER
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET()
    Call<List<ReportDto>> getReportsBySpecialistId(@Url String url);

    @POST("report/")
    Call<CreateReportDto> addReport();
}
