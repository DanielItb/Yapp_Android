package cat.itb.yapp.fragments.list;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import cat.itb.yapp.R;
import cat.itb.yapp.activities.MainActivity;
import cat.itb.yapp.adapters.TreatmentAdapter;
import cat.itb.yapp.models.treatment.TreatmentDto;
import cat.itb.yapp.utils.UtilsAuth;
import cat.itb.yapp.webservices.TreatmentWebServiceClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectTreatmentFragment extends Fragment {
    private NavController navController;
    private RecyclerView recyclerView;
    private List<TreatmentDto> treatmentDtoList = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navController = NavHostFragment.findNavController(this);
        getTreatments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_select_treatment, container, false);

        recyclerView = v.findViewById(R.id.recyclerSelectTreatment);

        return v;
    }

    private void getTreatments() {
        TreatmentWebServiceClient treatmentWebServiceClient = MainActivity.getRetrofitHttp().retrofit
                .create(TreatmentWebServiceClient.class);

        Call<List<TreatmentDto>> call;

        Long specialistId = MainActivity.getUser().getId().longValue();
        //CHECK USER ROLE
        if (UtilsAuth.getIsAdminRole(MainActivity.getUser().getRoles())) {

            String endpointUserRole = "treatment/clinic/" +  MainActivity.getUserDto().getClinicId();
            call = treatmentWebServiceClient.getTreatmentsByClinicId(endpointUserRole);
            Log.e("treatment", "all treatments in clinic");

        } else if (UtilsAuth.getIsUserRole(MainActivity.getUser().getRoles())) {

            String endpointUserRole = "treatment/specialist/" +  specialistId;
            call = treatmentWebServiceClient.getTreatmentsBySpecialistId(endpointUserRole);
            Log.e("treatment", "all treatments by specialist");

        } else {
            Toast.makeText(MainActivity.getActivity().getApplicationContext(), "error, usuario sin rol? ", Toast.LENGTH_SHORT).show();
            call = null;
        }

        if (call != null) {
            call.enqueue(new Callback<List<TreatmentDto>>() {
                @Override
                public void onResponse(Call<List<TreatmentDto>> call, Response<List<TreatmentDto>> response) {
                    Log.e("treatment", "onResponse okey");
                    if (response.isSuccessful()) {
                        Log.e("treatment", "status response: " + response.code());

                        treatmentDtoList = response.body();
                        setUpRecycler(recyclerView);

                    }else {
                        Toast.makeText(MainActivity.getActivity().getApplicationContext(), "error get treatment by specialistId", Toast.LENGTH_SHORT).show();
                        Log.e("treatment", "status response: " + response.code()); //401 Unauthorized
                    }
                }

                @Override
                public void onFailure(Call<List<TreatmentDto>> call, Throwable t) {
                    Log.e("treatment", "onResponse onFailure");
                    Log.e("treatment", "throwable.getMessage(): "+t.getMessage());
                    Log.e("treatment", "call.toString(): "+call.toString());
                }
            });
        }
    }

    private void setUpRecycler(RecyclerView recyclerView) {
        if (getContext() != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            TreatmentAdapter adapter = new TreatmentAdapter(treatmentDtoList, this::recyclerItemClicked);
            recyclerView.setAdapter(adapter);
        }
    }

    private void recyclerItemClicked(int i) {
        Bundle result = new Bundle();
        TreatmentDto treatmentDto = treatmentDtoList.get(i);

        result.putString("treatmentId", treatmentDto.getId());
        result.putString("reason", treatmentDto.getReason());

        getParentFragmentManager().setFragmentResult("treatment", result);
        navController.popBackStack();
    }

}