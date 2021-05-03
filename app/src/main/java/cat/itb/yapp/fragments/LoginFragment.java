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

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import cat.itb.yapp.R;
import cat.itb.yapp.models.auth.LoginDto;
import cat.itb.yapp.retrofit.RetrofitHttp;
import cat.itb.yapp.utils.UtilsSharedPreferences;
import cat.itb.yapp.webservices.AuthWebServiceClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {


    public static RetrofitHttp retrofitHttp;

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
        usernameTextInput = v.findViewById(R.id.usernameLoginEditText);
        passwordTextInput = v.findViewById(R.id.passwordLoginEditText);
        btnLogin = v.findViewById(R.id.loginButton);

        //TESTING HARDCODE
        usernameTextInput.setText("username");
        passwordTextInput.setText("password");


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = usernameTextInput.getText().toString();
                String password = passwordTextInput.getText().toString();

                //TODO: show errors in layout
                if (username.length() > 0 && password.length() > 0) {
                    login();
                } else {
                    Log.e("login", "wrong credentials");
                }

            }
        });


        return v;
    }

    public void login() {

        retrofitHttp = new RetrofitHttp();

        AuthWebServiceClient authWebServiceClient = retrofitHttp.retrofit.create(AuthWebServiceClient.class);

        LoginDto loginDto = new LoginDto(usernameTextInput.getText().toString(),
                passwordTextInput.getText().toString());


        Call<LoginDto> call = authWebServiceClient.login(loginDto);

        call.enqueue(new Callback<LoginDto>() {
            @Override
            public void onResponse(Call<LoginDto> call, Response<LoginDto> response) {
                Log.e("login", "onResponse okey");

                //TODO
                // get token and info user(photo, username, ??) from response and save token in shared preferences and go to main
                UtilsSharedPreferences.saveToken(requireActivity(), "token");
            }

            @Override
            public void onFailure(Call<LoginDto> call, Throwable t) {
                Log.e("login", "onResponse onFailure");

                Log.e("login", call.toString());
            }
        });



    }
}