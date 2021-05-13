package cat.itb.yapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.Picasso;

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
        // TODO: remove
        usernameTextInput.setText("userc4");
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
                    setUpNavDrawerInfo(profileUserDto.getUsername(), "No email in dto D:", profileUserDto.getPhoto());

                    UtilsSharedPreferences.setToken(MainActivity.getActivity(), profileUserDto.getAccessToken());


                    MainActivity.setRetrofitHttp(new RetrofitHttp());
                    navController.navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment());



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


    private void setUpNavDrawerInfo(String username, String mail, String photoUrl) {
        MaterialTextView textViewUsername, textViewMail;
        ShapeableImageView imageViewProfile;
        FragmentActivity mainActivity = getActivity();

        textViewUsername = mainActivity.findViewById(R.id.textViewUsernameDrawer);
        textViewMail = mainActivity.findViewById(R.id.textViewEmailDrawer);
        imageViewProfile = mainActivity.findViewById(R.id.shapeableImageViewUser);

        textViewUsername.setText(username);
        textViewMail.setText(mail);
        Picasso.with(requireContext()).load(photoUrl).into(imageViewProfile);
    }


}