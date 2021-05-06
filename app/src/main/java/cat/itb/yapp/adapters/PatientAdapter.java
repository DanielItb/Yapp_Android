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
    private List<Patient> listPatient;


    public PatientAdapter(List<Patient> listPatient) {
        this.listPatient = listPatient;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView patientNameTextView;
        TextView patientReasonTextView;
        TextView patientAgeTextView;
        CircleImageView patientImageTextView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            patientNameTextView = itemView.findViewById(R.id.namePatientTextView);
            patientAgeTextView = itemView.findViewById(R.id.agePatientTextView);
//            patientReason = itemView.findViewById(R.id.reasonPatientTextView);
            patientImageTextView = itemView.findViewById(R.id.circleImageView);
        }

        public void binData(Patient patient) {
            patientNameTextView.setText(patient.getName());
            patientAgeTextView.setText(String.valueOf(patient.getAge()));
//            patientReason.setText(patient.get);
            patientImageTextView.setImageResource(R.drawable.kid);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_list_item, parent, false);
        return new ViewHolder(v);
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
