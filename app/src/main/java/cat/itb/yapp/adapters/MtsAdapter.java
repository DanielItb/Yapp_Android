package cat.itb.yapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cat.itb.yapp.R;
import cat.itb.yapp.models.mts.MtsDto;
import cat.itb.yapp.models.user.User;

public class MtsAdapter extends RecyclerView.Adapter<MtsAdapter.ViewHolder> {
    private List<MtsDto> listMts;

    public MtsAdapter(List<MtsDto> listMts) {
        this.listMts = listMts;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Todo instanciar elementos del item
        }

        public void binData(MtsDto mts){
            //todo introducir los datos en los elementos
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mts_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binData(listMts.get(position));
    }

    @Override
    public int getItemCount() {
        return listMts.size();
    }


}
