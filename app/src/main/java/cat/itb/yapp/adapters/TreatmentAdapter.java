package cat.itb.yapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cat.itb.yapp.R;
import cat.itb.yapp.models.treatment.Treatment;

public class TreatmentAdapter extends RecyclerView.Adapter<TreatmentAdapter.Viewholder> {
    private List<Treatment> listTreatment;

    public TreatmentAdapter(List<Treatment> listTreatment) {
        this.listTreatment = listTreatment;
    }

    public static class Viewholder extends RecyclerView.ViewHolder {


        public Viewholder(@NonNull View itemView) {
            super(itemView);
            //Todo instanciar elementos del item
        }

        public void binData(Treatment treatment){
            //todo introducir los datos en los elementos
        }

    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.treatment_list_item, parent, false);
        return new Viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.binData(listTreatment.get(position));
    }

    @Override
    public int getItemCount() {
        return listTreatment.size();
    }



}
