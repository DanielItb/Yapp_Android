package cat.itb.yapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import cat.itb.yapp.R;
import cat.itb.yapp.activities.MainActivity;
import cat.itb.yapp.models.auth.LoginDto;
import cat.itb.yapp.models.treatment.TreatmentDto;
import cat.itb.yapp.models.user.ProfileUserDto;
import cat.itb.yapp.retrofit.RetrofitHttp;
import cat.itb.yapp.retrofit.RetrofitHttpLogin;
import cat.itb.yapp.utils.UtilsAuth;
import cat.itb.yapp.utils.UtilsSharedPreferences;
import cat.itb.yapp.webservices.AuthWebServiceClient;
import cat.itb.yapp.webservices.TreatmentWebServiceClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {


    public static RetrofitHttpLogin retrofitHttpLogin;

    ProfileUserDto profileUserDto;

    private NavController navController;

    TextInputEditText usernameTextInput;
    TextInputEditText passwordTextInput;
    Button btnLogin;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        String token = UtilsSharedPreferences.getToken(getActivity());
        if (token != null) {
            Log.e("login", "accessToken is present: " + token);
        } else {
            Log.e("login", "accessToken is == null");
        }


        usernameTextInput = v.findViewById(R.id.usernameLoginEditText);
        passwordTextInput = v.findViewById(R.id.passwordLoginEditText);
        // TESTING HARDCODE
        // username = ADMIN, username2 = USER ADMIN, username3 USER

        usernameTextInput.setText("username3");
        passwordTextInput.setText("password");


        btnLogin = v.findViewById(R.id.loginButton);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String username = usernameTextInput.getText().toString();
                String password = passwordTextInput.getText().toString();

                if (!username.isEmpty() && !password.isEmpty()) {
                    login();
                } else {
                    //TODO: show errors in layout
                    Log.e("login", "wrong credentials, username or password is empty");
                }

            }
        });

        return v;
    }

    public void login() {

        retrofitHttpLogin = new RetrofitHttpLogin();

        AuthWebServiceClient authWebServiceClient = retrofitHttpLogin.retrofit.create(AuthWebServiceClient.class);

        LoginDto loginDto = new LoginDto(usernameTextInput.getText().toString(),
                passwordTextInput.getText().toString());


        Call<ProfileUserDto> call = authWebServiceClient.login(loginDto);

        call.enqueue(new Callback<ProfileUserDto>() {
            @Override
            public void onResponse(Call<ProfileUserDto> call, Response<ProfileUserDto> response) {
                Log.e("login", "onResponse okey");
                if (response.isSuccessful()) {
                    ProfileUserDto profileUserDto = response.body();

                    Log.e("login", "id: "+ profileUserDto.getId().intValue());
                    Log.e("login", "username: "+profileUserDto.getUsername());
                    Log.e("login", "photo: "+profileUserDto.getPhoto());
                    Log.e("login", "accessToken: "+profileUserDto.getAccessToken());

                    profileUserDto.getRoles().forEach( rol ->
                            Log.e("login", rol)
                    );

                    profileUserDto = response.body();

                    MainActivity.setUser(profileUserDto);


                    UtilsSharedPreferences.setToken(MainActivity.getActivity(), profileUserDto.getAccessToken());



                    navController.navigate(R.id.mainFragment);

                    getTreatments();

                    Log.e("login", "accessToken from shared preferences: "+UtilsSharedPreferences.getToken(MainActivity.getActivity()));
                }else {
                    Toast.makeText(MainActivity.getActivity().getApplicationContext(), "error bad credentials", Toast.LENGTH_SHORT).show();
                    Log.e("login", "status response: " + response.code()); //401 Unauthorized
                    //delete token
                    UtilsSharedPreferences.setToken(MainActivity.getActivity(), null);
                }
            }


            @Override
            public void onFailure(Call<ProfileUserDto> call, Throwable t) {
                Log.e("login", "onResponse onFailure");
                Log.e("login", "throwable.getMessage(): "+t.getMessage());
                Log.e("login", "call.toString(): "+call.toString());
            }
        });



    }



    // TODO: desde el fragment main, al pulsar el boton de tratments o lo que sea, hacer la request
    // navegar a new Fragment( data );
    public void getTreatments() {
        //TODO: if is admin go to view admin ...
        Log.e("treatment", "id: "+MainActivity.getUser().getId());
        Log.e("treatment", "username: "+MainActivity.getUser().getUsername());

        final List<TreatmentDto>[] treatmentDtoList = new List[]{new ArrayList<>()};



        RetrofitHttp retrofitHttp = new RetrofitHttp();
        TreatmentWebServiceClient treatmentWebServiceClient = retrofitHttp.retrofit.create(TreatmentWebServiceClient.class);

        Call<List<TreatmentDto>> call;

        //CHECK USER ROLE
        if (UtilsAuth.getIsAdminRole(MainActivity.getUser().getRoles())) {
            call = treatmentWebServiceClient.getTreatments();
            Log.e("treatment", "is admin");
        } else if (UtilsAuth.getIsUserRole(MainActivity.getUser().getRoles())) {
            String endpointUserRole = "treatment/specialist/" +  MainActivity.getUser().getId().longValue();
            call = treatmentWebServiceClient.getTreatmentsBySpecialistId(endpointUserRole);
            Log.e("treatment", "is user");
        } else {
            Toast.makeText(MainActivity.getActivity().getApplicationContext(), "error, usuario sin rol? ", Toast.LENGTH_SHORT).show();
            call = null;
        }

        if (call != null) {
            call.enqueue(new Callback<List<TreatmentDto>>() {
                @Override
                public void onResponse(Call<List<TreatmentDto>> call, Response<List<TreatmentDto>> response) {
                    Log.e("treatment", "onResponse okey");
                    if (response.isSuccessful()) {
                        Log.e("treatment", "status response: " + response.code());
                        treatmentDtoList[0] = response.body();
                        treatmentDtoList[0].forEach(t-> {
                            Log.e("treatment", "status response: " + t.toString());
                        });

                    }else {
                        Toast.makeText(MainActivity.getActivity().getApplicationContext(), "error get treatment by specialistId", Toast.LENGTH_SHORT).show();
                        Log.e("treatment", "status response: " + response.code()); //401 Unauthorized
                    }
                }

                @Override
                public void onFailure(Call<List<TreatmentDto>> call, Throwable t) {
                    Log.e("treatment", "onResponse onFailure");
                    Log.e("treatment", "throwable.getMessage(): "+t.getMessage());
                    Log.e("treatment", "call.toString(): "+call.toString());
                }
            });
        }





    }
}