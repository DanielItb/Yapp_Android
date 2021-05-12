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
    private final List<UserDto> listUser;
    private final RecyclerItemClickListener recyclerItemClickListener;

    public UserAdapter(List<UserDto> listUser, RecyclerItemClickListener recyclerItemClickListener) {
        this.listUser = listUser;
        this.recyclerItemClickListener = recyclerItemClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView userNameTextView, userSpecialistTypeTextView;
        private final RecyclerItemClickListener recyclerItemClickListener;

        public ViewHolder(@NonNull View itemView, RecyclerItemClickListener recyclerItemClickListener) {
            super(itemView);
            this.recyclerItemClickListener = recyclerItemClickListener;
            userNameTextView = itemView.findViewById(R.id.nameUserTextView);
            userSpecialistTypeTextView = itemView.findViewById(R.id.specialistTypeUserTextView);

            itemView.setOnClickListener(this);
        }

        public void binData(UserDto user){
            userNameTextView.setText(user.getName() + " " + user.getSurnames());
            userSpecialistTypeTextView.setText(user.getSpecialistType());
        }

        @Override
        public void onClick(View v) {
            recyclerItemClickListener.onRecyclerItemClick(getAdapterPosition());
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false);
        return new ViewHolder(v, recyclerItemClickListener);
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
