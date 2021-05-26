package cat.itb.yapp.retrofit;

import android.util.Log;
import android.widget.Toast;

import java.util.List;

import cat.itb.yapp.activities.MainActivity;
import cat.itb.yapp.models.clinic.ClinicDto;
import cat.itb.yapp.models.clinic.CreateUpdateClinicDto;
import cat.itb.yapp.models.patient.PatientDto;
import cat.itb.yapp.models.treatment.TreatmentDto;
import cat.itb.yapp.models.user.UserDto;
import cat.itb.yapp.utils.UtilsAuth;
import cat.itb.yapp.webservices.ClinicWebServiceClient;
import cat.itb.yapp.webservices.PatientWebServiceClient;
import cat.itb.yapp.webservices.TreatmentWebServiceClient;
import cat.itb.yapp.webservices.UserWebServiceClient;
import retrofit2.Call;
import retrofit2.Callback;

public class DatabaseUtils {
    private static RetrofitHttp retrofitHttp;
    private static ClinicWebServiceClient clinicWebServiceClient;

    public static RetrofitHttp getRetrofitHttp() {
        return retrofitHttp;
    }

    public static void setRetrofitHttp(RetrofitHttp retrofit) {
        DatabaseUtils.retrofitHttp = retrofit;
        clinicWebServiceClient = retrofitHttp.retrofit.create(ClinicWebServiceClient.class);
    }

    public static void getClinicById(Callback<ClinicDto> callback, int id) {
        Call<ClinicDto> call = clinicWebServiceClient.getClinicById(id);
        call.enqueue(callback);
    }

    public static void updateClinic(Callback<ClinicDto> callback, ClinicDto clinicDto) {
        CreateUpdateClinicDto createUpdateClinicDto = new CreateUpdateClinicDto(clinicDto.getName(),
                clinicDto.getAddress(), clinicDto.getPhoneNumber(), clinicDto.getEmail());

        Call<ClinicDto> call = clinicWebServiceClient.updateClinic(clinicDto.getId(), createUpdateClinicDto);
        call.enqueue(callback);
    }

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

    public static void getUsers(Callback<List<UserDto>> callback) {
        Log.e("user", "role admin?: " + UtilsAuth.getIsAdminRole(MainActivity.getUser().getRoles()));
        Log.e("user", "role user?: " + UtilsAuth.getIsUserRole(MainActivity.getUser().getRoles()));


        //TODO: if is admin go to view admin ...

        MainActivity.getUser().getRoles().forEach(rol -> {
            Log.e("user", "role: " + rol);
        });

        RetrofitHttp retrofitHttp = MainActivity.getRetrofitHttp();
        UserWebServiceClient userWebServiceClient = retrofitHttp.retrofit.create(UserWebServiceClient.class);

        Call<List<UserDto>> call;

        //CHECK USER ROLE
        if (UtilsAuth.getIsAdminRole(MainActivity.getUser().getRoles())) {
            String endpointUserRole = "user/clinic/" + MainActivity.getUserDto().getClinicId();
            call = userWebServiceClient.getUsers(endpointUserRole);
            Log.e("user", "all users in clinic");


        } else if (UtilsAuth.getIsUserRole(MainActivity.getUser().getRoles())) {
            Log.e("user", "estoy");
            call = null;


        } else {
            Toast.makeText(MainActivity.getActivity().getApplicationContext(), "error, usuario sin rol? ", Toast.LENGTH_SHORT).show();
            call = null;
        }

        if (call != null) {
            call.enqueue(callback);
        }
    }


    public static void getPatients(Callback<List<PatientDto>> callback) {
        //TODO: if is admin go to view admin ...
        Log.e("user", "id: " + MainActivity.getUser().getId());
        Log.e("user", "username: " + MainActivity.getUser().getUsername());

        MainActivity.getUser().getRoles().forEach(rol -> {
            Log.e("user", "role: " + rol);
        });


        RetrofitHttp retrofitHttp = MainActivity.getRetrofitHttp();
        PatientWebServiceClient patientWebServiceClient = retrofitHttp.retrofit.create(PatientWebServiceClient.class);

        Call<List<PatientDto>> call;

        String endpointUserRole = "patient/clinic/" + MainActivity.getUserDto().getClinicId();
        call = patientWebServiceClient.getPatientsByClinicId(endpointUserRole);
        Log.e("patient", "all patients in clinic");

        if (call != null) {
            call.enqueue(callback);
        }

    }

}
