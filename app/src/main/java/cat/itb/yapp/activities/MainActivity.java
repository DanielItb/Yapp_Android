package cat.itb.yapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;

import cat.itb.yapp.R;

public class MainActivity extends AppCompatActivity {

    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;

    }
}