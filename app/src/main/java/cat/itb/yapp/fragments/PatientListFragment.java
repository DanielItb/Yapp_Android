package cat.itb.yapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cat.itb.yapp.R;
import cat.itb.yapp.adapters.PatientAdapter;
import cat.itb.yapp.models.patient.Patient;

public class PatientListFragment extends Fragment {
    private RecyclerView recyclerView;
    private PatientAdapter patientAdapter;
    private List<Patient> listPatient;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listPatient = this.getNamesTest();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_patient_list, container, false);


        recyclerView = v.findViewById(R.id.recyclerPatient);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        patientAdapter = new PatientAdapter(listPatient);

        recyclerView.setAdapter(patientAdapter);


        return v;
    }


    private List<Patient> getNamesTest(){
        return new ArrayList<Patient>(){{
            add(new Patient("Paco", 4));
            add(new Patient("Filomena", 8));
            add(new Patient("Angustias", 6));
            add(new Patient("Pepe", 3));
            add(new Patient("Benancio", 12));
            add(new Patient("Topacio", 6));
            add(new Patient("Benancio", 5));
            add(new Patient("Eustaquia tercera", 4));
            add(new Patient("Federica", 10));
            add(new Patient("Tiburcio", 9));
            add(new Patient("Pancracio", 9));
            add(new Patient("Benedito", 10));
            add(new Patient("Leopoldo", 7));


        }};
    }
}