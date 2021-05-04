package cat.itb.yapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;

import cat.itb.yapp.R;
import cat.itb.yapp.models.user.ProfileUserDto;
import cat.itb.yapp.models.user.User;

public class MainActivity extends AppCompatActivity {

    private static Activity activity;

    private static ProfileUserDto profileUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;

    }


    public static Activity getActivity() {
        return activity;
    }

    public static ProfileUserDto getUser() {
        return profileUser;
    }

    public static void setUser(ProfileUserDto user) {
        MainActivity.profileUser = user;
    }
}