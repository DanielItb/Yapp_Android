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
import cat.itb.yapp.models.user.UserDto;
import cat.itb.yapp.retrofit.RetrofitHttp;
import cat.itb.yapp.utils.UtilsSharedPreferences;

/**
 * Actividad principal que ejerce de contenedor de fragmentos y para comunicar determinados fragmentos con una información determinada.
 * @author David Lama, Kenneth Griñan, Daniel Acosta
 *
 */
public class MainActivity extends AppCompatActivity {

    private static Activity activity;

    private static ProfileUserDto profileUser;
    private static UserDto userDto;
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

//        setSupportActionBar(toolbar);

    }

    private void destinationChange(NavController navController, NavDestination navDestination, Bundle bundle) {
        if (navDestination.getId() == R.id.loginFragment) {
            UtilsSharedPreferences.setToken(this, null);
            profileUser = null;
            toolbar.setVisibility(View.GONE);
        } else if (navDestination.getId() == R.id.mainFragment) {
            toolbar.setVisibility(View.VISIBLE);
        }else if (navDestination.getId() == R.id.initialFragment){
            toolbar.setVisibility(View.GONE);
        }else if (navDestination.getId() == R.id.registerFormFragment){
            toolbar.setVisibility(View.VISIBLE);
        }else if (navDestination.getId() == R.id.welcomeFragment){
            toolbar.setVisibility(View.GONE);
        }
    }


    /**
     * Método que retorna el contexto de la actividad.
     * @return Activity.
     */
    public static Activity getActivity() {
        return activity;
    }

    /**
     * Método que retorna un objeto ProfileUserDto.
     * @return ProfileUserDto.
     */
    public static ProfileUserDto getUser() {
        return profileUser;
    }

    /**
     * Método que setea un objeto ProfileUserDto.
     * @param user Objeto ProfileUserDto.
     */
    public static void setUser(ProfileUserDto user) {
        MainActivity.profileUser = user;
    }

    /**
     * Método que retorna un objeto RetrofirHttp.
     * @return RetrofitHttp.
     */
    public static RetrofitHttp getRetrofitHttp() {
        return retrofitHttp;
    }

    /**
     * Método que setea un objeto RetrofitHttp.
     * @param retrofitHttp Objeto RetrofitHttp.
     */
    public static void setRetrofitHttp(RetrofitHttp retrofitHttp) {
        MainActivity.retrofitHttp = retrofitHttp;
    }

    /**
     * Método que retorna un objeto UserDto.
     * @return UserDto
     */
    public static UserDto getUserDto() {
        return userDto;
    }

    /**
     * Método que setea un objeto UserDto
     * @param userDto Objeto UserDto
     */
    public static void setUserDto(UserDto userDto) {
        MainActivity.userDto = userDto;
    }
}