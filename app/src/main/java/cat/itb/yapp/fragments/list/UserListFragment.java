package cat.itb.yapp.fragments.list;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import cat.itb.yapp.R;
import cat.itb.yapp.activities.MainActivity;
import cat.itb.yapp.adapters.UserAdapter;
import cat.itb.yapp.models.user.UserDto;
import cat.itb.yapp.retrofit.RetrofitHttp;
import cat.itb.yapp.utils.UtilsAuth;
import cat.itb.yapp.webservices.UserWebServiceClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListFragment extends Fragment {
    private NavController navController;
    private RecyclerView recyclerView;
    private List<UserDto> listUsers = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_list, container, false);

        recyclerView = v.findViewById(R.id.recyclerUser);
        getUsers();

        FloatingActionButton fab = v.findViewById(R.id.fabUsers);

        fab.setOnClickListener(this::fabClicked);

        if (listUsers != null) setUpRecycler(recyclerView);

        return v;
    }

    private void fabClicked(View view) {
        navController.navigate(R.id.action_userListFragment_to_userFormFragment);
    }

    private void recyclerItemClicked(int position) {
        //TODO
    }

    private void setUpRecycler(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        UserAdapter adapter = new UserAdapter(listUsers, this::recyclerItemClicked);
        recyclerView.setAdapter(adapter);
    }


    public void getUsers() {
        Log.e("user", "role admin?: " + UtilsAuth.getIsAdminRole(MainActivity.getUser().getRoles()));
        Log.e("user", "role user?: " + UtilsAuth.getIsUserRole(MainActivity.getUser().getRoles()));


        //TODO: if is admin go to view admin ...

        MainActivity.getUser().getRoles().forEach(rol -> {
            Log.e("user", "role: " + rol);
        });

        //CHECK USER ROLE
        if (UtilsAuth.getIsAdminRole(MainActivity.getUser().getRoles())) {
            RetrofitHttp retrofitHttp = MainActivity.getRetrofitHttp();
            UserWebServiceClient userWebServiceClient = retrofitHttp.retrofit.create(UserWebServiceClient.class);

            String endpointUserRole = "user/";
            Call<List<UserDto>> call = userWebServiceClient.getUsers(endpointUserRole);
            Log.e("user", "all users in clinic");

            if (call != null) {
                call.enqueue(new Callback<List<UserDto>>() {
                    @Override
                    public void onResponse(Call<List<UserDto>> call, Response<List<UserDto>> response) {
                        Log.e("user", "onResponse okey");
                        if (response.isSuccessful()) {
                            Log.e("user", "status response: " + response.code());
                            listUsers = response.body();
                            setUpRecycler(recyclerView);
                        } else {
                            Toast.makeText(MainActivity.getActivity().getApplicationContext(), "error get user by specialistId", Toast.LENGTH_SHORT).show();
                            Log.e("user", "status response: " + response.code()); //401 Unauthorized
                        }
                    }

                    @Override
                    public void onFailure(Call<List<UserDto>> call, Throwable t) {
                        Log.e("user", "onResponse onFailure");
                        Log.e("user", "throwable.getMessage(): " + t.getMessage());
                        Log.e("user", "call.toString(): " + call.toString());
                    }
                });

            }

        } else if (UtilsAuth.getIsUserRole(MainActivity.getUser().getRoles())) {
            
            Log.e("user", "estoy");
            listUsers = new ArrayList<>(1);
            listUsers.add(MainActivity.getUserDto());

        } else {
            Toast.makeText(MainActivity.getActivity().getApplicationContext(), "error, usuario sin rol? ", Toast.LENGTH_SHORT).show();
        }
    }
}