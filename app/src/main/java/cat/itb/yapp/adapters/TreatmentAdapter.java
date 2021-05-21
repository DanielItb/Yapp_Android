package cat.itb.yapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cat.itb.yapp.R;
import cat.itb.yapp.models.patient.PatientDto;
import cat.itb.yapp.models.treatment.TreatmentDto;

public class TreatmentAdapter extends RecyclerView.Adapter<TreatmentAdapter.Viewholder> implements Filterable {
    private final List<TreatmentDto> listTreatment;
    private final List<TreatmentDto> listTreatmentFilter;
    private final RecyclerItemClickListener recyclerItemClickListener;

    public TreatmentAdapter(List<TreatmentDto> listTreatment, RecyclerItemClickListener recyclerItemClickListener) {
        this.listTreatment = listTreatment;
        listTreatmentFilter = new ArrayList<>(listTreatment);
        this.recyclerItemClickListener = recyclerItemClickListener;
    }



    public static class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView treatmentIdTextView;
        TextView startDateTreatmentTextView;
        TextView patientFullNameTreatmentTextView;
        private final RecyclerItemClickListener recyclerItemClickListener;

        public Viewholder(@NonNull View itemView, RecyclerItemClickListener recyclerItemClickListener) {
            super(itemView);
            this.recyclerItemClickListener = recyclerItemClickListener;

            treatmentIdTextView = itemView.findViewById(R.id.idTreatmentTextView);
            startDateTreatmentTextView = itemView.findViewById(R.id.startDateTreatmentTextView);
            patientFullNameTreatmentTextView = itemView.findViewById(R.id.patientNameTreatmentTextView);

            itemView.setOnClickListener(this);
        }

        public void binData(TreatmentDto treatment){
            treatmentIdTextView.setText(treatment.getId());
            startDateTreatmentTextView.setText(treatment.getStartDate());
            patientFullNameTreatmentTextView.setText(treatment.getPatientFullName());
        }

        @Override
        public void onClick(View v) {
            recyclerItemClickListener.onRecyclerItemClick(getAdapterPosition());
        }
    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.treatment_list_item, parent, false);
        return new Viewholder(v, recyclerItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.binData(listTreatment.get(position));
    }

    @Override
    public int getItemCount() {
        return listTreatment.size();
    }

    @Override
    public Filter getFilter() {
        return treatmentFilter;
    }

    private Filter treatmentFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<TreatmentDto> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(listTreatmentFilter);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (TreatmentDto item : listTreatmentFilter) {
                    if (item.getPatientFullName().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listTreatment.clear();
            listTreatment.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };


}
