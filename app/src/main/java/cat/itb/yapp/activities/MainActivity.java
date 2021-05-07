package cat.itb.yapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import cat.itb.yapp.R;
import cat.itb.yapp.models.user.ProfileUserDto;
import cat.itb.yapp.models.user.User;
import cat.itb.yapp.utils.UtilsSharedPreferences;

public class MainActivity extends AppCompatActivity {

    private static Activity activity;

    private static ProfileUserDto profileUser;
    private MaterialToolbar toolbar;
    private View navHostFragmentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Yapp);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;
        navHostFragmentLayout = findViewById(R.id.nav_host_fragment);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        NavigationView navView = findViewById(R.id.nav_view);
        final NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        final NavController navController = navHostFragment.getNavController();

        navController.addOnDestinationChangedListener(this::destinationChange);

        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(R.id.mainFragment, R.id.treatmentListFragment,
                        R.id.patientListFragment, R.id.userListFragment, R.id.mtsListFragment,
                        R.id.reportListFragment)
                        .setOpenableLayout(drawerLayout)
                        .build();


        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

    }

    private void destinationChange(NavController navController, NavDestination navDestination, Bundle bundle) {
        if (navDestination.getId() == R.id.loginFragment) {
            UtilsSharedPreferences.setToken(this, null);
            profileUser = null;
            toolbar.setVisibility(View.GONE);
        } else if (navDestination.getId() == R.id.mainFragment) {
            toolbar.setVisibility(View.VISIBLE);
        }
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