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


public class InitialFragment extends Fragment {
    private MaterialButton registerButton, loginButton;
    private NavController navController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_initial, container, false);

        registerButton = v.findViewById(R.id.registerInitialButton);
        loginButton = v.findViewById(R.id.loginInitialButton);

        registerButton.setOnClickListener(v1 -> navController.navigate(R.id.action_initialFragment_to_welcomeFragment));
        loginButton.setOnClickListener(v1 -> navController.navigate(R.id.action_initialFragment_to_loginFragment));
        return v;
    }

}