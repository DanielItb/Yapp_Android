package cat.itb.yapp.fragments.forms;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textfield.TextInputEditText;

import cat.itb.yapp.R;
import cat.itb.yapp.models.treatment.TreatmentDto;

public class TreatmentFormFragment extends Fragment {

    private TextInputEditText editTextSessions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_treatment_form, container, false);

        editTextSessions = v.findViewById(R.id.sessionsEditText);


        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TreatmentDto treatment = TreatmentFormFragmentArgs.fromBundle(getArguments()).getTreatmentDto();

        if (treatment != null) {
            editTextSessions.setText(treatment.getSessionsFinished());
        }
    }
}