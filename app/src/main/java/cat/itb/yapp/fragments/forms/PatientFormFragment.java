package cat.itb.yapp.fragments.forms;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import cat.itb.yapp.R;
import cat.itb.yapp.activities.MainActivity;
import cat.itb.yapp.fragments.list.PatientListFragment;
import cat.itb.yapp.models.patient.CreateUpdatePatientDto;
import cat.itb.yapp.models.patient.PatientDto;
import cat.itb.yapp.webservices.PatientWebServiceClient;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PatientFormFragment extends Fragment {
    // TODO cargar la foto y borrar paciente
    private NavController navController;
    private MaterialButton birthDateButton, saveButton, deleteButton;
    private TextInputEditText nameEditText, surnameEditText, ageEditText, addressEditText, phoneNumberEditText, emailEditText, schoolEditText, courseEditText, reasonEditTExt;
    private AutoCompleteTextView paymentTypeAutoCompleteTextView;
    private boolean editing;
    private PatientDto patientDto = null;
    private CircleImageView circleImageView;
    private SwitchCompat editSwitch;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
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
        reasonEditTExt = v.findViewById(R.id.patientReasonEditText);
        deleteButton = v.findViewById(R.id.deletePatientButton);
        circleImageView = v.findViewById(R.id.profile_image);
        editSwitch = v.findViewById(R.id.editSwitchPatient);


        birthDateButton.setOnClickListener(this::datePicker);




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
                deleteButton.setText(R.string.deleteButton);
                editing = true;
                notFocusable();
                fillUpInfoInLayout(patientDto);
            } else { // If new
                patientDto = new PatientDto();
                editSwitch.setVisibility(View.GONE);
                focusable();
//                patientDto.setDate(LocalDateTime.now().toString());
                editing = false;
            }
        } else { //If load data
            fillUpInfoInLayout(patientDto);
        }

        deleteButton.setOnClickListener(v1 -> deletePatientDialog());
        saveButton.setOnClickListener(v -> {
            if (allRequiredCampsSet()) save();
        });

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
        birthDateButton.setEnabled(false);
        nameEditText.setFocusable(false);
        surnameEditText.setFocusable(false);
        addressEditText.setFocusable(false);
        phoneNumberEditText.setFocusable(false);
        emailEditText.setFocusable(false);
        schoolEditText.setFocusable(false);
        courseEditText.setFocusable(false);
        reasonEditTExt.setFocusable(false);
        deleteButton.setFocusable(false);
        circleImageView.setFocusable(false);
        paymentTypeAutoCompleteTextView.setFocusableInTouchMode(false);
        paymentTypeAutoCompleteTextView.setFocusable(false);
        paymentTypeAutoCompleteTextView.setClickable(false);
        paymentTypeAutoCompleteTextView.setFocusable(false);
        paymentTypeAutoCompleteTextView.setDropDownHeight(0);
        saveButton.setVisibility(View.GONE);
        deleteButton.setVisibility(View.GONE);

    }

    public void focusable(){
        birthDateButton.setEnabled(true);
        nameEditText.setFocusableInTouchMode(true);
        nameEditText.setFocusableInTouchMode(true);
        surnameEditText.setFocusableInTouchMode(true);
        addressEditText.setFocusableInTouchMode(true);
        phoneNumberEditText.setFocusableInTouchMode(true);
        emailEditText.setFocusableInTouchMode(true);
        schoolEditText.setFocusableInTouchMode(true);
        courseEditText.setFocusableInTouchMode(true);
        reasonEditTExt.setFocusableInTouchMode(true);
        deleteButton.setFocusableInTouchMode(true);
        circleImageView.setFocusableInTouchMode(true);
        paymentTypeAutoCompleteTextView.setDropDownHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        saveButton.setVisibility(View.VISIBLE);
        deleteButton.setVisibility(View.VISIBLE);
    }

    private boolean allRequiredCampsSet() {
        boolean allGood = true;
        CharSequence errorMsg = getText(R.string.must_fill);

        if (nameEditText.getText().toString().isEmpty()) {
            allGood = false;
            nameEditText.setError(errorMsg);
        } if (surnameEditText.getText().toString().isEmpty()) {
            allGood = false;
            surnameEditText.setError(errorMsg);
        } if(addressEditText.getText().toString().isEmpty()) {
            allGood= false;
            addressEditText.setError(errorMsg);
        } if(phoneNumberEditText.getText().toString().isEmpty()) {
            allGood= false;
            phoneNumberEditText.setError(errorMsg);
        } if(schoolEditText.getText().toString().isEmpty()) {
            allGood= false;
            schoolEditText.setError(errorMsg);
        } if(courseEditText.getText().toString().isEmpty()) {
            allGood= false;
            courseEditText.setError(errorMsg);
        } if(reasonEditTExt.getText().toString().isEmpty()) {
            allGood= false;
            reasonEditTExt.setError(errorMsg);
        }

        return allGood;
    }


    private void save() {
        final CreateUpdatePatientDto createUpdatePatientDto = getCreateUpdatePatientFromFrontEnd();

        PatientWebServiceClient patientWebServiceClient = MainActivity.getRetrofitHttp()
                .retrofit.create(PatientWebServiceClient.class);

        Call<PatientDto> call;
        if (editing) call = patientWebServiceClient.updateDto("patient/" + patientDto.getId(),
                createUpdatePatientDto);
        else call = patientWebServiceClient.addPatient(createUpdatePatientDto);

        call.enqueue(new Callback<PatientDto>() {
            @Override
            public void onResponse(Call<PatientDto> call, Response<PatientDto> response) {
                if (response.isSuccessful()) {
                    navController.popBackStack();
                } else {
                    Toast.makeText(getContext(), R.string.error_saving, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<PatientDto> call, Throwable t) {
                Toast.makeText(getContext(), R.string.error_saving, Toast.LENGTH_LONG).show();
            }
        });

    }




    private void delete(){
        PatientWebServiceClient patientWebServiceClient = MainActivity.getRetrofitHttp()
                .retrofit.create(PatientWebServiceClient.class);

        Call<PatientDto> call;

        call = patientWebServiceClient.deletePatientDto(patientDto.getId());

        call.enqueue(new Callback<PatientDto>() {
            @Override
            public void onResponse(Call<PatientDto> call, Response<PatientDto> response) {
                if (response.isSuccessful()) {
                    PatientListFragment.patientList.remove(patientDto);
                    navController.popBackStack();
                } else {
                    Toast.makeText(getContext(), R.string.error_deleting, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<PatientDto> call, Throwable t) {
                Toast.makeText(getContext(), R.string.error_deleting, Toast.LENGTH_LONG).show();
            }
        });

    }


    private void fillUpInfoInLayout(PatientDto patientDto) {
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
        String patientReason = patientDto.getReason();
        Picasso.with(requireContext()).load(patientDto.getUrlPhoto()).into(circleImageView);

        if (patientName != null) nameEditText.setText(patientName);
        if (patientSurname != null) surnameEditText.setText(patientSurname);
        ageEditText.setText(patientAge);
        if (patientAddress != null) addressEditText.setText(patientAddress);
        if (patientPhone != null) phoneNumberEditText.setText(patientPhone);
        if (patientEmail != null) emailEditText.setText(patientEmail);
        if (patientSchool != null) schoolEditText.setText(patientSchool);
        if (patientCourse != null) courseEditText.setText(patientCourse);
        if (patientPaymentType != null) paymentTypeAutoCompleteTextView.setText(patientPaymentType, false);
        if (patientBirthDate != null) birthDateButton.setText(patientBirthDate);
        if (patientReason != null) reasonEditTExt.setText(patientReason);


    }


    private CreateUpdatePatientDto getCreateUpdatePatientFromFrontEnd() {
        CreateUpdatePatientDto createUpdatePatientDto = new CreateUpdatePatientDto();

        createUpdatePatientDto.setName(nameEditText.getText().toString());
        createUpdatePatientDto.setSurname(surnameEditText.getText().toString());
        createUpdatePatientDto.setReason(reasonEditTExt.getText().toString());
        createUpdatePatientDto.setPhoneNumber(phoneNumberEditText.getText().toString());
        createUpdatePatientDto.setEmail(emailEditText.getText().toString());
        createUpdatePatientDto.setDateOfBirth(birthDateButton.getText().toString());
        createUpdatePatientDto.setHomeAddress(addressEditText.getText().toString());
        createUpdatePatientDto.setSchoolName(schoolEditText.getText().toString());
        createUpdatePatientDto.setCourse(courseEditText.getText().toString());
        createUpdatePatientDto.setPaymentType(paymentTypeAutoCompleteTextView.getText().toString());
        createUpdatePatientDto.setClinicId(patientDto.getClinicId());
        if (createUpdatePatientDto.getClinicId() == null) {
            createUpdatePatientDto.setClinicId(MainActivity.getUserDto().getClinicId());
        }

        createUpdatePatientDto.setUrlPhoto(patientDto.getUrlPhoto());
        // TODO al crear patient peta por que el id de clinica es null, se debería coger de alguna manera el id de la clinica
        // TODO urlPhoto

        return createUpdatePatientDto;
    }


    public void deletePatientDialog(){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
        builder.setTitle(R.string.caution);
        builder.setMessage(R.string.sure);
        builder.setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton(R.string.deleteButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO delete
                delete();
            }
        });
        builder.show();
    }



    public void datePicker(View v) {
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select date: ");

        final MaterialDatePicker<Long> picker = builder.build();
        picker.show(getChildFragmentManager(), picker.toString());
        if (picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                Calendar today = Calendar.getInstance();
                long now = today.getTimeInMillis();

                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(selection);

                int year = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH) + 1;
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                String finalMonth;
                String finalDay;

                if (mMonth < 10){
                    finalMonth = "0" + mMonth;
                }else{
                    finalMonth = String.valueOf(mMonth);
                }

                if (mDay < 10){
                    finalDay = "0" + mDay;
                }else{
                    finalDay = String.valueOf(mDay);
                }
                String date = year + "-" + finalMonth + "-" + finalDay;

                birthDateButton.setText(date);


            }
        })) ;
    }
}