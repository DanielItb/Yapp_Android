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
import cat.itb.yapp.adapters.ReportAdapter;
import cat.itb.yapp.models.report.ReportDto;
import cat.itb.yapp.retrofit.RetrofitHttp;
import cat.itb.yapp.utils.ErrorUtils;
import cat.itb.yapp.utils.UtilsAuth;
import cat.itb.yapp.webservices.ReportServiceClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragmento controlador de la lista que muestra los informes.
 * @author David Lama, Kenneth Griñan, Daniel Acosta
 *
 */
public class ReportListFragment extends Fragment {
    private NavController navController;
    private List<ReportDto> reportList = null;
    private ReportAdapter adapter;
    private SearchView filterReportSearchView;
    private TextView loadTextView;
    private ProgressBar loadProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_report_list, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.recyclerReport);
        filterReportSearchView = v.findViewById(R.id.filterReportSearchView);
        loadTextView = v.findViewById(R.id.loafingTextViewReportList);
        loadProgressBar = v.findViewById(R.id.progressBarReportList);
        getReportsIntroRecycler(recyclerView);
        FloatingActionButton fab = v.findViewById(R.id.fabReport);

        fab.setOnClickListener(this::fabClicked);



        if (reportList != null) setUpRecycler(recyclerView);
        filterReportSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null) adapter.getFilter().filter(newText);
                return false;
            }
        });

        return v;
    }

    private void fabClicked(View view) {
        navController.navigate(R.id.action_reportListFragment_to_reportFormFragment);
    }


    private void setUpRecycler(RecyclerView recyclerView) {
        if (getContext() != null) {
            loadingGone();
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            adapter = new ReportAdapter(reportList, ReportListFragment.this::loadForm);
            recyclerView.setAdapter(adapter);
        }
    }

    private void loadForm(int position) {
        ReportListFragmentDirections.ActionReportListFragmentToReportFormFragment dir =
                ReportListFragmentDirections.actionReportListFragmentToReportFormFragment();
        dir.setReportDto(reportList.get(position));

        filterReportSearchView.setQuery("", true);
        navController.navigate(dir);
    }

    private void getReportsIntroRecycler(RecyclerView recyclerView) {
        loadingVisible();
        Log.e("user", "id: "+ MainActivity.getUser().getId());
        Log.e("user", "username: "+MainActivity.getUser().getUsername());

        MainActivity.getUser().getRoles().forEach(rol -> Log.e("user", "role: "+rol));

        RetrofitHttp retrofitHttp = new RetrofitHttp();
        ReportServiceClient treatmentWebServiceClient = retrofitHttp.retrofit.create(ReportServiceClient.class);

        Call<List<ReportDto>> call;

        Long specialistId = MainActivity.getUser().getId().longValue();
        //CHECK USER ROLE
        if (UtilsAuth.getIsAdminRole(MainActivity.getUser().getRoles())) {

            String endpointUserRole = "report/clinic/" +  MainActivity.getUserDto().getClinicId();
            call = treatmentWebServiceClient.getReportsByClinicId(endpointUserRole);
            Log.e("report", "all reports in clinic");

        } else if (UtilsAuth.getIsUserRole(MainActivity.getUser().getRoles())) {

            String endpointUserRole = "report/specialist/" +  specialistId;
            call = treatmentWebServiceClient.getReportsBySpecialistId(endpointUserRole);
            Log.e("report", "all reports by specialist");

        } else {
            Toast.makeText(MainActivity.getActivity().getApplicationContext(), "error, usuario sin rol? ", Toast.LENGTH_SHORT).show();
            call = null;
        }

        if (call != null) {
            call.enqueue(new Callback<List<ReportDto>>() {
                @Override
                public void onResponse(Call<List<ReportDto>> call, Response<List<ReportDto>> response) {
                    Log.e("treatment", "onResponse okey");
                    if (response.isSuccessful()) {
                        Log.e("treatment", "status response: " + response.code());

                        reportList = response.body();
                        setUpRecycler(recyclerView);
                    } else {
                        loadingGone();
                        Toast.makeText(getContext(), ErrorUtils.getErrorString(response.errorBody()), Toast.LENGTH_LONG).show();
                        Log.e("report", "status response: " + response.code()); //401 Unauthorized
                    }
                }

                @Override
                public void onFailure(Call<List<ReportDto>> call, Throwable t) {
                    loadingGone();
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("report", "onResponse onFailure");
                    Log.e("report", "throwable.getMessage(): "+t.getMessage());
                    Log.e("report", "call.toString(): "+call.toString());
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