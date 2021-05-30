package cat.itb.yapp.fragments.forms;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import cat.itb.yapp.R;
import cat.itb.yapp.activities.MainActivity;
import cat.itb.yapp.models.report.CreateUpdateReportDto;
import cat.itb.yapp.models.report.ReportDto;
import cat.itb.yapp.utils.ErrorUtils;
import cat.itb.yapp.utils.UtilsDatePicker;
import cat.itb.yapp.webservices.ReportServiceClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragmento controlador del formulario de los datos de los informes (Report).
 * @author David Lama, Kenneth Griñan, Daniel Acosta
 *
 */
public class ReportFormFragment extends Fragment {
    private NavController navController;
    private TextInputEditText editTextDiagnosis, editTextObjectives, editTextSpecialistType, editTextDate,
            editTextPatient, editTextSpecialist, editTextTreatment;
    private MaterialButton buttonDelete, buttonSave;
    private ReportDto reportDto = null;
    private boolean editing;
    private SwitchCompat editSwitch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(this);

        final FragmentManager fragmentManager = getParentFragmentManager();

        fragmentManager.setFragmentResultListener("treatment", this, ((requestKey, bundle) -> {
            reportDto.setTreatmentId(Integer.valueOf(bundle.getString("treatmentId")));
            reportDto.setTreatmentReason(bundle.getString("reason"));
            reportDto.setPatientFullName(bundle.getString("patientName"));
            reportDto.setPatientId(bundle.getInt("patientId"));
            reportDto.setSpecialistFullName(bundle.getString("specialistName"));
            reportDto.setSpecialistId(bundle.getLong("specialistId"));
            reportDto.setSpecialistType(bundle.getString("type"));

            editTextTreatment.setText(reportDto.getTreatmentReason());
            editTextPatient.setText(reportDto.getPatientFullName());
            editTextSpecialist.setText(reportDto.getSpecialistFullName());
            editTextSpecialistType.setText(reportDto.getSpecialistType());
        }));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_report_form, container, false);

        editTextDiagnosis = v.findViewById(R.id.diagnosisReportEditText);
        editTextObjectives = v.findViewById(R.id.objetivesReportEditText);
        buttonDelete = v.findViewById(R.id.cancelReportButton);
        buttonSave = v.findViewById(R.id.saveReportButton);
        editTextSpecialistType = v.findViewById(R.id.specialistTypeReportEditText);
        editTextDate = v.findViewById(R.id.dateReportEditText);
        editTextPatient = v.findViewById(R.id.patientReportEditText);
        editTextTreatment = v.findViewById(R.id.treatmentReportEditText);
        editTextSpecialist = v.findViewById(R.id.specialistReportEditText);
        editSwitch  =v.findViewById(R.id.editSwitchReport);

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
                notFocusable();
                fillUpInfoInLayout(reportDto);
            } else { // If new
                reportDto = new ReportDto();
                reportDto.setDate(LocalDate.now().toString());
                focusable();
                editing = false;

            }
        } else { //If load data
            fillUpInfoInLayout(reportDto);
        }

        if (editing) {
            buttonDelete.setOnClickListener(v -> deleteReportDialog());
        } else {
            buttonDelete.setText(R.string.cancel);
            buttonDelete.setOnClickListener(v -> navController.popBackStack());
            editSwitch.setVisibility(View.GONE);
        }


        buttonSave.setOnClickListener(v -> {
            if (allRequiredCampsSet()) save();
        });

        editTextSpecialist.setOnClickListener(v -> navController.navigate(R.id.action_reportFormFragment_to_selectUserFragment));
        editTextPatient.setOnClickListener(v -> navController.navigate(R.id.action_reportFormFragment_to_selectPatientFragment));
        editTextTreatment.setOnClickListener(v -> navController.navigate(R.id.action_reportFormFragment_to_selectTreatmentFragment));
        editTextDate.setOnClickListener(v -> UtilsDatePicker.showDatePicker(this::dateSelected, getParentFragmentManager()));


        editSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    focusable();
                }else{
                    notFocusable();
                }
            }
        });

    }


    private void notFocusable(){
        editTextDiagnosis.setFocusable(false);
        editTextObjectives.setFocusable(false);
        editTextDate.setEnabled(false);
        editTextTreatment.setEnabled(false);
        buttonDelete.setVisibility(View.GONE);
        buttonSave.setVisibility(View.GONE);
    }

    private void focusable(){
        editTextDiagnosis.setFocusableInTouchMode(true);
        editTextObjectives.setFocusableInTouchMode(true);
        editTextSpecialistType.setEnabled(true);
        editTextDate.setEnabled(true);
        editTextTreatment.setEnabled(true);
        buttonDelete.setVisibility(View.VISIBLE);
        buttonSave.setVisibility(View.VISIBLE);
    }

    private void dateSelected(Object o) {
        LocalDate date =
                Instant.ofEpochMilli((Long) o).atZone(ZoneId.systemDefault()).toLocalDate();

        editTextDate.setText(date.toString());
        reportDto.setDate(date.toString());
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
                    Toast.makeText(getContext(), ErrorUtils.getErrorString(response.errorBody()), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ReportDto> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }



    private void delete(){
        ReportServiceClient reportService = MainActivity.getRetrofitHttp()
                .retrofit.create(ReportServiceClient.class);

        Call<ReportDto> call;

        call = reportService.deleteReportDto(reportDto.getId());

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



    private void deleteReportDialog(){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
        builder.setTitle(R.string.caution);
        builder.setMessage(R.string.sure_report);
        builder.setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton(R.string.deleteButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delete();
            }
        });
        builder.show();
    }


    private CreateUpdateReportDto getCreateUpdateReportFromFrontEnd() {
        CreateUpdateReportDto createUpdateReportDto = new CreateUpdateReportDto();

        createUpdateReportDto.setDiagnosis(editTextDiagnosis.getText().toString());
        createUpdateReportDto.setObjectives(editTextObjectives.getText().toString());
        createUpdateReportDto.setTreatmentId(reportDto.getTreatmentId());
        createUpdateReportDto.setDate(reportDto.getDate());

        return createUpdateReportDto;
    }

    private boolean allRequiredCampsSet() {
        boolean allGood = true;
        Integer patientId = reportDto.getPatientId();
        Long specialistId = reportDto.getSpecialistId();
        CharSequence errorMsg = getText(R.string.must_fill);

        if (patientId == null) {
            allGood = false;
            editTextPatient.setError(errorMsg);
        } if (specialistId == null) {
            allGood = false;
            editTextSpecialist.setError(errorMsg);
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
        String reason = reportDto.getTreatmentReason();

        if (patientName != null) editTextPatient.setText(patientName);
        if (specialistName != null) editTextSpecialist.setText(specialistName);
        if (specialistType != null) editTextSpecialistType.setText(specialistType);
        if (diagnosis != null) editTextDiagnosis.setText(diagnosis);
        if (objectives != null) editTextObjectives.setText(objectives);
        if (reason != null) editTextTreatment.setText(reason);
        if (date != null) editTextDate.setText(date);
    }
}