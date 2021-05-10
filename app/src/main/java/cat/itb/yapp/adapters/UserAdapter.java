package cat.itb.yapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cat.itb.yapp.R;
import cat.itb.yapp.models.user.User;
import cat.itb.yapp.models.user.UserDto;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private List<UserDto> listUser;

    public UserAdapter(List<UserDto> listUser) {
        this.listUser = listUser;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView userIdTextView, userNameTextView, userSurnameTextView, userUserNameTextView, userSpecialistTypeTextView, userRolTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userIdTextView = itemView.findViewById(R.id.userIdTextView);
            userNameTextView = itemView.findViewById(R.id.nameUserTextView);
            userSurnameTextView = itemView.findViewById(R.id.surnameUserTextView);
            userUserNameTextView = itemView.findViewById(R.id.usernameTextView);
            userSpecialistTypeTextView = itemView.findViewById(R.id.specialistTypeUserTextView);
            userRolTextView = itemView.findViewById(R.id.rolUserEditText);

        }

        public void binData(UserDto user){
            userIdTextView.setText(String.valueOf(user.getId()));
            userNameTextView.setText(user.getName());
            userSurnameTextView.setText(user.getSurnames());
            userUserNameTextView.setText(user.getUsername());
            userSpecialistTypeTextView.setText(user.getSpecialistType());
            userRolTextView.setText(user.getRoles().get(0));
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binData(listUser.get(position));
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }


}
