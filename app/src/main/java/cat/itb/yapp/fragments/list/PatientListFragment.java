package cat.itb.yapp.fragments.list;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import cat.itb.yapp.R;
import cat.itb.yapp.adapters.PatientAdapter;
import cat.itb.yapp.models.patient.Patient;
import cat.itb.yapp.models.patient.PatientDto;

public class PatientListFragment extends Fragment {
    private RecyclerView recyclerView;
    private PatientAdapter patientAdapter;
    private List<PatientDto> listPatient;
    private NavController navController;

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

        fab.setOnClickListener(this::fabClicked);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        patientAdapter = new PatientAdapter(listPatient, this::recyclerItemClicked);
        recyclerView.setAdapter(patientAdapter);


        return v;
    }

    private void recyclerItemClicked(int i) {
        //TODO open form
    }

    private void fabClicked(View view) {
        navController.navigate(R.id.action_patientListFragment_to_patientFormFragment);
    }

}