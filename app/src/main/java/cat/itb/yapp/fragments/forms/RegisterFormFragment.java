package cat.itb.yapp.fragments.forms;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;

import cat.itb.yapp.R;

public class RegisterFormFragment extends Fragment {
    private MaterialButton cancelButton, registerButton;
    private NavController navController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register_form, container, false);

//        cancelButton = v.findViewById(R.id.cancelClinicButtonRegister);
        registerButton = v.findViewById(R.id.saveClinicButtonRegister);
        return v;
    }
}