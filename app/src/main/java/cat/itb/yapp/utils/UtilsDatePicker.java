package cat.itb.yapp.utils;

import androidx.fragment.app.FragmentManager;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

/**
 * Clase utils para cargar un calendario con unas peculiaridades concretas.
 * @author David Lama, Kenneth Griñan, Daniel Acosta
 */
public class UtilsDatePicker {
    public static void showDatePicker(MaterialPickerOnPositiveButtonClickListener clickListener,
                                      FragmentManager fragmentManager) {
        MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker()
                .build();

        datePicker.addOnPositiveButtonClickListener(clickListener);
        datePicker.show(fragmentManager, "tag");
    }
}
