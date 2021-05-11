package cat.itb.yapp.fragments.forms;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import cat.itb.yapp.R;
import cat.itb.yapp.models.treatment.TreatmentDto;
import cat.itb.yapp.retrofit.RetrofitHttp;
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
    private TreatmentDto treatment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
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

        treatment = TreatmentFormFragmentArgs.fromBundle(getArguments()).getTreatmentDto();

        if (treatment != null) {
            editing = true;
            fillUpInfoInLayout(treatment);
        } else {
            editing = false;
        }

        buttonCancel.setOnClickListener((v1 -> navController.popBackStack()));
        buttonSave.setOnClickListener(this::save);
    }

    private void fillUpInfoInLayout(TreatmentDto treatment) {
        buttonPatient.setText(treatment.getPatientFullName());
        buttonSpecialist.setText(treatment.getSpecialistFullName());
        buttonStartDate.setText(treatment.getStartDate());

        editTextSessions.setText(treatment.getSessionsFinished());
        editTextReason.setText(treatment.getReason());
        //TODO active button
        //switchActive.setChecked(treatment.get);
    }

    private void save(View v) {
        TreatmentDto treatmentDto;
        if (editing) treatmentDto = getEditedTreatment();
        else treatmentDto = getNewTreatment();

        TreatmentWebServiceClient webServiceClient = new RetrofitHttp().retrofit.create(TreatmentWebServiceClient.class);

        Call<TreatmentDto> call;
        if (editing) call = webServiceClient.updateTreatment(treatment);
        else call = webServiceClient.updateTreatment(treatment); //TODO this

        call.enqueue(new Callback<TreatmentDto>() {
            @Override
            public void onResponse(Call<TreatmentDto> call, Response<TreatmentDto> response) {
                navController.popBackStack();
            }

            @Override
            public void onFailure(Call<TreatmentDto> call, Throwable t) {
                Toast.makeText(getContext(), R.string.error_saving, Toast.LENGTH_LONG).show();
            }
        });
    }

    private TreatmentDto getNewTreatment() {
        TreatmentDto treatmentDto = new TreatmentDto();

        return treatmentDto;
    }

    private TreatmentDto getEditedTreatment() {
        TreatmentDto treatmentDto = treatment;

        treatmentDto.setSessionsFinished(editTextSessions.getText().toString());
        treatmentDto.setReason(editTextReason.getText().toString());

        return treatmentDto;
    }
}