package cat.itb.yapp.fragments.forms;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.util.HashSet;

import cat.itb.yapp.R;
import cat.itb.yapp.activities.MainActivity;
import cat.itb.yapp.fragments.list.UserListFragment;
import cat.itb.yapp.models.user.CreateUserDto;
import cat.itb.yapp.models.user.UpdateUserDto;
import cat.itb.yapp.models.user.UserDto;
import cat.itb.yapp.utils.ErrorUtils;
import cat.itb.yapp.utils.UtilsAuth;
import cat.itb.yapp.webservices.UserWebServiceClient;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * Fragmento controlador del formulario de los datos de los usuarios (especialistas).
 * @author David Lama, Kenneth Griñan, Daniel Acosta
 *
 */
public class UserFormFragment extends Fragment {
    private NavController navController;
    private TextInputEditText usernameEditText, nameEditText, surnameEditText, phoneEditText, emailEditText, collegiateNumberEditText;
    private AutoCompleteTextView rolUserAutoCompleteTextView, specialistTypeAutoCompleteTextView;
    private MaterialButton saveButton, cancelButton;
    private CircleImageView circleImageView;
    private boolean editing, myProfile;
    private UserDto userDto = null;
    private SwitchCompat editSwitch;

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
        editSwitch = v.findViewById(R.id.editSwitchUser);


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
                notFocusable();
                fillUpInfoInLayout(userDto);

            } else if (userDto != null) { //If editing
                editing = true;
                notFocusable();
                fillUpInfoInLayout(userDto);
            } else { // If new
                userDto = new UserDto();
                editSwitch.setVisibility(View.GONE);
                focusable();
//                userDto.setDate(LocalDateTime.now().toString());
                editing = false;
            }
        } else { //If load data
            fillUpInfoInLayout(userDto);
        }

        if (!UtilsAuth.getIsAdminRole(MainActivity.getUser().getRoles())){
            cancelButton.setOnClickListener(v -> navController.popBackStack());
        }else{
            cancelButton.setText(R.string.deleteButton);
            cancelButton.setOnClickListener(v -> deleteUserDialog());
        }

        saveButton.setOnClickListener(v -> {
            if (allRequiredCampsSet()) save();
        });

        editSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (myProfile){
                        focusableEdit();
                    }else{
                        focusable();
                    }

                }else{
                    notFocusable();
                }
            }
        });

    }

    private void notFocusable(){
        usernameEditText.setFocusable(false);
        nameEditText.setFocusable(false);
        surnameEditText.setFocusable(false);
        phoneEditText.setFocusable(false);
        emailEditText.setFocusable(false);
        collegiateNumberEditText.setFocusable(false);
        rolUserAutoCompleteTextView.setFocusableInTouchMode(false);
        rolUserAutoCompleteTextView.setFocusable(false);
        rolUserAutoCompleteTextView.setClickable(false);
        rolUserAutoCompleteTextView.setFocusable(false);
        rolUserAutoCompleteTextView.setDropDownHeight(0);
        specialistTypeAutoCompleteTextView.setFocusableInTouchMode(false);
        specialistTypeAutoCompleteTextView.setFocusable(false);
        specialistTypeAutoCompleteTextView.setClickable(false);
        specialistTypeAutoCompleteTextView.setFocusable(false);
        specialistTypeAutoCompleteTextView.setDropDownHeight(0);
        saveButton.setVisibility(View.GONE);
        cancelButton.setVisibility(View.GONE);

    }

    private void focusable(){
        usernameEditText.setFocusableInTouchMode(true);
        nameEditText.setFocusableInTouchMode(true);
        surnameEditText.setFocusableInTouchMode(true);
        phoneEditText.setFocusableInTouchMode(true);
        emailEditText.setFocusableInTouchMode(true);
        collegiateNumberEditText.setFocusableInTouchMode(true);
        rolUserAutoCompleteTextView.setFocusableInTouchMode(true);
        rolUserAutoCompleteTextView.setFocusable(true);
        rolUserAutoCompleteTextView.setClickable(true);
        rolUserAutoCompleteTextView.setFocusable(true);
        rolUserAutoCompleteTextView.setDropDownHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        specialistTypeAutoCompleteTextView.setFocusableInTouchMode(true);
        specialistTypeAutoCompleteTextView.setFocusable(true);
        specialistTypeAutoCompleteTextView.setClickable(true);
        specialistTypeAutoCompleteTextView.setFocusable(true);
        specialistTypeAutoCompleteTextView.setDropDownHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        saveButton.setVisibility(View.VISIBLE);
        cancelButton.setVisibility(View.VISIBLE);
    }

    private void focusableEdit(){
        nameEditText.setFocusableInTouchMode(true);
        phoneEditText.setFocusableInTouchMode(true);
        surnameEditText.setFocusableInTouchMode(true);
        saveButton.setVisibility(View.VISIBLE);
        cancelButton.setVisibility(View.VISIBLE);
    }

    private boolean allRequiredCampsSet() {
        boolean allGood = true;
        if (editing){
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
        }else{

        }


        return allGood;
    }

    private void save() {
        UpdateUserDto updateUserDto;
        CreateUserDto userDtoCreate;

        UserWebServiceClient userWebServiceClient = MainActivity.getRetrofitHttp()
                .retrofit.create(UserWebServiceClient.class);

        Call<UserDto> call;
        if (editing) {
            updateUserDto = getUpdateUserFromFrontEnd();
            call = userWebServiceClient.updateDto("auth/updateuser/" + userDto.getId(),
                    updateUserDto);
        } else {
            userDtoCreate = getCreateUserFromFrontEnd();
            call = userWebServiceClient.addUser(userDtoCreate);
        }


        call.enqueue(new Callback<UserDto>() {
            @Override
            public void onResponse(Call<UserDto> call, Response<UserDto> response) {
                if (response.isSuccessful()) {
                    navController.popBackStack();
                } else {
                    Toast.makeText(getContext(), ErrorUtils.getErrorString(response.errorBody()), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserDto> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }


    private void delete(){
        UserWebServiceClient userService = MainActivity.getRetrofitHttp()
                .retrofit.create(UserWebServiceClient.class);

        Call<UserDto> call;

        call = userService.deleteUserDto(userDto.getId());

        call.enqueue(new Callback<UserDto>() {
            @Override
            public void onResponse(Call<UserDto> call, Response<UserDto> response) {
                if (response.isSuccessful()) {
                    UserListFragment.listUsers.remove(userDto);
                    navController.popBackStack();
                } else {
                    Toast.makeText(getContext(), R.string.error_deleting, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserDto> call, Throwable t) {
                Toast.makeText(getContext(), R.string.error_deleting, Toast.LENGTH_LONG).show();
            }
        });

    }

    private void deleteUserDialog(){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
        builder.setTitle(R.string.caution);
        builder.setMessage(R.string.sure);
        builder.setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton(R.string.deleteButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delete();
            }
        });
        builder.show();
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


    private UpdateUserDto getUpdateUserFromFrontEnd() {
        UpdateUserDto updateUserDto = new UpdateUserDto();

        updateUserDto.setName(nameEditText.getText().toString());
        updateUserDto.setSurnames(surnameEditText.getText().toString());
        updateUserDto.setPhone(phoneEditText.getText().toString());
        updateUserDto.setSpecialistType(specialistTypeAutoCompleteTextView.getText().toString());
        updateUserDto.setActive(true);
        updateUserDto.setIsAdminRole(UtilsAuth.getIsAdminRole(new HashSet<>(userDto.getRoles())));
        // TODO urlPhoto

        return updateUserDto;
    }

    private CreateUserDto getCreateUserFromFrontEnd() {
        CreateUserDto userDto = new CreateUserDto();

        userDto.setName(nameEditText.getText().toString());
        userDto.setPassword("password");
        userDto.setPassword2("password");
        userDto.setSurnames(surnameEditText.getText().toString());
        userDto.setPhone(phoneEditText.getText().toString());
        userDto.setEmail(emailEditText.getText().toString());
        userDto.setSpecialistType(specialistTypeAutoCompleteTextView.getText().toString());
        userDto.setUsername(usernameEditText.getText().toString());
        userDto.setCollegiateNumber(Integer.parseInt(collegiateNumberEditText.getText().toString()));
        userDto.setClinicId(MainActivity.getUserDto().getClinicId());
        userDto.setPhotoUrl("https://yapp-backend.herokuapp.com/files/placeholder-user-image.png");

        // TODO urlPhoto

        return userDto;
    }

}