package cat.itb.yapp.fragments.forms;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cat.itb.yapp.R;
import cat.itb.yapp.models.user.RegisterDtoRoleAdmin;
import cat.itb.yapp.retrofit.RetrofitHttpLogin;
import cat.itb.yapp.utils.ErrorUtils;
import cat.itb.yapp.webservices.UserWebServiceClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragmento controlador del formulario de registro.
 * @author David Lama, Kenneth Griñan, Daniel Acosta
 *
 */
public class RegisterFormFragment extends Fragment {
    private MaterialButton cancelButton, registerButton;
    private NavController navController;
    private AutoCompleteTextView specialistTypeAutoCompleteTextView;
    private TextInputEditText usernameEditText, passwordEditText, repeatPasswordEditText, nameEditText, surnameEditText, phoneEditText, emailEditText, collegiateNumberEditText, clinicNameEditTExt, clinicPhoneEditTExt, clinicAddressEditText, clinicEmailEditText;
    private TextView loadTextView;
    private ProgressBar loadProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register_form, container, false);

        registerButton = v.findViewById(R.id.saveClinicButtonRegister);
        usernameEditText = v.findViewById(R.id.userNameEditTextRegister);
        passwordEditText = v.findViewById(R.id.passwordEditTextRegister);
        repeatPasswordEditText = v.findViewById(R.id.repeatPasswordEditTextRegister);
        nameEditText = v.findViewById(R.id.nameEditTextRegister);
        surnameEditText = v.findViewById(R.id.surnameEditTextRegister);
        phoneEditText = v.findViewById(R.id.phoneEditTextRegister);
        emailEditText = v.findViewById(R.id.emailEditTextRegister);
        collegiateNumberEditText = v.findViewById(R.id.collegiateNumberEditTextRegister);
        clinicNameEditTExt = v.findViewById(R.id.nameClinicEditTextRegister);
        clinicPhoneEditTExt = v.findViewById(R.id.phoneClinicEditTextRegister);
        clinicAddressEditText = v.findViewById(R.id.addressClinicEditTextRegister);
        clinicEmailEditText = v.findViewById(R.id.emailClinicEditTextRegister);
        loadTextView = v.findViewById(R.id.loadingTextViewRegister);
        loadProgressBar = v.findViewById(R.id.progressBarRegister);

        final String[] specialistTypes = getResources().getStringArray(R.array.specialist_types);
        specialistTypeAutoCompleteTextView = v.findViewById(R.id.autoCompleteSpecialistRegister);
        ArrayAdapter<String> adapterSpecialist = new ArrayAdapter<String>(requireContext(), R.layout.drop_down_types, specialistTypes);
        specialistTypeAutoCompleteTextView.setAdapter(adapterSpecialist);


        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        registerButton.setOnClickListener(v -> {
            if (allRequiredCampsSet()) save();
        });
    }



    private boolean allRequiredCampsSet() {
        boolean allGood = true;
        CharSequence errorMsg = getText(R.string.must_fill);
        CharSequence errorLengthMsg = getText(R.string.length_pass);
        CharSequence errorEqualPassMsg = getText(R.string.equal_pass);
        CharSequence errorPhoneLenghtPassMsg = getText(R.string.phone_length);
        CharSequence errorInvalidEmailMsg = getText(R.string.invalid_mail);

        if (nameEditText.getText().toString().isEmpty()) {
            allGood = false;
            nameEditText.setError(errorMsg);

        } if (surnameEditText.getText().toString().isEmpty()) {
            allGood = false;
            surnameEditText.setError(errorMsg);

        } if (usernameEditText.getText().toString().isEmpty()) {
            allGood = false;
            usernameEditText.setError(errorMsg);

        } if(passwordEditText.getText().toString().isEmpty()) {
            allGood = false;
            passwordEditText.setError(errorMsg);

        } if(passwordEditText.getText().toString().length() < 6) {
            allGood = false;
            passwordEditText.setError(errorLengthMsg);

        } if(repeatPasswordEditText.getText().toString().isEmpty()) {
            allGood = false;
            repeatPasswordEditText.setError(errorMsg);

        } if(!passwordEditText.getText().toString().equals(repeatPasswordEditText.getText().toString())) {
            allGood = false;
            passwordEditText.setError(errorEqualPassMsg);
            repeatPasswordEditText.setError(errorEqualPassMsg);

        } if(phoneEditText.getText().toString().isEmpty()) {
            allGood = false;
            phoneEditText.setError(errorMsg);

        } if(phoneEditText.getText().toString().length() != 9 ) {
            allGood = false;
            phoneEditText.setError(errorPhoneLenghtPassMsg);

        } if(emailEditText.getText().toString().isEmpty()) {
            allGood = false;
            emailEditText.setError(errorMsg);

        } if(validate(emailEditText.getText().toString())) {
            allGood = false;
            emailEditText.setError(errorInvalidEmailMsg);

        } if(collegiateNumberEditText.getText().toString().isEmpty()) {
            allGood = false;
            collegiateNumberEditText.setError(errorMsg);

        } if(clinicNameEditTExt.getText().toString().isEmpty()) {
            allGood = false;
            clinicNameEditTExt.setError(errorMsg);

        } if(clinicAddressEditText.getText().toString().isEmpty()) {
            allGood = false;
            clinicAddressEditText.setError(errorMsg);

        } if(collegiateNumberEditText.getText().toString().isEmpty()) {
            allGood = false;
            collegiateNumberEditText.setError(errorMsg);

        } if(clinicEmailEditText.getText().toString().isEmpty()) {
            allGood = false;
            clinicEmailEditText.setError(errorMsg);

        } if(validate(clinicEmailEditText.getText().toString())) {
            allGood = false;
            clinicEmailEditText.setError(errorInvalidEmailMsg);

        } if(clinicPhoneEditTExt.getText().toString().isEmpty()) {
            allGood = false;
            clinicPhoneEditTExt.setError(errorMsg);

        } if(clinicPhoneEditTExt.getText().toString().length() != 9 ) {
            allGood = false;
            clinicPhoneEditTExt.setError(errorPhoneLenghtPassMsg);

        } if(specialistTypeAutoCompleteTextView.getText().toString().isEmpty()) {
            allGood = false;
            specialistTypeAutoCompleteTextView.setError(errorMsg);
        }



            return allGood;
    }

    /**
     * Método con un patrón regex para controlar que el formato del campo email es correcto.
     */
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    /**
     * Mátodo que valida el forato correcto del campo email.
     * @param emailStr String
     * @return Boolean
     */
    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return !matcher.find();
    }




    private void save() {
        loadProgressBar.setVisibility(View.VISIBLE);
        loadTextView.setVisibility(View.VISIBLE);
        final RegisterDtoRoleAdmin registerDtoRoleAdmin = getCreateAdminFromFrontEnd();

        UserWebServiceClient userWebServiceClient = new RetrofitHttpLogin()
                .retrofit.create(UserWebServiceClient.class);

        Call<RegisterDtoRoleAdmin> call;
        call = userWebServiceClient.addAdmin(registerDtoRoleAdmin);

        call.enqueue(new Callback<RegisterDtoRoleAdmin>() {
            @Override
            public void onResponse(Call<RegisterDtoRoleAdmin> call, Response<RegisterDtoRoleAdmin> response) {
                if (response.isSuccessful()) {
                    confirmEmailDialog();
                } else {
                    Toast.makeText(getContext(), ErrorUtils.getErrorString(response.errorBody()), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterDtoRoleAdmin> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private RegisterDtoRoleAdmin getCreateAdminFromFrontEnd() {
        RegisterDtoRoleAdmin registerDtoRoleAdmin = new RegisterDtoRoleAdmin();

        registerDtoRoleAdmin.setUsername(usernameEditText.getText().toString());
        registerDtoRoleAdmin.setName(nameEditText.getText().toString());
        registerDtoRoleAdmin.setSurnames(surnameEditText.getText().toString());
        registerDtoRoleAdmin.setPhone(phoneEditText.getText().toString());
        registerDtoRoleAdmin.setPassword(passwordEditText.getText().toString());
        registerDtoRoleAdmin.setPassword2(repeatPasswordEditText.getText().toString());
        registerDtoRoleAdmin.setEmail(emailEditText.getText().toString());
        registerDtoRoleAdmin.setCollegiateNumber(Integer.parseInt(collegiateNumberEditText.getText().toString()));
        registerDtoRoleAdmin.setNameClinic(clinicNameEditTExt.getText().toString());
        registerDtoRoleAdmin.setPhoneNumberClinic(clinicPhoneEditTExt.getText().toString());
        registerDtoRoleAdmin.setAddressClinic(clinicAddressEditText.getText().toString());
        registerDtoRoleAdmin.setEmailClinic(clinicEmailEditText.getText().toString());
        registerDtoRoleAdmin.setSpecialistType(specialistTypeAutoCompleteTextView.getText().toString());
        registerDtoRoleAdmin.setPhotoUrl("https://yapp-backend.herokuapp.com/files/placeholder-user-image.png");

        return registerDtoRoleAdmin;
    }


    private void confirmEmailDialog(){
        loadProgressBar.setVisibility(View.GONE);
        loadTextView.setVisibility(View.GONE);
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
        builder.setTitle(R.string.thanks_for_joining);
        builder.setMessage(R.string.check_email);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                navController.navigate(R.id.action_registerFormFragment_to_loginFragment);
            }
        });
        builder.show();
    }


}