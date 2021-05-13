package cat.itb.yapp.fragments.forms;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import cat.itb.yapp.R;
import cat.itb.yapp.activities.MainActivity;
import cat.itb.yapp.models.treatment.CreateUpdateTreatmentDto;
import cat.itb.yapp.models.treatment.TreatmentDto;
import cat.itb.yapp.utils.UtilsDatePicker;
import cat.itb.yapp.webservices.TreatmentWebServiceClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TreatmentFormFragment extends Fragment {

    private NavController navController;
    private TextInputEditText editTextSessions, editTextReason;
    private MaterialButton buttonPatient, buttonSpecialist, buttonStartDate, buttonCancel,
            buttonSave;
    private SwitchCompat switchActive;
    private boolean editing;
    private TreatmentDto treatment = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(this);

        final FragmentManager fragmentManager = getParentFragmentManager();
        fragmentManager.setFragmentResultListener("userId", this, (requestKey, bundle) -> {
            treatment.setSpecialistId(String.valueOf(bundle.getLong("userId"))); //TODO Remove parse
            String fullName = bundle.getString("fullName");
            treatment.setSpecialistFullName(fullName);
            buttonSpecialist.setText(fullName);
        });
        fragmentManager.setFragmentResultListener("patientId", this, (requestKey, bundle) -> {
            treatment.setPatientId(String.valueOf(bundle.getInt("patientId"))); //TODO Remove parse
            String fullName = bundle.getString("fullName");
            treatment.setPatientFullName(fullName);
            buttonPatient.setText(fullName);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_treatment_form, container, false);

        editTextSessions = v.findViewById(R.id.sessionsEditText);
        editTextReason = v.findViewById(R.id.treatmentReasonEditText);
        buttonPatient = v.findViewById(R.id.selectPatientButton);
        buttonSpecialist = v.findViewById(R.id.selectSpecialistButton);
        buttonStartDate = v.findViewById(R.id.startDateButton);
        buttonCancel = v.findViewById(R.id.cancelButton);
        buttonSave = v.findViewById(R.id.saveButton);
        switchActive = v.findViewById(R.id.simpleSwitch);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (treatment == null) {
            treatment = TreatmentFormFragmentArgs.fromBundle(getArguments()).getTreatmentDto();
            if (treatment != null) {
                editing = true;
                fillUpInfoInLayout(treatment);
            } else {
                treatment = new TreatmentDto();
                editing = false;
            }
        } else {
            fillUpInfoInLayout(treatment);
        }


        buttonCancel.setOnClickListener(v1 -> navController.popBackStack());
        buttonSave.setOnClickListener(v -> {
            if(allRequiredCampsSet()) save();
        });
        buttonSpecialist.setOnClickListener(v -> navController.navigate(R.id.action_treatmentFormFragment_to_selectUserFragment));
        buttonPatient.setOnClickListener(v -> navController.navigate(R.id.action_treatmentFormFragment_to_selectPatientFragment));
        buttonStartDate.setOnClickListener(v -> UtilsDatePicker.showDatePicker(this::dateSelected,
                getParentFragmentManager()));
    }

    private void dateSelected(Object o) {
        LocalDate date =
                Instant.ofEpochMilli((Long) o).atZone(ZoneId.systemDefault()).toLocalDate();

        buttonStartDate.setText(date.toString());
        treatment.setStartDate(date.toString());
    }


    private boolean allRequiredCampsSet() {
        boolean allGood = true;
        String patientId = treatment.getPatientId();
        String specialistId = treatment.getSpecialistId();
        CharSequence errorMsg = getText(R.string.must_fill);

        if (patientId == null) {
            allGood = false;
            buttonPatient.setError(errorMsg);
        } if (specialistId == null) {
            allGood = false;
            buttonSpecialist.setError(errorMsg);
        } if (editTextSessions.getText().toString().isEmpty()) {
            allGood= false;
            editTextSessions.setError(errorMsg);
        }

        return allGood;
    }

    private void fillUpInfoInLayout(TreatmentDto treatment) {
        String startDate = treatment.getStartDate();
        String patientName= treatment.getPatientFullName();
        String specialistName = treatment.getSpecialistFullName();

        if (startDate != null) buttonStartDate.setText(startDate);
        if (patientName != null) buttonPatient.setText(treatment.getPatientFullName());
        if (specialistName != null) buttonSpecialist.setText(treatment.getSpecialistFullName());

        editTextSessions.setText(treatment.getSessionsFinished());
        editTextReason.setText(treatment.getReason());
        //TODO active button
        //switchActive.setChecked(treatment.get);
    }

    private void save() {
        final CreateUpdateTreatmentDto createUpdateTreatmentDto = getTreatment();

        TreatmentWebServiceClient webServiceClient = MainActivity.getRetrofitHttp()
                .retrofit.create(TreatmentWebServiceClient.class);

        Call<TreatmentDto> call = null;
        if (editing) call = webServiceClient.updateTreatment("treatment/" + treatment.getId(), createUpdateTreatmentDto);
        else call = webServiceClient.addTreatment(createUpdateTreatmentDto);

        call.enqueue(new Callback<TreatmentDto>() {
            @Override
            public void onResponse(Call<TreatmentDto> call, Response<TreatmentDto> response) {
                Log.d("treatmentFrom", response.toString());
                navController.popBackStack();
            }

            @Override
            public void onFailure(Call<TreatmentDto> call, Throwable t) {
                Toast.makeText(getContext(), R.string.error_saving, Toast.LENGTH_LONG).show();
            }
        });
    }

    private CreateUpdateTreatmentDto getTreatment() {
        CreateUpdateTreatmentDto createUpdateTreatmentDto = new CreateUpdateTreatmentDto();

        createUpdateTreatmentDto.setActive(switchActive.getShowText());
        createUpdateTreatmentDto.setPatientId(Integer.parseInt(treatment.getPatientId())); //TODO remove parse
        createUpdateTreatmentDto.setReason(editTextReason.getText().toString());
        createUpdateTreatmentDto.setSessionsFinished(Integer.parseInt(editTextSessions.getText().toString()));
        createUpdateTreatmentDto.setStartDate(buttonStartDate.getText().toString());
        createUpdateTreatmentDto.setUserId(Long.parseLong(treatment.getSpecialistId())); //TODO remove parse

        return createUpdateTreatmentDto;
    }
}