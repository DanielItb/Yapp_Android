package cat.itb.yapp.fragments.forms;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import cat.itb.yapp.R;

public class UserFormFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_form, container, false);

        final String[] rolTypes = getResources().getStringArray(R.array.rol_types);
        AutoCompleteTextView rolTypesDropDown = v.findViewById(R.id.autoCompleteRol);
        ArrayAdapter<String> adapterRol = new ArrayAdapter<String>(requireContext(), R.layout.drop_down_types, rolTypes);
        rolTypesDropDown.setAdapter(adapterRol);

        final String[] specialistTypes = getResources().getStringArray(R.array.specialist_types);
        AutoCompleteTextView specialistTypesDropDown = v.findViewById(R.id.autoCompleteSpecialist);
        ArrayAdapter<String> adapterSpecialist = new ArrayAdapter<String>(requireContext(), R.layout.drop_down_types, specialistTypes);
        specialistTypesDropDown.setAdapter(adapterSpecialist);
        return v;
    }
}