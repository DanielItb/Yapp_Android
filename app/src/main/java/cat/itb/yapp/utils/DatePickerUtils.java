package cat.itb.yapp.utils;


import androidx.fragment.app.FragmentManager;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.util.Calendar;

public class DatePickerUtils {
    public static String datePicker(FragmentManager fragmentManager) {
        String[] date = new String[1];
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select date: ");
        CalendarConstraints.DateValidator dateValidator = DateValidatorPointForward.now();
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        constraintsBuilder.setValidator(dateValidator);
        builder.setCalendarConstraints(constraintsBuilder.build());
        final MaterialDatePicker<Long> picker = builder.build();
        picker.show(fragmentManager, picker.toString());
        if (picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
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
                date[0] = year + "-" + finalMonth + "-" + finalDay;
            }
        })) ;

        return date[0];
    }
}
