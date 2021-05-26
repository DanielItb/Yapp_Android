package cat.itb.yapp.fragments.forms;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
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
import cat.itb.yapp.fragments.list.TreatmentListFragment;
import cat.itb.yapp.models.treatment.CreateUpdateTreatmentDto;
import cat.itb.yapp.models.treatment.TreatmentDto;
import cat.itb.yapp.utils.UtilsDatePicker;
import cat.itb.yapp.webservices.TreatmentWebServiceClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TreatmentFormFragment extends Fragment {

    private NavController navController;
    private TextInputEditText editTextSessions, editTextReason, editTextPatient, editTextSpecialist,
            editTextStartDate;
    private MaterialButton buttonDelete, buttonSave;
    private boolean editing;
    private TreatmentDto treatment = null;
    private SwitchCompat editSwitch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(this);

        final FragmentManager fragmentManager = getParentFragmentManager();
        fragmentManager.setFragmentResultListener("userId", this, (requestKey, bundle) -> {
            treatment.setSpecialistId(String.valueOf(bundle.getLong("userId"))); //TODO Remove parse
            String fullName = bundle.getString("fullName");
            treatment.setSpecialistFullName(fullName);
            editTextSpecialist.setText(fullName);
        });
        fragmentManager.setFragmentResultListener("patientId", this, (requestKey, bundle) -> {
            treatment.setPatientId(String.valueOf(bundle.getInt("patientId"))); //TODO Remove parse
            String fullName = bundle.getString("fullName");
            treatment.setPatientFullName(fullName);
            editTextPatient.setText(fullName);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_treatment_form, container, false);

        editTextSessions = v.findViewById(R.id.sessionsEditText);
        editTextReason = v.findViewById(R.id.treatmentReasonEditText);
        editTextSpecialist = v.findViewById(R.id.specialistTreatmentEditText);
        editTextPatient = v.findViewById(R.id.patientTreatmentEditText);
        editTextStartDate = v.findViewById(R.id.startDateTreatmentEditText);
        buttonDelete = v.findViewById(R.id.cancelButton);
        buttonSave = v.findViewById(R.id.saveButton);
        editSwitch = v.findViewById(R.id.editSwitchTreatments);




        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (treatment == null) {
            treatment = TreatmentFormFragmentArgs.fromBundle(getArguments()).getTreatmentDto();
            if (treatment != null) {
                editing = true;
                notFocusable();
                fillUpInfoInLayout(treatment);
            } else {
                treatment = new TreatmentDto();
                editing = false;
                editSwitch.setVisibility(View.GONE);
                focusable();
            }
        } else {
            fillUpInfoInLayout(treatment);
        }


        buttonDelete.setOnClickListener(v1 -> deleteTreatmentDialog());
        buttonSave.setOnClickListener(v -> {
            if(allRequiredCampsSet()) save();
        });
        editTextSpecialist.setOnClickListener(v -> navController.navigate(R.id.action_treatmentFormFragment_to_selectUserFragment));
        editTextPatient.setOnClickListener(v -> navController.navigate(R.id.action_treatmentFormFragment_to_selectPatientFragment));
        editTextStartDate.setOnClickListener(v -> UtilsDatePicker.showDatePicker(this::dateSelected,
                getParentFragmentManager()));

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

    public void notFocusable(){
        editTextSessions.setFocusable(false);
        editTextReason.setFocusable(false);
        editTextSpecialist.setEnabled(false);
        editTextPatient.setEnabled(false);
        editTextStartDate.setEnabled(false);
        buttonDelete.setVisibility(View.GONE);
        buttonSave.setVisibility(View.GONE);
    }

    public void focusable(){
        editTextSessions.setFocusable(true);
        editTextReason.setFocusable(true);
        editTextSpecialist.setEnabled(true);
        editTextPatient.setEnabled(true);
        editTextStartDate.setEnabled(true);
        buttonDelete.setVisibility(View.VISIBLE);
        buttonSave.setVisibility(View.VISIBLE);
    }

    private void dateSelected(Object o) {
        LocalDate date =
                Instant.ofEpochMilli((Long) o).atZone(ZoneId.systemDefault()).toLocalDate();

        editTextStartDate.setText(date.toString());
        treatment.setStartDate(date.toString());
    }


    private boolean allRequiredCampsSet() {
        boolean allGood = true;
        String patientId = treatment.getPatientId();
        String specialistId = treatment.getSpecialistId();
        CharSequence errorMsg = getText(R.string.must_fill);

        if (patientId == null) {
            allGood = false;
            editTextPatient.setError(errorMsg);
        } if (specialistId == null) {
            allGood = false;
            editTextSpecialist.setError(errorMsg);
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

        if (startDate != null) editTextStartDate.setText(startDate);
        if (patientName != null) editTextPatient.setText(treatment.getPatientFullName());
        if (specialistName != null) editTextSpecialist.setText(treatment.getSpecialistFullName());

        editTextSessions.setText(treatment.getSessionsFinished());
        editTextReason.setText(treatment.getReason());
    }

    private void save() {
        final CreateUpdateTreatmentDto createUpdateTreatmentDto = getTreatment();

        TreatmentWebServiceClient webServiceClient = MainActivity.getRetrofitHttp()
                .retrofit.create(TreatmentWebServiceClient.class);

        Call<TreatmentDto> call;
        if (editing) call = webServiceClient.updateTreatment("treatment/" + treatment.getId(), createUpdateTreatmentDto);
        else call = webServiceClient.addTreatment(createUpdateTreatmentDto);

        call.enqueue(new Callback<TreatmentDto>() {
            @Override
            public void onResponse(Call<TreatmentDto> call, Response<TreatmentDto> response) {
                Log.d("treatmentFrom", response.toString());
                if (response.isSuccessful()) {
                    navController.popBackStack();
                } else {
                    Toast.makeText(getContext(), R.string.error_saving, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<TreatmentDto> call, Throwable t) {
                Toast.makeText(getContext(), R.string.error_saving, Toast.LENGTH_LONG).show();
            }
        });
    }


    private void delete(){
        TreatmentWebServiceClient treatmentService = MainActivity.getRetrofitHttp()
                .retrofit.create(TreatmentWebServiceClient.class);

        Call<TreatmentDto> call;

        call = treatmentService.deleteTreatmentDto(Integer.parseInt(treatment.getId()));

        call.enqueue(new Callback<TreatmentDto>() {
            @Override
            public void onResponse(Call<TreatmentDto> call, Response<TreatmentDto> response) {
                if (response.isSuccessful()) {
                    TreatmentListFragment.treatmentList.remove(treatment);
                    navController.popBackStack();
                } else {
                    Toast.makeText(getContext(), R.string.error_saving, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<TreatmentDto> call, Throwable t) {
                Toast.makeText(getContext(), R.string.error_saving, Toast.LENGTH_LONG).show();
            }
        });

    }


    public void deleteTreatmentDialog(){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
        builder.setTitle(R.string.caution);
        builder.setMessage(R.string.sure_treatment);
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




    private CreateUpdateTreatmentDto getTreatment() {
        CreateUpdateTreatmentDto createUpdateTreatmentDto = new CreateUpdateTreatmentDto();

//        createUpdateTreatmentDto.setActive(switchActive.getShowText());
        createUpdateTreatmentDto.setPatientId(Integer.parseInt(treatment.getPatientId())); //TODO remove parse
        createUpdateTreatmentDto.setReason(editTextReason.getText().toString());
        createUpdateTreatmentDto.setSessionsFinished(Integer.parseInt(editTextSessions.getText().toString()));
        createUpdateTreatmentDto.setStartDate(editTextStartDate.getText().toString());
        createUpdateTreatmentDto.setUserId(Long.parseLong(treatment.getSpecialistId())); //TODO remove parse

        return createUpdateTreatmentDto;
    }
}