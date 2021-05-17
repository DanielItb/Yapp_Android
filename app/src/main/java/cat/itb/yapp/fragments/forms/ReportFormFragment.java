package cat.itb.yapp.fragments.forms;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import cat.itb.yapp.models.report.UpdateReportDto;
import cat.itb.yapp.models.treatment.TreatmentDto;

public class ReportFormFragment extends Fragment {
    private NavController navController;
    private TextInputEditText editTextDiagnosis, editTextObjectives, editTextSpecialist;
    private MaterialButton buttonPatient, buttonSpecialist, buttonCancel, buttonSave;
    private SwitchCompat switchActive;
    private ReportDto reportDto = null;
    private boolean editing;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(this);

        final FragmentManager fragmentManager = getParentFragmentManager();

        fragmentManager.setFragmentResultListener("userId", this, (requestKey, bundle) -> {
            reportDto.setSpecialistId(String.valueOf(bundle.getLong("userId"))); //TODO Remove parse
            String fullName = bundle.getString("fullName");
            String specialistType = bundle.getString("specialistType");

            reportDto.setSpecialistFullName(fullName);
            reportDto.setSpecialistType(specialistType);

            buttonSpecialist.setText(fullName);
            editTextSpecialist.setText(specialistType);
        });

        fragmentManager.setFragmentResultListener("patientId", this, (requestKey, bundle) -> {
            reportDto.setPatientId(String.valueOf(bundle.getInt("patientId"))); //TODO Remove parse
            String fullName = bundle.getString("fullName");
            reportDto.setPatientFullName(fullName);
            buttonPatient.setText(fullName);
        });
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
        editTextSpecialist = v.findViewById(R.id.specialistTypeReportEditText);


        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //If starts for first time
        if (reportDto == null) {
            reportDto = ReportFormFragmentArgs.fromBundle(getArguments()).getReportDto();
            if (reportDto != null) { //If editing
                editing = true;
                fillUpInfoInLayout(reportDto);
            } else { // If new
                reportDto = new ReportDto();
                editing = false;
            }
        } else { //If new
            fillUpInfoInLayout(reportDto);
        }

        buttonCancel.setOnClickListener(v -> navController.popBackStack());
        buttonSave.setOnClickListener(v -> {
            if (allRequiredCampsSet()) save();
        });

        buttonSpecialist.setOnClickListener(v -> navController.navigate(R.id.action_reportFormFragment_to_selectUserFragment));
        buttonPatient.setOnClickListener(v -> navController.navigate(R.id.action_reportFormFragment_to_selectPatientFragment));
    }

    private void save() {
        if (editing) saveEdited();
        else saveNew();

    }

    private void saveNew() {
        final UpdateReportDto updateReportDto = getUpdateReportDto(reportDto);
    }

    private void saveEdited() {
    }

    private UpdateReportDto getUpdateReportDto(ReportDto reportDto) {
        UpdateReportDto updateReportDto = new UpdateReportDto();

        updateReportDto.setActive(switchActive.getShowText());
        updateReportDto.setDate(reportDto.getDate());//todo Date?
        updateReportDto.setDiagnosis(reportDto.getDiagnosis());
        updateReportDto.setDiagnosis(reportDto.getDiagnosis());


        //TODO finish this
        return updateReportDto;
    }

    private boolean allRequiredCampsSet() {
        boolean allGood = true;
        String patientId = reportDto.getPatientId();
        String specialistId = reportDto.getSpecialistId();
        CharSequence errorMsg = getText(R.string.must_fill);

        if (patientId == null) {
            allGood = false;
            buttonPatient.setError(errorMsg);
        } if (specialistId == null) {
            allGood = false;
            buttonSpecialist.setError(errorMsg);
        } if (editTextDiagnosis.getText().toString().isEmpty()) {
            allGood= false;
            editTextDiagnosis.setError(errorMsg);
        } if(editTextObjectives.getText().toString().isEmpty()) {
            allGood= false;
            editTextObjectives.setError(errorMsg);
        }

        return allGood;
    }

    private void fillUpInfoInLayout(ReportDto reportDto) {
        String patientName = reportDto.getPatientFullName();
        String specialistName = reportDto.getSpecialistFullName();
        String specialistType = reportDto.getSpecialistType();
        String diagnosis = reportDto.getDiagnosis();
        String objectives = reportDto.getObjectives();

        if (patientName != null) buttonPatient.setText(patientName);
        if (specialistName != null) buttonSpecialist.setText(specialistName);
        if (specialistType != null) editTextSpecialist.setText(reportDto.getSpecialistType());
        if (diagnosis != null) editTextDiagnosis.setText(diagnosis);
        if (objectives != null) editTextObjectives.setText(objectives);

        //TODO active button

    }
}