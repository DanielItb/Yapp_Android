package cat.itb.yapp.fragments.forms;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import cat.itb.yapp.R;
import cat.itb.yapp.activities.MainActivity;
import cat.itb.yapp.models.mts.MtsCreateUpdateDto;
import cat.itb.yapp.models.mts.MtsDto;
import cat.itb.yapp.models.report.ReportDto;
import cat.itb.yapp.models.report.UpdateReportDto;
import cat.itb.yapp.models.treatment.CreateUpdateTreatmentDto;
import cat.itb.yapp.models.treatment.TreatmentDto;
import cat.itb.yapp.webservices.MtsServiceClient;
import cat.itb.yapp.webservices.TreatmentWebServiceClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MtsFormFragment extends Fragment {
    private NavController navController;
    private MaterialButton patientButton, specialistButton, dateButton, saveButton, cancelButton;
    private TextInputEditText reasonEditText;
    private SwitchCompat switchActive;
    private MtsDto mtsDto = null;
    private boolean editing;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(this);

        final FragmentManager fragmentManager = getParentFragmentManager();

        fragmentManager.setFragmentResultListener("userId", this, (requestKey, bundle) -> {
            mtsDto.setSpecialistId(bundle.getLong("userId"));
            String fullName = bundle.getString("fullName");
            String specialistType = bundle.getString("specialistType");

            mtsDto.setSpecialistFullName(fullName);
            mtsDto.setSpecialistType(specialistType);

            specialistButton.setText(fullName);
//            editTextSpecialist.setText(specialistType);
        });

        fragmentManager.setFragmentResultListener("patientId", this, (requestKey, bundle) -> {
            mtsDto.setPatientId(bundle.getInt("patientId"));
            String fullName = bundle.getString("fullName");
            mtsDto.setPatientFullName(fullName);
            patientButton.setText(fullName);
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mts_form, container, false);


        patientButton = v.findViewById(R.id.selectPatientMtsButton);
        specialistButton = v.findViewById(R.id.selectSpecialistMtsButton);
        dateButton = v.findViewById(R.id.startDateMtsButton);
        saveButton = v.findViewById(R.id.saveMtsButton);
        cancelButton = v.findViewById(R.id.cancelMtsButton);
        reasonEditText = v.findViewById(R.id.mtsReasonEditText);
        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mtsDto == null) {
            mtsDto = MtsFormFragmentArgs.fromBundle(getArguments()).getMtsDto();
            if (mtsDto != null) { //If editing
                editing = true;
                fillUpInfoInLayout(mtsDto);
            } else { // If new
                mtsDto = new MtsDto();
                editing = false;
            }
        } else { //If new
            fillUpInfoInLayout(mtsDto);
        }

        cancelButton.setOnClickListener(v -> navController.popBackStack());
        saveButton.setOnClickListener(v -> {
            if (allRequiredCampsSet()) save();
        });

        specialistButton.setOnClickListener(v -> navController.navigate(R.id.action_mtsFormFragment_to_selectUserFragment));
        patientButton.setOnClickListener(v -> navController.navigate(R.id.action_mtsFormFragment_to_selectPatientFragment));


        System.out.println(mtsDto.toString());

    }


    private void fillUpInfoInLayout(MtsDto mtsDto) {
        String patientName = mtsDto.getPatientFullName();
        String specialistName = mtsDto.getSpecialistFullName();
        String reason = mtsDto.getReason();
        String date = mtsDto.getDate();
        int treatmentId = mtsDto.getTratmentId();



        if (patientName != null) patientButton.setText(patientName);
        if (specialistName != null) specialistButton.setText(specialistName);
        if (reason != null) reasonEditText.setText(reason);
        if (date != null) dateButton.setText(date);

    }

    private boolean allRequiredCampsSet() {
        boolean allGood = true;
        String patientId = String.valueOf(mtsDto.getPatientId());
        String specialistId = String.valueOf(mtsDto.getSpecialistId());
        CharSequence errorMsg = getText(R.string.must_fill);

        if (patientId == null) {
            allGood = false;
            patientButton.setError(errorMsg);
        } if (specialistId == null) {
            allGood = false;
            specialistButton.setError(errorMsg);
        } if (reasonEditText.getText().toString().isEmpty()) {
            allGood= false;
            reasonEditText.setError(errorMsg);
        }

        return allGood;
    }

    private void save() {
        final MtsCreateUpdateDto mtsCreateUpdateDto = getInfoFromLayout();

        MtsServiceClient webServiceClient = MainActivity.getRetrofitHttp()
                .retrofit.create(MtsServiceClient.class);

        Call<MtsDto> call = null;
        if (editing) call = webServiceClient.updateMts("medicalsheet/" + mtsDto.getId(), mtsCreateUpdateDto);
        else call = webServiceClient.addMts(mtsCreateUpdateDto);

        call.enqueue(new Callback<MtsDto>() {
            @Override
            public void onResponse(Call<MtsDto> call, Response<MtsDto> response) {
                Log.d("mtsFrom", response.toString());
                if (response.isSuccessful()){
                    navController.popBackStack();
                }else{
                    Toast.makeText(getContext(), R.string.error_saving, Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<MtsDto> call, Throwable t) {
                Toast.makeText(getContext(), R.string.error_saving, Toast.LENGTH_LONG).show();
            }
        });
    }


    private MtsCreateUpdateDto getInfoFromLayout() {
        MtsCreateUpdateDto mtsCreateUpdateDto = new MtsCreateUpdateDto();

        mtsCreateUpdateDto.setPatientId(mtsDto.getPatientId());
        mtsCreateUpdateDto.setSpecialistId(mtsDto.getSpecialistId());
        mtsCreateUpdateDto.setDate(mtsDto.getDate());
        mtsCreateUpdateDto.setTreatmentId(mtsDto.getTratmentId());


        return mtsCreateUpdateDto;
    }



}