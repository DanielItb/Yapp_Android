package cat.itb.yapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cat.itb.yapp.R;
import cat.itb.yapp.models.treatment.TreatmentDto;

public class TreatmentAdapter extends RecyclerView.Adapter<TreatmentAdapter.Viewholder> {
    private List<TreatmentDto> listTreatment;

    public TreatmentAdapter(List<TreatmentDto> listTreatment) {
        this.listTreatment = listTreatment;
    }

    public static class Viewholder extends RecyclerView.ViewHolder {
        TextView treatmentIdTextView;
        TextView startDateTreatmentTextView;
        TextView patientFullNameTreatmentTextView;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            treatmentIdTextView = itemView.findViewById(R.id.idTreatmentTextView);
            startDateTreatmentTextView = itemView.findViewById(R.id.startDateTreatmentTextView);
            patientFullNameTreatmentTextView = itemView.findViewById(R.id.patientNameTreatmentTextView);
        }

        public void binData(TreatmentDto treatment){
            treatmentIdTextView.setText(treatment.getId());
            startDateTreatmentTextView.setText(treatment.getStartDate());
            patientFullNameTreatmentTextView.setText(treatment.getPatientFullName());
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
