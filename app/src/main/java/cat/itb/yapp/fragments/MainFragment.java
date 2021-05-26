package cat.itb.yapp.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

import cat.itb.yapp.R;
import cat.itb.yapp.activities.MainActivity;
import cat.itb.yapp.models.patient.PatientDto;
import cat.itb.yapp.models.treatment.TreatmentDto;
import cat.itb.yapp.models.user.UserDto;
import cat.itb.yapp.retrofit.DatabaseUtils;
import cat.itb.yapp.utils.UtilsAuth;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainFragment extends Fragment {
    private NavController navController;
    private UserDto userDto;
    private List<UserDto> listUsersDtos;
    private List<PatientDto> listPatientsDto;
    private List<TreatmentDto> listTreatmmentsDto;

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


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (listUsersDtos == null){
            DatabaseUtils.getUsers(new Callback<List<UserDto>>() {
                @Override
                public void onResponse(Call<List<UserDto>> call, Response<List<UserDto>> response) {
                    if (response.isSuccessful()) {
                        listUsersDtos = response.body();
                        if (MainActivity.getUserDto().getSpecialistType().equalsIgnoreCase("none") && UtilsAuth.getIsAdminRole(MainActivity.getUser().getRoles())
                                && listUsersDtos.size() < 2){
                            stepsDialog("Step 1", "You need to create a specialist", "GO", 1);
//                        Toast.makeText(getContext(), "Crea un espacialista", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<UserDto>> call, Throwable t) {

                }
            });
        }else if (listPatientsDto == null || listPatientsDto.size() == 0){
            DatabaseUtils.getPatients(new Callback<List<PatientDto>>() {
                @Override
                public void onResponse(Call<List<PatientDto>> call, Response<List<PatientDto>> response) {
                    if (response.isSuccessful()) {
                        listPatientsDto = response.body();
                        if (listPatientsDto.size() == 0){
                            String title = "Step 2";
                            if (!UtilsAuth.getIsAdminRole(MainActivity.getUser().getRoles())){
                                title = "Step 1";
                            }
                            stepsDialog(title, "You need patients in your clinic", "GO", 2);
                        }
                    }
                }
                @Override
                public void onFailure(Call<List<PatientDto>> call, Throwable t) {

                }
            });
        }else if (listTreatmmentsDto == null || listTreatmmentsDto.size() == 0){
            DatabaseUtils.getTreatments(new Callback<List<TreatmentDto>>() {
                @Override
                public void onResponse(Call<List<TreatmentDto>> call, Response<List<TreatmentDto>> response) {
                    if (response.isSuccessful()) {
                        String title = "Final step";
                        listTreatmmentsDto = response.body();
                        if (listTreatmmentsDto.size() == 0){
                            stepsDialog(title, "Create a treatment associate to any of your patients\nNow, you can use all the features of YAPP", "GO", 3);
                        }

                    }

                }

                @Override
                public void onFailure(Call<List<TreatmentDto>> call, Throwable t) {

                }
            });
        }



    }

    public void stepsDialog(String title, String body, String go, int index){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
        builder.setTitle(title);
        builder.setMessage(body);
        builder.setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton(go, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (index == 1){
                    MainFragmentDirections.ActionMainFragmentToUserFormFragment dir =
                            MainFragmentDirections.actionMainFragmentToUserFormFragment();

                    dir.setMyProfile(false);
                    navController.navigate(dir);
                }else if(index == 2){
                    MainFragmentDirections.ActionMainFragmentToPatientFormFragment dir =
                            MainFragmentDirections.actionMainFragmentToPatientFormFragment();
                    navController.navigate(dir);
                }else if (index == 3){
                    MainFragmentDirections.ActionMainFragmentToTreatmentFormFragment dir =
                            MainFragmentDirections.actionMainFragmentToTreatmentFormFragment();
                    navController.navigate(dir);
                }

//                navController.navigate(R.id.action_mainFragment_to_userFormFragment);
            }
        });
        builder.show();
    }


    public void rolUserAction(){
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