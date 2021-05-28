package cat.itb.yapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.util.List;

import cat.itb.yapp.R;
import cat.itb.yapp.activities.MainActivity;
import cat.itb.yapp.models.patient.PatientDto;
import cat.itb.yapp.models.treatment.TreatmentDto;
import cat.itb.yapp.models.user.UserDto;
import cat.itb.yapp.utils.UtilsAuth;

/**
 * Fragmento controlador el home.
 * @author David Lama, Kenneth Griñan, Daniel Acosta
 *
 */
public class MainFragment extends Fragment {
    private NavController navController;
    private UserDto userDto;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navController = NavHostFragment.findNavController(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CardView usersCardView, patientsCardView, mtsCardView, reportCardView, treatmentCardView,
                centerCardView;
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        usersCardView = v.findViewById(R.id.usersCardViewButton);
        patientsCardView = v.findViewById(R.id.patientsCardViewButton);
        mtsCardView = v.findViewById(R.id.mtsCardViewButton);
        reportCardView = v.findViewById(R.id.reportCardViewButton);
        treatmentCardView = v.findViewById(R.id.treatmentCardViewButton);
        centerCardView = v.findViewById(R.id.centerCardViewButton);
        ImageView userMenuImageView = v.findViewById(R.id.userIconImageView);
        TextView userMenuTextView = v.findViewById(R.id.usersTextViewMain);

        if (!UtilsAuth.getIsAdminRole(MainActivity.getUser().getRoles())) {
            userMenuImageView.setImageResource(R.drawable.profile_icon_main);
            userMenuTextView.setText(R.string.profile);
        }


        usersCardView.setOnClickListener(v1 -> rolUserAction());
        patientsCardView.setOnClickListener(v1 -> navController.navigate(R.id.action_mainFragment_to_patientListFragment));
        mtsCardView.setOnClickListener(v1 -> navController.navigate(R.id.action_mainFragment_to_calendarFragment));
        reportCardView.setOnClickListener(v1 -> navController.navigate(R.id.action_mainFragment_to_reportListFragment));
        treatmentCardView.setOnClickListener(v1 -> navController.navigate(R.id.action_mainFragment_to_treatmentListFragment));
        centerCardView.setOnClickListener(v1 -> navController.navigate(R.id.action_mainFragment_to_clinicFormFragment));

        return v;
    }


    private void rolUserAction(){
        if (UtilsAuth.getIsAdminRole(MainActivity.getUser().getRoles())) {
            navController.navigate(R.id.action_mainFragment_to_userListFragment);
        }else{
            userDto = MainActivity.getUserDto();
            sendUserToForm(userDto);
        }
    }

    private void sendUserToForm(UserDto userDto) {
        MainFragmentDirections.ActionMainFragmentToUserFormFragment dir =
                MainFragmentDirections.actionMainFragmentToUserFormFragment();
        dir.setUserDto(userDto);

        navController.navigate(dir);
    }

}