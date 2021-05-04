package cat.itb.yapp.fragments;

import android.os.Bundle;

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

import cat.itb.yapp.R;


public class PatientFormFragment extends Fragment {
    private MaterialButton birthDateButton;
    private TextInputEditText registerDate;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_patient_profile, container, false);
        final String[] paymentTypes = getResources().getStringArray(R.array.payment_types);

        registerDate = v.findViewById(R.id.registerDateEditText);

        birthDateButton = v.findViewById(R.id.birthDateButton);
        birthDateButton.setOnClickListener(this::datePickerCall);


        AutoCompleteTextView paymentTypesDropDown = v.findViewById(R.id.autoComplete);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(), R.layout.drop_down_types, paymentTypes);
        paymentTypesDropDown.setAdapter(adapter);


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