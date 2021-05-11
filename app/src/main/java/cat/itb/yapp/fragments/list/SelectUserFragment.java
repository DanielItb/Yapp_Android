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


public class SelectUserFragment extends Fragment {
    private NavController navController;
    private RecyclerView recyclerView;
    private List<UserDto> listUsers = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navController = NavHostFragment.findNavController(this);

        //getParentFragmentManager().setFragmentResult("userId", result);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_select_user, container, false);

        recyclerView = v.findViewById(R.id.recyclerSelectUser);

        return v;
    }

    private void setUpRecycler(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        UserAdapter adapter = new UserAdapter(listUsers);
        recyclerView.setAdapter(adapter);

    }

    public void getUsers() {
        //TODO: if is admin go to view admin ...
        Log.e("user", "id: " + MainActivity.getUser().getId());
        Log.e("user", "username: " + MainActivity.getUser().getUsername());

        MainActivity.getUser().getRoles().forEach(rol -> {
            Log.e("user", "role: " + rol);
        });

        final List<UserDto>[] userDtoList = new List[]{new ArrayList<>()};


        RetrofitHttp retrofitHttp = new RetrofitHttp();
        UserWebServiceClient userWebServiceClient = retrofitHttp.retrofit.create(UserWebServiceClient.class);

        Call<List<UserDto>> call;


        Long specialistId = MainActivity.getUser().getId().longValue();
        //CHECK USER ROLE
        if (UtilsAuth.getIsAdminRole(MainActivity.getUser().getRoles())) {

            String endpointUserRole = "user/";
            call = userWebServiceClient.getUsers(endpointUserRole);
            Log.e("user", "all users in clinic");

        } else if (UtilsAuth.getIsUserRole(MainActivity.getUser().getRoles())) {

            String endpointUserRole = "user/" + specialistId;
            call = userWebServiceClient.getUsersBySpecialistId(endpointUserRole);
            Log.e("user", "all users by specialist");

        } else {
            Toast.makeText(MainActivity.getActivity().getApplicationContext(), "error, usuario sin rol? ", Toast.LENGTH_SHORT).show();
            call = null;
        }

        if (call != null) {
            call.enqueue(new Callback<List<UserDto>>() {
                @Override
                public void onResponse(Call<List<UserDto>> call, Response<List<UserDto>> response) {
                    Log.e("user", "onResponse okey");
                    if (response.isSuccessful()) {
                        Log.e("user", "status response: " + response.code());

                        listUsers = response.body();

                        setUpRecycler(recyclerView);


                        userDtoList[0].forEach(t -> {
                            Log.e("user", "status response: " + t.toString());
                        });

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
    }
}