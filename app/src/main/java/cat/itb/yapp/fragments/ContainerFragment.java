package cat.itb.yapp.fragments;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cat.itb.yapp.R;
import de.hdodenhof.circleimageview.CircleImageView;


public class ContainerFragment extends Fragment {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;

    private Fragment login, userForm, main;
    private CircleImageView imageView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_container, container, false);
        toolbar = v.findViewById(R.id.toolbar);
        drawerLayout = v.findViewById(R.id.drawerLayout);

        login = new LoginFragment();
        userForm = new UserFormFragment();
        main = new MainFragment();


        imageView = v.findViewById(R.id.profileImage);

        imageView.setImageResource(R.drawable.kid);
        toolbar.setNavigationOnClickListener(v1 -> drawerLayout.open());

        return v;
    }

    public void fragmenteando(Fragment fragment){
        getChildFragmentManager().beginTransaction().add(R.id.containerFragments, fragment).commit();
    }
}