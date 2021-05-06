package cat.itb.yapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import cat.itb.yapp.R;

public class MainActivity extends AppCompatActivity {

    private static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Yapp);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        NavigationView navView = findViewById(R.id.nav_view);
        final NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        final NavController navController = navHostFragment.getNavController();

        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(R.id.loginFragment, R.id.mainFragment)
                        .setOpenableLayout(drawerLayout)
                        .build();


        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

    }



    public static Activity getActivity() {
        return activity;
    }
}