package cat.itb.yapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cat.itb.yapp.R;
import cat.itb.yapp.models.patient.Patient;
import de.hdodenhof.circleimageview.CircleImageView;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.ViewHolder> {
    private final List<Patient> listPatient;
    private final RecyclerItemClickListener recyclerItemClickListener;


    public PatientAdapter(List<Patient> listPatient, RecyclerItemClickListener recyclerItemClickListener) {
        this.listPatient = listPatient;
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
//            patientReason = itemView.findViewById(R.id.reasonPatientTextView);
            patientImageTextView = itemView.findViewById(R.id.circleImageView);

            itemView.setOnClickListener(this);
        }

        public void binData(Patient patient) {
            patientNameTextView.setText(patient.getName());
            patientAgeTextView.setText(String.valueOf(patient.getAge()));
//            patientReason.setText(patient.get);
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




}
