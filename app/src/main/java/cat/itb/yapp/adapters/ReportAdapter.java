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
import cat.itb.yapp.models.report.ReportDto;
import cat.itb.yapp.models.user.User;

/**
 * Adapter del objeto RportDto. Carga las listas de informes.
 * @author David Lama, Kenneth Griñan, Daniel Acosta
 *
 */
public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> implements Filterable {
    private final List<ReportDto> listReport;
    private final List<ReportDto> listReportFilter;
    private final RecyclerItemClickListener recyclerItemClickListener;

    /**
     * Constructor del adapter.
     * @param listReport Lista de objetos RportDto
     * @param recyclerItemClickListener Objeto RecyclerItemClickListener
     */
    public ReportAdapter(List<ReportDto> listReport, RecyclerItemClickListener recyclerItemClickListener) {
        this.listReport = listReport;
        listReportFilter = new ArrayList<>(listReport);
        this.recyclerItemClickListener = recyclerItemClickListener;
    }


    /**
     * ViehHolder que extiende de RecyclerView e implementa un onClickListener.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView patientNameReportTextView, diagnosisReportTextView;
        private RecyclerItemClickListener recyclerItemClickListener;

        /**
         * Constructor de la clase ViewHolder.
         * @param itemView Objeto View
         * @param recyclerItemClickListener Objeto RecyclerItemClickListener
         */
        public ViewHolder(@NonNull View itemView, RecyclerItemClickListener recyclerItemClickListener) {
            super(itemView);
            this.recyclerItemClickListener = recyclerItemClickListener;
            patientNameReportTextView = itemView.findViewById(R.id.patientNameReportTextView);
            diagnosisReportTextView = itemView.findViewById(R.id.diagnosisReportTextView);

            itemView.setOnClickListener(this);
        }

        /**
         * Setea los datos en el elemento pertinente.
         * @param report Objeto ReportDto
         */
        public void binData(ReportDto report){
            patientNameReportTextView.setText(report.getPatientFullName());
            diagnosisReportTextView.setText(report.getDiagnosis());
        }

        @Override
        public void onClick(View v) {
            recyclerItemClickListener.onRecyclerItemClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_list_item, parent, false);
        return new ViewHolder(v, recyclerItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binData(listReport.get(position));
    }

    @Override
    public int getItemCount() {
        return listReport.size();
    }


    /**
     * Retorna la lista filtrada.
     * @return Objeto Filter
     */
    @Override
    public Filter getFilter() {
        return reportFilter;
    }

    private Filter reportFilter = new Filter() {
        /**
         * Filtra la lista que mostrará el recycler dependiendo del patrón de texto recibido como parámetro.
         * @param constraint Objeto CharSequence
         * @return Objeto FilterResults
         */
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ReportDto> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(listReportFilter);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (ReportDto item : listReportFilter) {
                    if (item.getPatientFullName().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
//                    else if (item.get.toLowerCase().contains(filterPattern)){
//                        filteredList.add(item);
//                    }
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
            listReport.clear();
            listReport.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };


}
