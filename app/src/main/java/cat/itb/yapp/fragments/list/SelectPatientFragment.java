package cat.itb.yapp.fragments.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cat.itb.yapp.R;
import cat.itb.yapp.activities.MainActivity;
import cat.itb.yapp.adapters.PatientAdapter;
import cat.itb.yapp.models.patient.PatientDto;
import cat.itb.yapp.utils.ErrorUtils;
import cat.itb.yapp.webservices.PatientWebServiceClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * Fragmento controlador de la lista que muestra los pacientes para ser seleccionados.
 * @author David Lama, Kenneth Griñan, Daniel Acosta
 *
 */
public class SelectPatientFragment extends Fragment {
    private NavController navController;
    private RecyclerView recyclerView;
    private List<PatientDto> patientDtoList = null;
    private SearchView filterPatientSearchView;
    private PatientAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navController = NavHostFragment.findNavController(this);
        getPatients();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_select_patient, container, false);

        filterPatientSearchView = v.findViewById(R.id.filterSelectPatientSearchView);

        recyclerView = v.findViewById(R.id.recyclerSelectPatient);

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
        Bundle result = new Bundle();
        PatientDto patient = patientDtoList.get(position);

        result.putInt("patientId", patient.getId());
        result.putString("fullName", patient.getName() + " " + patient.getSurname());

        getParentFragmentManager().setFragmentResult("patientId", result);
        navController.popBackStack();
    }

    private void setUpRecycler(RecyclerView recyclerView) {
        if (getContext() != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            adapter = new PatientAdapter(patientDtoList, this::recyclerItemClicked);
            recyclerView.setAdapter(adapter);
        }
    }

    private void getPatients() {
        PatientWebServiceClient webServiceClient = MainActivity.getRetrofitHttp().retrofit
                .create(PatientWebServiceClient.class);

        Call<List<PatientDto>> call = webServiceClient
                .getPatientsByActiveAndClinicEntityId("patient/true/clinic/" +
                        MainActivity.getUserDto().getClinicId());

        call.enqueue(new Callback<List<PatientDto>>() {
            @Override
            public void onResponse(Call<List<PatientDto>> call, Response<List<PatientDto>> response) {
                if (response.isSuccessful()) {
                    patientDtoList = response.body();
                    setUpRecycler(recyclerView);
                } else {
                    Toast.makeText(getContext(), ErrorUtils.getErrorString(response.errorBody()), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<List<PatientDto>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

}