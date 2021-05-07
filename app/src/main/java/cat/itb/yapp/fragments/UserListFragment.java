package cat.itb.yapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import cat.itb.yapp.R;

public class UserListFragment extends Fragment {
    private NavController navController;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_list, container, false);

        FloatingActionButton fab = v.findViewById(R.id.fabUsers);

        fab.setOnClickListener(this::fabClicked);

        return v;
    }

    private void fabClicked(View view) {
        navController.navigate(R.id.action_userListFragment_to_userFormFragment);
    }
}