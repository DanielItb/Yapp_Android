package cat.itb.yapp.fragments.forms;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

import cat.itb.yapp.R;
import cat.itb.yapp.activities.MainActivity;
import cat.itb.yapp.models.mts.MtsCreateUpdateDto;
import cat.itb.yapp.models.mts.MtsDto;
import cat.itb.yapp.utils.DatePickerUtils;
import cat.itb.yapp.utils.TimePickerUtils;
import cat.itb.yapp.webservices.MtsServiceClient;
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

        dateButton.setOnClickListener(this::datePicker);

//        dateButton.setOnClickListener(v1 -> setMtsDateAndTime());
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
        }
        if (specialistId == null) {
            allGood = false;
            specialistButton.setError(errorMsg);
        }
        if (reasonEditText.getText().toString().isEmpty()) {
            allGood = false;
            reasonEditText.setError(errorMsg);
        }

        return allGood;
    }

    private void save() {
        final MtsCreateUpdateDto mtsCreateUpdateDto = getInfoFromLayout();

        MtsServiceClient webServiceClient = MainActivity.getRetrofitHttp()
                .retrofit.create(MtsServiceClient.class);


        Call<MtsDto> call = null;
        if (editing)
            call = webServiceClient.updateMts("medicalsheet/" + mtsDto.getId(), mtsCreateUpdateDto);
        else call = webServiceClient.addMts(mtsCreateUpdateDto);

        call.enqueue(new Callback<MtsDto>() {
            @Override
            public void onResponse(Call<MtsDto> call, Response<MtsDto> response) {
                Log.d("mtsFrom", response.toString());
                if (response.isSuccessful()) {
                    navController.popBackStack();
                } else {
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


    public void datePicker(View v) {
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select date: ");
        CalendarConstraints.DateValidator dateValidator = DateValidatorPointForward.now();
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        constraintsBuilder.setValidator(dateValidator);
        builder.setCalendarConstraints(constraintsBuilder.build());
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

                timePicker(date);
            }
        })) ;
    }


    public void timePicker(String date) {
            // Get Current Time
            int mHour = 0;
            int mMinute = 0;

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String finalHour;
                    String finalMinute;

                    if (hourOfDay < 10){
                        finalHour = "0" + hourOfDay;
                    }else{
                        finalHour = String.valueOf(hourOfDay);
                    }

                    if (minute < 10){
                        finalMinute = "0" + minute;
                    }else{
                        finalMinute = String.valueOf(minute);
                    }
                    
                    String finalDate = date + "T" + finalHour + ":" + finalMinute + ":00" ;
                    dateButton.setText(finalDate);
                    mtsDto.setDate(finalDate);

                }
            }, mHour, mMinute, false);
            timePickerDialog.show();
    }



    public void setMtsDateAndTime(){
        DatePickerUtils datePickerUtils = new DatePickerUtils();
        String date;

        date = datePickerUtils.datePicker(getChildFragmentManager());

//        date = TimePickerUtils.timePicker(date, getContext());

        dateButton.setText(date);
        mtsDto.setDate(date);
    }
}




