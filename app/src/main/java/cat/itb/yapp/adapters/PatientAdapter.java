package cat.itb.yapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cat.itb.yapp.R;
import cat.itb.yapp.models.patient.PatientDto;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Adapter del objeto PatientDto. Carga las listas de pacientes.
 * @author David Lama, Kenneth Griñan, Daniel Acosta
 *
 *
 */
public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.ViewHolder> implements Filterable {
    private final List<PatientDto> listPatient;
    private final List<PatientDto> listPatientFilter;
    private final RecyclerItemClickListener recyclerItemClickListener;

    /**
     * Constructor del adapter.
     * @param listPatient Lista de objetos PatientDto
     * @param recyclerItemClickListener Objeto RecyclerItemClickListener
     */
    public PatientAdapter(List<PatientDto> listPatient, RecyclerItemClickListener recyclerItemClickListener) {
        this.listPatient = listPatient;
        listPatientFilter = new ArrayList<>(listPatient);
        this.recyclerItemClickListener = recyclerItemClickListener;
    }

    /**
     * ViehHolder que extiende de RecyclerView e implementa un onClickListener.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final RecyclerItemClickListener recyclerItemClickListener;
        TextView patientNameTextView;
        TextView patientReasonTextView;
        TextView patientAgeTextView;
        CircleImageView patientImageView;

        /**
         * Constructor de la clase ViewHolder.
         * @param recyclerItemClickListener Objeto RecyclerItemClickListener
         * @param itemView Objeto View
         */
        public ViewHolder(RecyclerItemClickListener recyclerItemClickListener, @NonNull View itemView) {
            super(itemView);
            this.recyclerItemClickListener = recyclerItemClickListener;
            patientNameTextView = itemView.findViewById(R.id.namePatientTextView);
            patientAgeTextView = itemView.findViewById(R.id.agePatientTextView);
            patientReasonTextView = itemView.findViewById(R.id.reasonPatientTextView);
            patientImageView = itemView.findViewById(R.id.circleImageView);

            itemView.setOnClickListener(this);
        }

        /**
         * Setea los datos en el elemento pertinente.
         * @param patient Objeto PatientDto
         */
        public void binData(PatientDto patient) {
            patientNameTextView.setText(patient.getName() + " " + patient.getSurname());
            patientAgeTextView.setText(String.valueOf(patient.getAge()));
            patientReasonTextView.setText(patient.getReason());

            Context context = patientImageView.getContext();
            Picasso.with(context).load(patient.getUrlPhoto()).into(patientImageView);
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


    /**
     * Retorna la lista filtrada.
     * @return Objeto Filter
     */
    @Override
    public Filter getFilter() {
        return patientFilter;
    }


    private Filter patientFilter = new Filter() {
        /**
         * Filtra la lista que mostrará el recycler dependiendo del patrón de texto recibido como parámetro.
         * @param constraint Objeto CharSequence
         * @return Objeto FilterResults
         */
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

        /**
         * Carga la listra filtrada.
         * @param constraint Objeto CharSequence
         * @param results Objeto FilterResults
         */
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
             listPatient.clear();
             listPatient.addAll((List)results.values);
             notifyDataSetChanged();
        }
    };




}
