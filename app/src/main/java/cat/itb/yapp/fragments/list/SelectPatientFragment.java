package cat.itb.yapp.fragments.list;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cat.itb.yapp.R;
import cat.itb.yapp.models.patient.Patient;
import cat.itb.yapp.models.patient.PatientDto;

public class SelectPatientFragment extends Fragment {
    private NavController navController;
    private RecyclerView recyclerView;
    private List<PatientDto> patientDtoList

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_select_patient, container, false);

        return v;
    }
}