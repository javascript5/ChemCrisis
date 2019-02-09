package com.chemcrisis.nsc.nscchemcrisis;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gu.toolargetool.TooLargeTool;

import java.util.ArrayList;
import java.util.Collections;

public class AccidentHistoryFragment extends Fragment {

    private ArrayList<History> histories = new ArrayList<>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private ListView listView;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView = (ListView) getView().findViewById(R.id.historyListview);
        DatabaseReference databaseReference = database.getReference("history");
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                histories.clear();
                for(DataSnapshot dataSnapshotObj : dataSnapshot.getChildren())
                    histories.add(dataSnapshotObj.getValue(History.class));

                Collections.reverse(histories);

                AccidentHistoryAdapter accidentHistoryAdapter = new AccidentHistoryAdapter(getContext(),R.layout.history_adapter, histories);

                listView.setAdapter(accidentHistoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.accident_history_fragment, container, false);
    }

}
