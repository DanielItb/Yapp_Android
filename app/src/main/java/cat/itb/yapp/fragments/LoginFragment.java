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

import cat.itb.yapp.R;
import cat.itb.yapp.models.auth.LoginDto;
import cat.itb.yapp.models.auth.ResponseLoginDto;
import cat.itb.yapp.retrofit.RetrofitHttp;
import cat.itb.yapp.webservices.AuthWebServiceClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    public static String BASE_URL = "http://10.0.2.2:8080/api/auth/";
    public static RetrofitHttp retrofitHttp;

    private NavController navController;

    TextInputEditText username;
    TextInputEditText password;
    Button btnLogin;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        username = v.findViewById(R.id.usernameLoginEditText);
        password = v.findViewById(R.id.passwordLoginEditText);
        btnLogin = v.findViewById(R.id.loginButton);

        username.setText("username");
        password.setText("password");


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //TODO: validate not empty
                if (username.getText() != null && password.getText() != null) {
                    login();
                }
                Log.e("login", username.getText().toString() + " " + password.getText().toString());
                //TODO send request and get token
            }
        });





        return v;
    }


    public void login() {

        retrofitHttp = new RetrofitHttp(BASE_URL);

        AuthWebServiceClient authWebServiceClient = retrofitHttp.retrofit.create(AuthWebServiceClient.class);

        LoginDto loginDto = new LoginDto();
        loginDto.setUsername(username.getText().toString());
        loginDto.setPassword(password.getText().toString());

        Call<LoginDto> call = authWebServiceClient.login(loginDto);

        call.enqueue(new Callback<LoginDto>() {
            @Override
            public void onResponse(Call<LoginDto> call, Response<LoginDto> response) {
                Log.e("login", "onResponse");
            }

            @Override
            public void onFailure(Call<LoginDto> call, Throwable t) {
                Log.e("login", "onResponse");

                Log.e("login", call.toString());
            }
        });



    }



}