package cat.itb.yapp.fragments.forms;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDate;
import java.time.LocalDateTime;

import cat.itb.yapp.R;
import cat.itb.yapp.models.treatment.CreateUpdateTreatmentDto;
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

        getParentFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                // We use a String here, but any type that can be put in a Bundle is supported
                String result = bundle.getString("bundleKey");
                // Do something with the result
            }
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

        treatment = TreatmentFormFragmentArgs.fromBundle(getArguments()).getTreatmentDto();

        if (treatment != null) {
            editing = true;
            fillUpInfoInLayout(treatment);
        } else {
            editing = false;
        }

        buttonCancel.setOnClickListener((v1 -> navController.popBackStack()));
        buttonSave.setOnClickListener(this::save);
        buttonStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonStartDate.setText(LocalDate.now().toString());
            }
        });
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
        CreateUpdateTreatmentDto createUpdateTreatmentDto;
        if (editing) createUpdateTreatmentDto = getEditedTreatment();
        else createUpdateTreatmentDto = getNewTreatment();

        TreatmentWebServiceClient webServiceClient = new RetrofitHttp().retrofit.create(TreatmentWebServiceClient.class);

        Call<TreatmentDto> call = null;
        if (editing) call = webServiceClient.updateTreatment("treatment/" + treatment.getId(), createUpdateTreatmentDto);
        //else call = webServiceClient.updateTreatment(treatmentDto); //TODO this

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

    private CreateUpdateTreatmentDto getNewTreatment() {
        CreateUpdateTreatmentDto treatmentDto = new CreateUpdateTreatmentDto();



        return treatmentDto;
    }

    private CreateUpdateTreatmentDto getEditedTreatment() {
        CreateUpdateTreatmentDto createUpdateTreatmentDto = new CreateUpdateTreatmentDto();

        createUpdateTreatmentDto.setActive(switchActive.getShowText());
        createUpdateTreatmentDto.setPatientId(Integer.parseInt(treatment.getPatientId())); //TODO cambiar
        createUpdateTreatmentDto.setReason(editTextReason.getText().toString());
        createUpdateTreatmentDto.setSessionsFinished(Integer.parseInt(editTextSessions.getText().toString()));
        createUpdateTreatmentDto.setStartDate(buttonStartDate.getText().toString());
        createUpdateTreatmentDto.setUserId(Long.parseLong(treatment.getSpecialistId())); //TODO cambiar

        return createUpdateTreatmentDto;
    }
}