package cat.itb.yapp.fragments.forms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import cat.itb.yapp.R;
import cat.itb.yapp.activities.MainActivity;
import cat.itb.yapp.models.clinic.ClinicDto;
import cat.itb.yapp.models.clinic.CreateUpdateClinicDto;
import cat.itb.yapp.retrofit.DatabaseUtils;
import cat.itb.yapp.utils.UtilsAuth;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClinicFormFragment extends Fragment {
    private NavController navController;
    private ClinicDto clinicDto;
    private CircleImageView imageViewLogo;
    private SwitchCompat editSwitch;
    private TextInputEditText editTextName, editTextAddress, editTextPhoneNumber, editTextEmail;
    private TextInputEditText[] editTexts;
    private MaterialButton buttonSave;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_clinic_form, container, false);

        imageViewLogo = v.findViewById(R.id.photoClinic);
        editSwitch = v.findViewById(R.id.editSwitchClinic);
        editTextName = v.findViewById(R.id.nameEditTextClinic);
        editTextAddress = v.findViewById(R.id.addressEditTextClinic);
        editTextPhoneNumber = v.findViewById(R.id.phoneEditTextClinic);
        editTextEmail = v.findViewById(R.id.emailEditTextClinic);
        buttonSave = v.findViewById(R.id.saveClinicButton);

        editTexts = new TextInputEditText[]{editTextName, editTextAddress, editTextPhoneNumber, editTextEmail};

        if (UtilsAuth.getIsAdminRole(MainActivity.getUser().getRoles())) {
            editSwitch.setEnabled(true);
        }
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (clinicDto == null) loadClinicFromDb();
        else loadInfoInLayout(clinicDto);

        buttonSave.setOnClickListener(this::save);

        editSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) focusable();
            else notFocusable();
        });

    }

    private void notFocusable() {
        for (TextInputEditText editText : editTexts) {
            editText.setFocusable(false);
        }
        buttonSave.setVisibility(View.GONE);
    }

    private void focusable() {
        for (TextInputEditText editText : editTexts) {
            editText.setFocusableInTouchMode(true);
        }
        buttonSave.setVisibility(View.VISIBLE);
    }

    private void save(View view) {
        CreateUpdateClinicDto createUpdateClinicDto = getInfoFromLayout();

        Callback<CreateUpdateClinicDto> callback = new Callback<CreateUpdateClinicDto>() {
            @Override
            public void onResponse(Call<CreateUpdateClinicDto> call, Response<CreateUpdateClinicDto> response) {
                if (response.isSuccessful()) {
                    navController.popBackStack();
                } else {
                    Toast.makeText(getContext(), R.string.error_saving, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CreateUpdateClinicDto> call, Throwable t) {
                Toast.makeText(getContext(), R.string.error_saving, Toast.LENGTH_LONG).show();
            }
        };

        DatabaseUtils.updateClinic(callback, createUpdateClinicDto, clinicDto.getId());
    }

    private CreateUpdateClinicDto getInfoFromLayout() {
        CreateUpdateClinicDto createUpdateClinicDto = new CreateUpdateClinicDto();

        createUpdateClinicDto.setAddress(editTextAddress.getText().toString());
        createUpdateClinicDto.setEmail(editTextEmail.getText().toString());
        createUpdateClinicDto.setName(editTextName.getText().toString());
        createUpdateClinicDto.setPhoneNumber(editTextPhoneNumber.getText().toString());

        return createUpdateClinicDto;
    }


    private void loadClinicFromDb() {
        DatabaseUtils.getClinicById(new Callback<ClinicDto>() {
            @Override
            public void onResponse(Call<ClinicDto> call, Response<ClinicDto> response) {
                if (response.isSuccessful()) {
                    clinicDto = response.body();
                    loadInfoInLayout(clinicDto);
                } else {
                    Toast.makeText(getContext(), "Error getting data", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ClinicDto> call, Throwable t) {
                Toast.makeText(getContext(), "Error getting data", Toast.LENGTH_LONG).show();
            }
        }, MainActivity.getUserDto().getClinicId());
    }

    private void loadInfoInLayout(ClinicDto clinicDto) {
        editTextName.setText(clinicDto.getName());
        editTextEmail.setText(clinicDto.getEmail());
        editTextPhoneNumber.setText(clinicDto.getPhoneNumber());
        editTextAddress.setText(clinicDto.getAddress());
        Picasso.with(getContext()).load(clinicDto.getPhoto()).into(imageViewLogo);
    }
}