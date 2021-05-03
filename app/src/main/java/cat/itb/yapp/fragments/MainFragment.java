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


public class MainFragment extends Fragment {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        toolbar = v.findViewById(R.id.toolbar);
        drawerLayout = v.findViewById(R.id.drawerLayout);

        toolbar.setNavigationOnClickListener(v1 -> drawerLayout.open());

        return v;
    }
}