package cat.itb.yapp.retrofit;

import android.util.Log;
import android.widget.Toast;

import java.util.List;

import cat.itb.yapp.activities.MainActivity;
import cat.itb.yapp.models.clinic.ClinicDto;
import cat.itb.yapp.models.clinic.CreateUpdateClinicDto;
import cat.itb.yapp.models.treatment.TreatmentDto;
import cat.itb.yapp.utils.UtilsAuth;
import cat.itb.yapp.webservices.ClinicWebServiceClient;
import cat.itb.yapp.webservices.TreatmentWebServiceClient;
import retrofit2.Call;
import retrofit2.Callback;
/**
 * Clase utils varios de conexión con la base de datos
 * @author David Lama, Kenneth Griñan, Daniel Acosta
 */
public class DatabaseUtils {
    private static RetrofitHttp retrofitHttp;
    private static ClinicWebServiceClient clinicWebServiceClient;

    /**
     * Método que retorna un objeto RetrofirHttp.
     * @return RetrofitHttp.
     */
    public static RetrofitHttp getRetrofitHttp() {
        return retrofitHttp;
    }

    /**
     * Método que setea un objeto RetrofitHttp.
     * @param retrofit Objeto RetrofitHttp.
     */
    public static void setRetrofitHttp(RetrofitHttp retrofit) {
        DatabaseUtils.retrofitHttp = retrofit;
        clinicWebServiceClient = retrofitHttp.retrofit.create(ClinicWebServiceClient.class);
    }

    /**
     * Recoge en un objeto Call de ClinicDto una clínica por su id.
     * @param callback Objeto Callback
     * @param id int
     */
    public static void getClinicById(Callback<ClinicDto> callback, int id) {
        Call<ClinicDto> call = clinicWebServiceClient.getClinicById(id);
        call.enqueue(callback);
    }

    /**
     * Método que actualiza los datos de la clñinica.
     * @param callback Objeto Callback
     * @param clinicDto Objeto CreateUpdateClinicDto
     * @param id int
     */
    public static void updateClinic(Callback<CreateUpdateClinicDto> callback, CreateUpdateClinicDto clinicDto, int id) {
        Call<CreateUpdateClinicDto> call = clinicWebServiceClient.updateClinic(id, clinicDto);
        call.enqueue(callback);
    }

    /**
     * Método que carga una lista con los tratamientos recogidos del backend
     * @param callback Objeto Callback
     */
    public static void getTreatments(Callback<List<TreatmentDto>> callback) {
        //TODO: if is admin go to view admin ...
        Log.e("treatment", "id: " + MainActivity.getUser().getId());
        Log.e("treatment", "username: " + MainActivity.getUser().getUsername());

        MainActivity.getUser().getRoles().forEach(rol -> {
            Log.e("treatment", "role: " + rol);
        });


        RetrofitHttp retrofitHttp = MainActivity.getRetrofitHttp();
        TreatmentWebServiceClient treatmentWebServiceClient = retrofitHttp.retrofit.create(TreatmentWebServiceClient.class);

        Call<List<TreatmentDto>> call;

        Long specialistId = MainActivity.getUser().getId().longValue();
        //CHECK USER ROLE
        if (UtilsAuth.getIsAdminRole(MainActivity.getUser().getRoles())) {
            String endpointUserRole = "treatment/clinic/" + MainActivity.getUserDto().getClinicId();
            call = treatmentWebServiceClient.getTreatmentsByClinicId(endpointUserRole);
            Log.e("treatment", "all treatments in clinic");

        } else if (UtilsAuth.getIsUserRole(MainActivity.getUser().getRoles())) {
            String endpointUserRole = "treatment/specialist/" + specialistId;
            call = treatmentWebServiceClient.getTreatmentsBySpecialistId(endpointUserRole);
            Log.e("treatment", "all treatments by specialist");

        } else {
            Toast.makeText(MainActivity.getActivity().getApplicationContext(), "error, usuario sin rol? ", Toast.LENGTH_SHORT).show();
            call = null;
        }

        if (call != null) {
            call.enqueue(callback);
        }

    }

}
