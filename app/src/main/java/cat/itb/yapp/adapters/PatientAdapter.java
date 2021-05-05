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

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView patientName;
        TextView patientReason;
        TextView patientAge;
        CircleImageView patientImage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            patientName = itemView.findViewById(R.id.namePatientTextView);
            patientAge = itemView.findViewById(R.id.agePatientTextView);
//            patientReason = itemView.findViewById(R.id.reasonPatientTextView);
            patientImage = itemView.findViewById(R.id.circleImageView);
        }

        public void binData(Patient patient) {
            patientName.setText(patient.getName());
            patientAge.setText(String.valueOf(patient.getAge()));
//            patientReason.setText(patient.get);
            patientImage.setImageResource(R.drawable.kid);
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
