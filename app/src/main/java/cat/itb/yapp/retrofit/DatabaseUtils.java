package cat.itb.yapp.retrofit;

import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cat.itb.yapp.activities.MainActivity;
import cat.itb.yapp.models.treatment.TreatmentDto;
import cat.itb.yapp.utils.UtilsAuth;
import cat.itb.yapp.webservices.TreatmentWebServiceClient;
import retrofit2.Call;
import retrofit2.Callback;

public class DatabaseUtils {
    public static void getTreatments(Callback<List<TreatmentDto>> callback) {
        //TODO: if is admin go to view admin ...
        Log.e("treatment", "id: " + MainActivity.getUser().getId());
        Log.e("treatment", "username: " + MainActivity.getUser().getUsername());

        MainActivity.getUser().getRoles().forEach(rol -> {
            Log.e("treatment", "role: " + rol);
        });

        final List<TreatmentDto>[] treatmentDtoList = new List[]{new ArrayList<>()};


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
