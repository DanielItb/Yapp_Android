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
    private NavController navController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
}