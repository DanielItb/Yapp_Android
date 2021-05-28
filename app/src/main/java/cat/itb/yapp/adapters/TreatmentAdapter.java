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
import cat.itb.yapp.models.treatment.TreatmentDto;
/**
 * Adapter del objeto TreatmentDto. Carga las listas de tratamientos.
 * @author David Lama, Kenneth Griñan, Daniel Acosta
 *
 */
public class TreatmentAdapter extends RecyclerView.Adapter<TreatmentAdapter.Viewholder> implements Filterable {
    private final List<TreatmentDto> listTreatment;
    private final List<TreatmentDto> listTreatmentFilter;
    private final RecyclerItemClickListener recyclerItemClickListener;

    /**
     * Constructor del adapter.
     * @param listTreatment Lista de objetos TreatmentDto
     * @param recyclerItemClickListener Objeto RecyclerItemClickListener
     */
    public TreatmentAdapter(List<TreatmentDto> listTreatment, RecyclerItemClickListener recyclerItemClickListener) {
        this.listTreatment = listTreatment;
        listTreatmentFilter = new ArrayList<>(listTreatment);
        this.recyclerItemClickListener = recyclerItemClickListener;
    }


    /**
     * ViehHolder que extiende de RecyclerView e implementa un onClickListener.
     */
    public static class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView treatmentIdTextView;
        TextView startDateTreatmentTextView;
        TextView patientFullNameTreatmentTextView;
        private final RecyclerItemClickListener recyclerItemClickListener;


        /**
         * Constructor de la clase ViewHolder.
         * @param recyclerItemClickListener Objeto RecyclerItemClickListener
         * @param itemView Objeto View
         */
        public Viewholder(@NonNull View itemView, RecyclerItemClickListener recyclerItemClickListener) {
            super(itemView);
            this.recyclerItemClickListener = recyclerItemClickListener;

            treatmentIdTextView = itemView.findViewById(R.id.idTreatmentTextView);
            startDateTreatmentTextView = itemView.findViewById(R.id.startDateTreatmentTextView);
            patientFullNameTreatmentTextView = itemView.findViewById(R.id.patientNameTreatmentTextView);

            itemView.setOnClickListener(this);
        }

        /**
         * Setea los datos en el elemento pertinente.
         * @param treatment Objeto TreatmentDto
         */
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

    /**
     * Retorna la lista filtrada.
     * @return Objeto Filter
     */
    @Override
    public Filter getFilter() {
        return treatmentFilter;
    }

    private Filter treatmentFilter = new Filter() {
        /**
         * Filtra la lista que mostrará el recycler dependiendo del patrón de texto recibido como parámetro.
         * @param constraint Objeto CharSequence
         * @return Objeto FilterResults
         */
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

        /**
         * Carga la listra filtrada.
         * @param constraint Objeto CharSequence
         * @param results Objeto FilterResults
         */
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listTreatment.clear();
            listTreatment.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };


}
