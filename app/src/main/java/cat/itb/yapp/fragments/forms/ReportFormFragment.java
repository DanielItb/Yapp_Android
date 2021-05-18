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
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDate;
import java.time.LocalDateTime;

import cat.itb.yapp.R;
import cat.itb.yapp.activities.MainActivity;
import cat.itb.yapp.models.report.CreateUpdateReportDto;
import cat.itb.yapp.models.report.ReportDto;
import cat.itb.yapp.webservices.ReportServiceClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportFormFragment extends Fragment {
    private NavController navController;
    private TextInputEditText editTextDiagnosis, editTextObjectives, editTextSpecialist, editTextDate;
    private TextInputLayout textInputDate;
    private MaterialButton buttonPatient, buttonSpecialist, buttonTreatment, buttonCancel, buttonSave;
    private ReportDto reportDto = null;
    private String reason = null;
    private boolean editing;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(this);

        final FragmentManager fragmentManager = getParentFragmentManager();

        fragmentManager.setFragmentResultListener("userId", this, (requestKey, bundle) -> {
            reportDto.setSpecialistId(bundle.getLong("userId")); //TODO Remove parse
            String fullName = bundle.getString("fullName");
            String specialistType = bundle.getString("specialistType");

            reportDto.setSpecialistFullName(fullName);
            reportDto.setSpecialistType(specialistType);

            buttonSpecialist.setText(fullName);
            editTextSpecialist.setText(specialistType);
        });

        fragmentManager.setFragmentResultListener("patientId", this, (requestKey, bundle) -> {
            reportDto.setPatientId(bundle.getInt("patientId")); //TODO Remove parse
            String fullName = bundle.getString("fullName");
            reportDto.setPatientFullName(fullName);
            buttonPatient.setText(fullName);
        });

        fragmentManager.setFragmentResultListener("treatment", this, ((requestKey, bundle) -> {
            reason = bundle.getString("reason");
            reportDto.setTreatmentId(Integer.valueOf(bundle.getString("treatmentId")));

            buttonTreatment.setText(reason);
        }));
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
        editTextSpecialist = v.findViewById(R.id.specialistTypeReportEditText);
        buttonTreatment = v.findViewById(R.id.selectTreatmentButton);
        editTextDate = v.findViewById(R.id.dateReportEditText);
        textInputDate = v.findViewById(R.id.dateReportInputLayout);


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
                reportDto.setDate(LocalDate.now().toString());
                editing = false;
                fillUpInfoInLayout(reportDto);
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
        buttonTreatment.setOnClickListener(v -> navController.navigate(R.id.action_reportFormFragment_to_selectTreatmentFragment));
        editTextDate.setOnClickListener(v -> editTextDate.setText("test")); //Todo
    }

    private void save() {
        final CreateUpdateReportDto createUpdateReportDto = getCreateUpdateReportFromFrontEnd();

        ReportServiceClient reportServiceClient = MainActivity.getRetrofitHttp()
                .retrofit.create(ReportServiceClient.class);

        Call<ReportDto> call;
        if (editing) call = reportServiceClient.updateDto("report/" + reportDto.getId(),
                createUpdateReportDto);
        else call = reportServiceClient.addReport(createUpdateReportDto);

        call.enqueue(new Callback<ReportDto>() {
            @Override
            public void onResponse(Call<ReportDto> call, Response<ReportDto> response) {
                if (response.isSuccessful()) {
                    navController.popBackStack();
                } else {
                    Toast.makeText(getContext(), R.string.error_saving, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ReportDto> call, Throwable t) {
                Toast.makeText(getContext(), R.string.error_saving, Toast.LENGTH_LONG).show();
            }
        });

    }

    private CreateUpdateReportDto getCreateUpdateReportFromFrontEnd() {
        CreateUpdateReportDto createUpdateReportDto = new CreateUpdateReportDto();

        createUpdateReportDto.setDiagnosis(editTextDiagnosis.getText().toString());
        createUpdateReportDto.setObjectives(editTextObjectives.getText().toString());
        createUpdateReportDto.setTreatmentId(reportDto.getTreatmentId());
        createUpdateReportDto.setDate(reportDto.getDate()); //Todo get date on new dto

        return createUpdateReportDto;
    }

    private boolean allRequiredCampsSet() {
        boolean allGood = true;
        Integer patientId = reportDto.getPatientId();
        Long specialistId = reportDto.getSpecialistId();
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
        String date = reportDto.getDate();

        if (patientName != null) buttonPatient.setText(patientName);
        if (specialistName != null) buttonSpecialist.setText(specialistName);
        if (specialistType != null) editTextSpecialist.setText(reportDto.getSpecialistType());
        if (diagnosis != null) editTextDiagnosis.setText(diagnosis);
        if (objectives != null) editTextObjectives.setText(objectives);
        if (reason != null) buttonTreatment.setText(reason);
        if (date != null) editTextDate.setText(date);
    }
}