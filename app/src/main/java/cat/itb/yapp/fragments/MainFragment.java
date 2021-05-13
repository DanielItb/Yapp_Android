package cat.itb.yapp.fragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cat.itb.yapp.R;
import cat.itb.yapp.activities.MainActivity;
import cat.itb.yapp.models.mts.MtsDto;
import cat.itb.yapp.retrofit.RetrofitHttp;
import cat.itb.yapp.utils.UtilsAuth;
import cat.itb.yapp.webservices.MtsServiceClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainFragment extends Fragment {
    static List<MtsDto> listMts = null;
    private NavController navController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMts();
        navController = NavHostFragment.findNavController(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CardView usersCardView, patientsCardView, mtsCardView, reportCardView, treatmentCardView,
                centerCardView;
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        usersCardView = v.findViewById(R.id.usersCardViewButton);
        patientsCardView = v.findViewById(R.id.patientsCardViewButton);
        mtsCardView = v.findViewById(R.id.mtsCardViewButton);
        reportCardView = v.findViewById(R.id.reportCardViewButton);
        treatmentCardView = v.findViewById(R.id.treatmentCardViewButton);
        centerCardView = v.findViewById(R.id.centerCardViewButton);

        usersCardView.setOnClickListener(v1 -> navController.navigate(R.id.action_mainFragment_to_userListFragment));
        patientsCardView.setOnClickListener(v1 -> navController.navigate(R.id.action_mainFragment_to_patientListFragment));
        mtsCardView.setOnClickListener(v1 -> navController.navigate(R.id.action_mainFragment_to_calendarFragment));
        reportCardView.setOnClickListener(v1 -> navController.navigate(R.id.action_mainFragment_to_reportListFragment));
        treatmentCardView.setOnClickListener(v1 -> navController.navigate(R.id.action_mainFragment_to_treatmentListFragment));

        return v;
    }

    /*Carga de la bd la lista con las citas del especialista logeado*/
    public void getMts() {
        //TODO: if is admin go to view admin ...
        Log.e("user", "id: " + MainActivity.getUser().getId());
        Log.e("user", "username: " + MainActivity.getUser().getUsername());

        MainActivity.getUser().getRoles().forEach(rol -> {
            Log.e("user", "role: " + rol);
        });

        final List<MtsDto>[] mtsDtoList = new List[]{new ArrayList<>()};


        RetrofitHttp retrofitHttp = new RetrofitHttp();
        MtsServiceClient mtsServiceClient = retrofitHttp.retrofit.create(MtsServiceClient.class);

        Call<List<MtsDto>> call = null;


        Long specialistId = MainActivity.getUser().getId().longValue();
        //CHECK USER ROLE
        if (UtilsAuth.getIsAdminRole(MainActivity.getUser().getRoles())) {

            String endpointUserRole = "medicalsheet/";
            call = mtsServiceClient.getUsers(endpointUserRole);
            Log.e("mts", "all mts in clinic");

        } else if (UtilsAuth.getIsUserRole(MainActivity.getUser().getRoles())) {

            String endpointUserRole = "medicalsheet/specialist/" + specialistId;
            call = mtsServiceClient.getMtsBySpecialistId(endpointUserRole);
            Log.e("mts", "all mts by specialist");

        } else {
            Toast.makeText(MainActivity.getActivity().getApplicationContext(), "error, usuario sin rol? ", Toast.LENGTH_SHORT).show();
            call = null;
        }

        if (call != null) {
            call.enqueue(new Callback<List<MtsDto>>() {
                @Override
                public void onResponse(Call<List<MtsDto>> call, Response<List<MtsDto>> response) {
                    Log.e("mts", "onResponse okey");
                    if (response.isSuccessful()) {
                        Log.e("mts", "status response: " + response.code());

                        listMts = response.body();


//                        setUpRecycler(recyclerView);
//                        UserAdapter adapter = new UserAdapter(listUsers);
//                        recyclerView.setAdapter(adapter);

                        mtsDtoList[0].forEach(t -> {
                            Log.e("mts", "status response: " + t.toString());
                        });

                    } else {
                        Toast.makeText(MainActivity.getActivity().getApplicationContext(), "error get mts by specialistId", Toast.LENGTH_SHORT).show();
                        Log.e("mts", "status response: " + response.code()); //401 Unauthorized
                    }
                }

                @Override
                public void onFailure(Call<List<MtsDto>> call, Throwable t) {
                    Log.e("mts", "onResponse onFailure");
                    Log.e("mts", "throwable.getMessage(): " + t.getMessage());
                    Log.e("mts", "call.toString(): " + call.toString());
                }
            });
        }
    }
}