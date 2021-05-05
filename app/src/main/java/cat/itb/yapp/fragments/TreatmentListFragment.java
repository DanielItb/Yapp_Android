package cat.itb.yapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cat.itb.yapp.R;
import cat.itb.yapp.apapters.TreatmentAdapter;


public class TreatmentListFragment extends Fragment {
    private RecyclerView recyclerView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_treatment_list, container, false);
        recyclerView = v.findViewById(R.id.recyclerTreatment);
        setUpRecycler(recyclerView);

        return v;
    }

    private void setUpRecycler(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        TreatmentAdapter adapter = new TreatmentAdapter();
        recyclerView.setAdapter(adapter);
    }

}