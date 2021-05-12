package cat.itb.yapp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import cat.itb.yapp.R;
import cat.itb.yapp.models.user.ProfileUserDto;
import cat.itb.yapp.models.user.User;
import cat.itb.yapp.retrofit.RetrofitHttp;
import cat.itb.yapp.utils.UtilsSharedPreferences;

public class MainActivity extends AppCompatActivity {

    private static Activity activity;

    private static ProfileUserDto profileUser;
    private MaterialToolbar toolbar;
    private View navHostFragmentLayout;
    private static RetrofitHttp retrofitHttp;

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

/*
        String rol = UtilsSharedPreferences.getKey(this, UtilsSharedPreferences.KEY_TOKEN_FILE);
        if (rol != null) {
            setStartDestination(navController, R.id.mainFragment);
        }
        //
*/

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

    private void setStartDestination(NavController navController, int startDestination) {
        NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.nav_graph);
        navGraph.setStartDestination(startDestination);

        navController.setGraph(navGraph);
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

    public static RetrofitHttp getRetrofitHttp() {
        return retrofitHttp;
    }

    public static void setRetrofitHttp(RetrofitHttp retrofitHttp) {
        MainActivity.retrofitHttp = retrofitHttp;
    }
}