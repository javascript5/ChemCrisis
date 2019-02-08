package com.chemcrisis.nsc.nscchemcrisis;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;

public class AccidentInformationFragment extends Fragment {
    private  ArrayList<Chemical> chemicals = new ArrayList<>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private String[] chemicalsName;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DatabaseReference databaseReference = database.getReference("accident/KMITL/accidentChemical/");
        databaseReference.addValueEventListener(new ValueEventListener() {
         @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             String chemicalStr = dataSnapshot.getValue().toString();
             chemicalsName = chemicalStr.split(",");

             for(final String chemical : chemicalsName) {
                 DatabaseReference databaseReference = database.getReference("chemical/"+chemical.trim()+"");

                 databaseReference.addValueEventListener(new ValueEventListener() {

                     @Override
                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                         chemicals.add(dataSnapshot.getValue(Chemical.class));
                         RecyclerView chemicalList = getView().findViewById(R.id.chemical_information_list_view);

                         RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                         chemicalList.setLayoutManager(mLayoutManager);

                         ChemicalInformationAdapter mAdapter = new ChemicalInformationAdapter(getContext(),chemicals);
                         chemicalList.setAdapter(mAdapter);

                         ArrayList<String> effectsOfChemical = new ArrayList<>();
                        //  split comma "a,b,c" > ["a","b","c"]
                         effectsOfChemical = splitEffects(chemicals);

                         ListView effectsListView = getView().findViewById(R.id.effects_of_chemical_list_view);
                         EffectOfChemicalAdapter effectOfChemicalAdapter = new EffectOfChemicalAdapter(getContext(),R.layout.accident_information_fragment,effectsOfChemical);
                         effectsListView.setAdapter(effectOfChemicalAdapter);

                     }

                     @Override
                     public void onCancelled(@NonNull DatabaseError databaseError) {

                     }
                 });
             }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }


    private ArrayList<String> splitEffects(ArrayList<Chemical> chemicals){
        ArrayList<String> effectsOfChemical = new ArrayList<>();
        String[] effects;
        for(Chemical chemical : chemicals){
            effects = chemical.getEffectsOfChemical().split(",");
            for(String effectOfChemical : effects){

                if (!effectsOfChemical.contains(effectOfChemical)) {
                    effectsOfChemical.add(effectOfChemical);
                }
            }
        }
        return effectsOfChemical;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.accident_information_fragment, container, false);
    }
}
