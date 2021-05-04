package cat.itb.yapp.webservices;

import cat.itb.yapp.models.auth.LoginDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PatientWebServiceClient {

    @POST("login")
    Call<LoginDto> login(@Body LoginDto loginDto);
}
