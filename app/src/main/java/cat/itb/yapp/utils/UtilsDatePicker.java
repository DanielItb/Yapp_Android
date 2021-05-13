package cat.itb.yapp.utils;

import androidx.fragment.app.FragmentManager;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

public class UtilsDatePicker {
    public static void showDatePicker(MaterialPickerOnPositiveButtonClickListener clickListener,
                                      FragmentManager fragmentManager) {
        MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker()
                .build();

        datePicker.addOnPositiveButtonClickListener(clickListener);
        datePicker.show(fragmentManager, "tag");
    }
}
