package cat.itb.yapp.fragments.forms;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDateTime;

import cat.itb.yapp.R;
import cat.itb.yapp.models.patient.PatientDto;
import cat.itb.yapp.models.report.ReportDto;
import cat.itb.yapp.models.treatment.TreatmentDto;


public class PatientFormFragment extends Fragment {
    private MaterialButton birthDateButton, saveButton;
    private TextInputEditText nameEditText, surnameEditText, ageEditText, addressEditText, phoneNumberEditText, emailEditText, schoolEditText, courseEditText;
    private AutoCompleteTextView paymentTypeAutoCompleteTextView;

    private boolean editing;
    private PatientDto patientDto = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_patient_profile, container, false);
        final String[] paymentTypes = getResources().getStringArray(R.array.payment_types);

//        registerDate = v.findViewById(R.id.registerDateEditText);

        birthDateButton = v.findViewById(R.id.birthDateButton);
        saveButton = v.findViewById(R.id.saveButtonPatientForm);
        nameEditText = v.findViewById(R.id.nameEditText);
        surnameEditText = v.findViewById(R.id.surnameEditText);
        ageEditText = v.findViewById(R.id.ageEditText);
        addressEditText = v.findViewById(R.id.addressEditText);
        phoneNumberEditText = v.findViewById(R.id.phoneEditText);
        emailEditText = v.findViewById(R.id.emailEditText);
        schoolEditText = v.findViewById(R.id.schoolEditText);
        courseEditText = v.findViewById(R.id.courseEditText);
        birthDateButton.setOnClickListener(this::datePickerCall);


        paymentTypeAutoCompleteTextView = v.findViewById(R.id.autoComplete);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(), R.layout.drop_down_types, paymentTypes);
        paymentTypeAutoCompleteTextView.setAdapter(adapter);


        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //If starts for first time
        if (patientDto == null) {
            patientDto = PatientFormFragmentArgs.fromBundle(getArguments()).getPatientDto();
            if (patientDto != null) { //If editing
                editing = true;
                fillUpInfoInLayout(patientDto);
            } else { // If new
                patientDto = new ReportDto();
                patientDto.setDate(LocalDateTime.now().toString());
                editing = false;
            }
        } else { //If load data
            fillUpInfoInLayout(patientDto);
        }

        buttonCancel.setOnClickListener(v -> navController.popBackStack());
        buttonSave.setOnClickListener(v -> {
            if (allRequiredCampsSet()) save();
        });

        buttonSpecialist.setOnClickListener(v -> navController.navigate(R.id.action_reportFormFragment_to_selectUserFragment));
        buttonPatient.setOnClickListener(v -> navController.navigate(R.id.action_reportFormFragment_to_selectPatientFragment));
        buttonTreatment.setOnClickListener(v -> navController.navigate(R.id.action_reportFormFragment_to_selectTreatmentFragment));
    }


    private void fillUpInfoInLayout(PatientDto patientDto) {
//        private TextInputEditText nameEditText, surnameEditText, ageEditText, addressEditText, phoneNumberEditText, emailEditText, schoolEditText, courseEditText;
        String patientName = patientDto.getName();
        String patientSurname = patientDto.getSurname();
        String patientAge = String.valueOf(patientDto.getAge());
        String patientAddress = patientDto.getHomeAddress();
        String patientPhone = patientDto.getPhoneNumber();
        String patientEmail = patientDto.getEmail();
        String patientSchool = patientDto.getSchoolName();
        String patientCourse = patientDto.getCourse();
        String patientPaymentType = patientDto.getPaymentType();
        String patientBirthDate = patientDto.getDateOfBirth();

        if (patientName != null) buttonPatient.setText(patientName);
        if (specialistName != null) buttonSpecialist.setText(specialistName);
        if (specialistType != null) editTextSpecialist.setText(patientDto.getSpecialistType());
        if (diagnosis != null) editTextDiagnosis.setText(diagnosis);
        if (objectives != null) editTextObjectives.setText(objectives);
        if (reason != null) buttonTreatment.setText(reason);
    }
}

    public void datePickerCall(View v) {
        datePicker();
    }

    public void datePicker() {
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select date: ");
        final MaterialDatePicker<Long> picker = builder.build();
        picker.show(getChildFragmentManager(), picker.toString());
        if (picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                birthDateButton.setText(String.valueOf(picker.getHeaderText()));
            }
        })) ;
    }
}