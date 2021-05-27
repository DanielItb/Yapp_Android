package cat.itb.yapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;

import cat.itb.yapp.R;

/**
 * Fragmento controlador que muestra la pantalla de bienvenida.
 * @author David Lama, Kenneth Griñan, Daniel Acosta
 *
 */
public class WelcomeFragment extends Fragment {
    private MaterialButton continueButton, backButton;
    private NavController navController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_welcome, container, false);
        continueButton = v.findViewById(R.id.continueWelcomeButton);
        backButton = v.findViewById(R.id.backToInitButton);

        continueButton.setOnClickListener(v1 -> navController.navigate(R.id.action_welcomeFragment_to_registerFormFragment));
        backButton.setOnClickListener(v1 -> navController.popBackStack());
        return v;
    }
}