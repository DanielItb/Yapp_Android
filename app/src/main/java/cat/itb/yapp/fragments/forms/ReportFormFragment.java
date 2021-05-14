package cat.itb.yapp.fragments.forms;

import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import cat.itb.yapp.R;
import cat.itb.yapp.models.report.ReportDto;

public class ReportFormFragment extends Fragment {
    private NavController navController;
    private TextInputEditText editTextDiagnosis, editTextObjectives;
    private MaterialButton buttonPatient, buttonSpecialist, buttonCancel, buttonSave;
    private SwitchCompat switchActive;
    private ReportDto reportDto = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_report_form, container, false);

        editTextDiagnosis = v.findViewById(R.id.diagnosisReportEditText);
        editTextObjectives = v.findViewById(R.id.objetivesReportEditText);
        buttonPatient = v.findViewById(R.id.selectPatientReportButton);
        buttonSpecialist = v.findViewById(R.id.selectSpecialistReportButton);
        buttonCancel = v.findViewById(R.id.cancelReportButton);
        buttonSave = v.findViewById(R.id.saveReportButton);
        switchActive = v.findViewById(R.id.simpleSwitchReport);


        return v;
    }
}