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

import java.util.List;

import cat.itb.yapp.R;
import cat.itb.yapp.activities.MainActivity;
import cat.itb.yapp.adapters.TreatmentAdapter;
import cat.itb.yapp.models.treatment.TreatmentDto;
import cat.itb.yapp.retrofit.RetrofitHttp;
import cat.itb.yapp.utils.ErrorUtils;
import cat.itb.yapp.utils.UtilsAuth;
import cat.itb.yapp.webservices.TreatmentWebServiceClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragmento controlador de la lista que muestra los tratamientos.
 * @author David Lama, Kenneth Griñan, Daniel Acosta
 *
 */
public class TreatmentListFragment extends Fragment {
    private RecyclerView recyclerView;
    private NavController navController;
    private List<TreatmentDto> treatmentList = null;
    private TreatmentAdapter adapter;
    private SearchView filterTreatmentSearchView;
    private TextView loadTextView;
    private ProgressBar loadProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_treatment_list, container, false);
        recyclerView = v.findViewById(R.id.recyclerTreatment);
        loadTextView = v.findViewById(R.id.loafingTextViewTreatmentList);
        loadProgressBar = v.findViewById(R.id.progressBarTreatmentList);
        getTreatments();
        FloatingActionButton fab = v.findViewById(R.id.fabTreatment);
        filterTreatmentSearchView = v.findViewById(R.id.filterTreatmentSearchView);


        fab.setOnClickListener(this::fabClicked);

        if (treatmentList != null) setUpRecycler(recyclerView);

        filterTreatmentSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null) {
                    adapter.getFilter().filter(newText);
                }

                return false;
            }
        });

        return v;
    }

    private void fabClicked(View view) {
        navController.navigate(R.id.action_treatmentListFragment_to_treatmentFormFragment);
    }

    private void setUpRecycler(RecyclerView recyclerView) {
        if (getContext() != null) {
            loadingGone();
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            adapter = new TreatmentAdapter(treatmentList, TreatmentListFragment.this::loadForm);
            recyclerView.setAdapter(adapter);
        }
    }

    private void loadForm(int position) {
        TreatmentListFragmentDirections.ActionTreatmentListFragmentToTreatmentFormFragment dir =
                TreatmentListFragmentDirections.actionTreatmentListFragmentToTreatmentFormFragment();
        dir.setTreatmentDto(treatmentList.get(position));

        filterTreatmentSearchView.setQuery("", true);
        navController.navigate(dir);
    }

    private void getTreatments() {
        loadingVisible();
        Log.e("treatment", "id: "+ MainActivity.getUser().getId());
        Log.e("treatment", "username: "+MainActivity.getUser().getUsername());

        MainActivity.getUser().getRoles().forEach(rol -> {
            Log.e("treatment", "role: "+rol);
        });

        RetrofitHttp retrofitHttp = new RetrofitHttp();
        TreatmentWebServiceClient treatmentWebServiceClient = retrofitHttp.retrofit.create(TreatmentWebServiceClient.class);

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

                        treatmentList = response.body();
                        setUpRecycler(recyclerView);

                    }else {
                        loadingGone();
                        Toast.makeText(getContext(), ErrorUtils.getErrorString(response.errorBody()), Toast.LENGTH_LONG).show();
                        Log.e("treatment", "status response: " + response.code()); //401 Unauthorized
                    }
                }

                @Override
                public void onFailure(Call<List<TreatmentDto>> call, Throwable t) {
                    loadingGone();
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("treatment", "onResponse onFailure");
                    Log.e("treatment", "throwable.getMessage(): "+t.getMessage());
                    Log.e("treatment", "call.toString(): "+call.toString());
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