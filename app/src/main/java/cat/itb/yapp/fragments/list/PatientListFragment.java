package cat.itb.yapp.fragments.list;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;
import java.util.List;

import cat.itb.yapp.R;
import cat.itb.yapp.activities.MainActivity;
import cat.itb.yapp.adapters.PatientAdapter;
import cat.itb.yapp.models.patient.PatientDto;
import cat.itb.yapp.retrofit.RetrofitHttp;
import cat.itb.yapp.utils.ErrorUtils;
import cat.itb.yapp.webservices.PatientWebServiceClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragmento controlador de la lista que muestra los pacientes.
 * @author David Lama, Kenneth Griñan, Daniel Acosta
 *
 */
public class PatientListFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<PatientDto> patientList;
    private NavController navController;
    private PatientAdapter adapter;
    private SearchView filterPatientSearchView;
    private TextView loadTextView;
    private ProgressBar loadProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navController = NavHostFragment.findNavController(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_patient_list, container, false);

        FloatingActionButton fab = v.findViewById(R.id.fabPatients);
        recyclerView = v.findViewById(R.id.recyclerPatient);
        filterPatientSearchView = v.findViewById(R.id.filterPatientSearchView);
        loadTextView = v.findViewById(R.id.loafingTextViewPatientList);
        loadProgressBar = v.findViewById(R.id.progressBarPatientList);

        getPatients();

        fab.setOnClickListener(this::fabClicked);

        if (patientList != null) setUpRecycler(recyclerView);

        filterPatientSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return v;
    }

    private void recyclerItemClicked(int position) {
        PatientListFragmentDirections.ActionPatientListFragmentToPatientFormFragment dir =
                PatientListFragmentDirections.actionPatientListFragmentToPatientFormFragment();
        dir.setPatientDto(patientList.get(position));

        filterPatientSearchView.setQuery("", true);
        navController.navigate(dir);
    }

    private void fabClicked(View view) {
        navController.navigate(R.id.action_patientListFragment_to_patientFormFragment);
    }
//
    private void setUpRecycler(RecyclerView recyclerView) {
        if (getContext() != null) {
            loadingGone();
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            adapter = new PatientAdapter(patientList, this::recyclerItemClicked);
            recyclerView.setAdapter(adapter);
        }
    }

    private void getPatients() {
        loadingVisible();
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
            call.enqueue(new Callback<List<PatientDto>>() {
                @Override
                public void onResponse(Call<List<PatientDto>> call, Response<List<PatientDto>> response) {
                    Log.e("patient", "onResponse okey");
                    if (response.isSuccessful()) {
                        Log.e("patient", "status response: " + response.code());

                        patientList = response.body();
                        System.out.println(Arrays.toString(patientList.toArray()));
                        setUpRecycler(recyclerView);
                    } else {
                        loadingGone();
                        Toast.makeText(getContext(), ErrorUtils.getErrorString(response.errorBody()), Toast.LENGTH_LONG).show();
                        Log.e("patient", "status response: " + response.code()); //401 Unauthorized
                    }
                }

                @Override
                public void onFailure(Call<List<PatientDto>> call, Throwable t) {
                    loadingGone();
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("patient", "onResponse onFailure");
                    Log.e("patient", "throwable.getMessage(): " + t.getMessage());
                    Log.e("patient", "call.toString(): " + call.toString());
                }
            });
        }

    }

    private void loadingGone(){
        loadProgressBar.setVisibility(View.GONE);
        loadTextView.setVisibility(View.GONE);
    }

    private void loadingVisible(){
        loadProgressBar.setVisibility(View.VISIBLE);
        loadTextView.setVisibility(View.VISIBLE);
    }

}