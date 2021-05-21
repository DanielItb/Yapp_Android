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
import cat.itb.yapp.models.patient.Patient;
import cat.itb.yapp.models.patient.PatientDto;
import de.hdodenhof.circleimageview.CircleImageView;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.ViewHolder> implements Filterable {
    private final List<PatientDto> listPatient;
    private final List<PatientDto> listPatientFilter;
    private final RecyclerItemClickListener recyclerItemClickListener;


    public PatientAdapter(List<PatientDto> listPatient, RecyclerItemClickListener recyclerItemClickListener) {
        this.listPatient = listPatient;
        listPatientFilter = new ArrayList<>(listPatient);
        this.recyclerItemClickListener = recyclerItemClickListener;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final RecyclerItemClickListener recyclerItemClickListener;
        TextView patientNameTextView;
        TextView patientReasonTextView;
        TextView patientAgeTextView;
        CircleImageView patientImageTextView;


        public ViewHolder(RecyclerItemClickListener recyclerItemClickListener, @NonNull View itemView) {
            super(itemView);
            this.recyclerItemClickListener = recyclerItemClickListener;
            patientNameTextView = itemView.findViewById(R.id.namePatientTextView);
            patientAgeTextView = itemView.findViewById(R.id.agePatientTextView);
            patientReasonTextView = itemView.findViewById(R.id.reasonPatientTextView);
            patientImageTextView = itemView.findViewById(R.id.circleImageView);

            itemView.setOnClickListener(this);
        }

        public void binData(PatientDto patient) {
            patientNameTextView.setText(patient.getName() + " " + patient.getSurname());
            patientAgeTextView.setText(String.valueOf(patient.getAge()));
            patientReasonTextView.setText(patient.getReason());
            patientImageTextView.setImageResource(R.drawable.kid);
        }

        @Override
        public void onClick(View v) {
            recyclerItemClickListener.onRecyclerItemClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_list_item, parent, false);
        return new ViewHolder(recyclerItemClickListener, v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binData(listPatient.get(position));
    }

    @Override
    public int getItemCount() {
        return listPatient.size();
    }


    @Override
    public Filter getFilter() {
        return patientFilter;
    }

    private Filter patientFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<PatientDto> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(listPatientFilter);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (PatientDto item : listPatientFilter) {
                    if (item.getName().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }else if (item.getSurname().toLowerCase().contains(filterPattern)){
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
             listPatient.clear();
             listPatient.addAll((List)results.values);
             notifyDataSetChanged();
        }
    };




}
