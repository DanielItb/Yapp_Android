package cat.itb.yapp.fragments.forms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import cat.itb.yapp.R;
import cat.itb.yapp.activities.MainActivity;
import cat.itb.yapp.models.user.UpdateUserDto;
import cat.itb.yapp.models.user.UserDto;
import cat.itb.yapp.webservices.UserWebServiceClient;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFormFragment extends Fragment {
    private NavController navController;
    private TextInputEditText usernameEditText, nameEditText, surnameEditText, phoneEditText, emailEditText, collegiateNumberEditText;
    private AutoCompleteTextView rolUserAutoCompleteTextView, specialistTypeAutoCompleteTextView;
    private MaterialButton saveButton, cancelButton;
    private CircleImageView circleImageView;
    private boolean editing, myProfile;
    private UserDto userDto = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_form, container, false);

        usernameEditText = v.findViewById(R.id.userNameEditText);
        nameEditText = v.findViewById(R.id.nameEditText);
        surnameEditText = v.findViewById(R.id.surnameEditText);
        phoneEditText = v.findViewById(R.id.phoneEditText);
        emailEditText = v.findViewById(R.id.emailEditText);
        collegiateNumberEditText = v.findViewById(R.id.collegiateNumberEditText);
        saveButton = v.findViewById(R.id.saveButtonUserForm);
        cancelButton = v.findViewById(R.id.cancelButtonUserForm);
        circleImageView = v.findViewById(R.id.profile_image);


        final String[] rolTypes = getResources().getStringArray(R.array.rol_types);
        rolUserAutoCompleteTextView = v.findViewById(R.id.autoCompleteRol);
        ArrayAdapter<String> adapterRol = new ArrayAdapter<String>(requireContext(), R.layout.drop_down_types, rolTypes);
        rolUserAutoCompleteTextView.setAdapter(adapterRol);

        final String[] specialistTypes = getResources().getStringArray(R.array.specialist_types);
        specialistTypeAutoCompleteTextView = v.findViewById(R.id.autoCompleteSpecialist);
        ArrayAdapter<String> adapterSpecialist = new ArrayAdapter<String>(requireContext(), R.layout.drop_down_types, specialistTypes);
        specialistTypeAutoCompleteTextView.setAdapter(adapterSpecialist);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //If starts for first time
        if (userDto == null) {
            UserFormFragmentArgs userFormFragmentArgs =
                    UserFormFragmentArgs.fromBundle(getArguments());

            userDto = userFormFragmentArgs.getUserDto();
            myProfile = userFormFragmentArgs.getMyProfile();

            if (myProfile) {
                editing = true;
                userDto = MainActivity.getUserDto();
                fillUpInfoInLayout(userDto);

            } else if (userDto != null) { //If editing
                editing = true;
                fillUpInfoInLayout(userDto);
            } else { // If new
                userDto = new UserDto();
//                userDto.setDate(LocalDateTime.now().toString());
                editing = false;
            }
        } else { //If load data
            fillUpInfoInLayout(userDto);
        }

        cancelButton.setOnClickListener(v -> navController.popBackStack());
        saveButton.setOnClickListener(v -> {
            if (allRequiredCampsSet()) save();
        });

    }

    private boolean allRequiredCampsSet() {
        boolean allGood = true;
        CharSequence errorMsg = getText(R.string.must_fill);

        if (nameEditText.getText().toString().isEmpty()) {
            allGood = false;
            nameEditText.setError(errorMsg);
        }
        if (surnameEditText.getText().toString().isEmpty()) {
            allGood = false;
            surnameEditText.setError(errorMsg);
        }
        if (phoneEditText.getText().toString().isEmpty()) {
            allGood = false;
            phoneEditText.setError(errorMsg);
        }

        return allGood;
    }

    private void save() {
        final UpdateUserDto updateUserDto = getCreateUpdateUserFromFrontEnd();

        UserWebServiceClient userWebServiceClient = MainActivity.getRetrofitHttp()
                .retrofit.create(UserWebServiceClient.class);

        Call<UserDto> call;
        if (editing) call = userWebServiceClient.updateDto("auth/updateuser/" + userDto.getId(),
                updateUserDto);
        else call = userWebServiceClient.addUser(updateUserDto);

        call.enqueue(new Callback<UserDto>() {
            @Override
            public void onResponse(Call<UserDto> call, Response<UserDto> response) {
                if (response.isSuccessful()) {
                    navController.popBackStack();
                } else {
                    Toast.makeText(getContext(), R.string.error_saving, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserDto> call, Throwable t) {
                Toast.makeText(getContext(), R.string.error_saving, Toast.LENGTH_LONG).show();
            }
        });

    }


    private void fillUpInfoInLayout(UserDto userDto) {
        String userName = userDto.getName();
        String userSurname = userDto.getSurnames();
        String userUserName = userDto.getUsername();
        String userPhone = userDto.getPhone();
        String userEmail = userDto.getEmail();
        String userCollegiateNumber = String.valueOf(userDto.getCollegiateNumber());
        String userRol = userDto.getRoles().get(0);
        String userSpecialistType = userDto.getSpecialistType();

        Picasso.with(requireContext()).load(userDto.getPhotoUrl()).into(circleImageView);

        if (userName != null) nameEditText.setText(userName);
        if (userSurname != null) surnameEditText.setText(userSurname);
        if (userPhone != null) phoneEditText.setText(userPhone);
        if (userEmail != null) emailEditText.setText(userEmail);
        if (userUserName != null) usernameEditText.setText(userUserName);
        collegiateNumberEditText.setText(userCollegiateNumber);
        if (userRol != null) rolUserAutoCompleteTextView.setText(userRol);
        if (userSpecialistType != null)
            specialistTypeAutoCompleteTextView.setText(userSpecialistType, false);
    }


    private UpdateUserDto getCreateUpdateUserFromFrontEnd() {
        UpdateUserDto updateUserDto = new UpdateUserDto();

        updateUserDto.setName(nameEditText.getText().toString());
        updateUserDto.setSurnames(surnameEditText.getText().toString());
        updateUserDto.setPhone(phoneEditText.getText().toString());
        updateUserDto.setActive(true);

        // TODO urlPhoto

        return updateUserDto;
    }
}