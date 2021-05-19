package cat.itb.yapp.fragments.list;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import cat.itb.yapp.R;
import cat.itb.yapp.activities.MainActivity;
import cat.itb.yapp.adapters.PatientAdapter;
import cat.itb.yapp.models.patient.PatientDto;
import cat.itb.yapp.retrofit.RetrofitHttp;
import cat.itb.yapp.utils.UtilsAuth;
import cat.itb.yapp.webservices.PatientWebServiceClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientListFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<PatientDto> patientList;
    private NavController navController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navController = NavHostFragment.findNavController(this);
//        getPatients();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_patient_list, container, false);
        patientList = null;
        getPatients();
        FloatingActionButton fab = v.findViewById(R.id.fabPatients);
        recyclerView = v.findViewById(R.id.recyclerPatient);

        fab.setOnClickListener(this::fabClicked);

        if (patientList != null) setUpRecycler(recyclerView);

        return v;
    }

    private void recyclerItemClicked(int position) {
        PatientListFragmentDirections.ActionPatientListFragmentToPatientFormFragment dir =
                PatientListFragmentDirections.actionPatientListFragmentToPatientFormFragment();
        dir.setPatientDto(patientList.get(position));

        navController.navigate(dir);
    }

    private void fabClicked(View view) {
        navController.navigate(R.id.action_patientListFragment_to_patientFormFragment);
    }
//
    private void setUpRecycler(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        PatientAdapter adapter = new PatientAdapter(patientList, this::recyclerItemClicked);
        recyclerView.setAdapter(adapter);

    }

    public void getPatients() {
        //TODO: if is admin go to view admin ...
        Log.e("user", "id: " + MainActivity.getUser().getId());
        Log.e("user", "username: " + MainActivity.getUser().getUsername());

        MainActivity.getUser().getRoles().forEach(rol -> {
            Log.e("user", "role: " + rol);
        });

        final List<PatientDto>[] patientDtoList = new List[]{new ArrayList<>()};


        RetrofitHttp retrofitHttp = new RetrofitHttp();
        PatientWebServiceClient patientWebServiceClient = retrofitHttp.retrofit.create(PatientWebServiceClient.class);

        Call<List<PatientDto>> call;

        Long specialistId = MainActivity.getUser().getId().longValue();
        //CHECK USER ROLE
        if (UtilsAuth.getIsAdminRole(MainActivity.getUser().getRoles())) {

            String endpointUserRole = "patient/clinic/" + MainActivity.getUserDto().getClinicId();
            call = patientWebServiceClient.getPatientsByClinicId(endpointUserRole);
            Log.e("patient", "all patients in clinic");

        } else if (UtilsAuth.getIsUserRole(MainActivity.getUser().getRoles())) {

            String endpointUserRole = "patient/" + specialistId;
            call = patientWebServiceClient.getPatientsBySpecialistId(endpointUserRole);
            Log.e("patient", "all patients by specialist");

        } else {
            Toast.makeText(MainActivity.getActivity().getApplicationContext(), "error, usuario sin rol? ", Toast.LENGTH_SHORT).show();
            call = null;
        }

        if (call != null) {
            call.enqueue(new Callback<List<PatientDto>>() {
                @Override
                public void onResponse(Call<List<PatientDto>> call, Response<List<PatientDto>> response) {
                    Log.e("patient", "onResponse okey");
                    if (response.isSuccessful()) {
                        Log.e("patient", "status response: " + response.code());

                        patientList = response.body();
                        setUpRecycler(recyclerView);

                        patientDtoList[0].forEach(t -> {
                            Log.e("patient", "status response: " + t.toString());
                        });

                    } else {
                        Toast.makeText(MainActivity.getActivity().getApplicationContext(), "error get patients by specialistId", Toast.LENGTH_SHORT).show();
                        Log.e("patient", "status response: " + response.code()); //401 Unauthorized
                    }
                }

                @Override
                public void onFailure(Call<List<PatientDto>> call, Throwable t) {
                    Log.e("patient", "onResponse onFailure");
                    Log.e("patient", "throwable.getMessage(): " + t.getMessage());
                    Log.e("patient", "call.toString(): " + call.toString());
                }
            });
        }

    }

}