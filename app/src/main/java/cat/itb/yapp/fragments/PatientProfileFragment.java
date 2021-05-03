package cat.itb.yapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;

import cat.itb.yapp.R;


public class PatientProfileFragment extends Fragment {
    private MaterialButton birthDateButton;
    private TextInputEditText registerDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_patient_profile, container, false);

        registerDate = v.findViewById(R.id.registerDateEditText);

        birthDateButton = v.findViewById(R.id.birthDateButton);
        birthDateButton.setOnClickListener(this::datePickerCall);

        return v;
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