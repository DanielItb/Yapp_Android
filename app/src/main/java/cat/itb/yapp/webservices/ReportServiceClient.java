package cat.itb.yapp.webservices;

import java.util.List;

import cat.itb.yapp.models.patient.PatientDto;
import cat.itb.yapp.models.report.CreateUpdateReportDto;
import cat.itb.yapp.models.report.ReportDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Interface con las peticiones rest al backend del objeto Report.
 * @author David Lama, Kenneth Griñan, Daniel Acosta
 */
public interface ReportServiceClient {
    // ROLE_ADMIN
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET()
    Call<List<ReportDto>> getReportsByClinicId(@Url String url);

    // ROLE_USER
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET()
    Call<List<ReportDto>> getReportsBySpecialistId(@Url String url);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("report/")
    Call<ReportDto> addReport(@Body CreateUpdateReportDto createUpdateReportDto);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @PUT()
    Call<ReportDto> updateDto(@Url String url, @Body CreateUpdateReportDto createUpdateReportDto);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @DELETE("report/deactivate/{id}")
    Call<ReportDto> deleteReportDto(@Path("id") int id);

}
