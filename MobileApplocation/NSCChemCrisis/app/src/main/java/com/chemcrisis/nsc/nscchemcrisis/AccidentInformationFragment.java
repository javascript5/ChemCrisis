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

import java.util.ArrayList;

public class AccidentInformationFragment extends Fragment {
    private  ArrayList<Chemical> chemicals;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        chemicals = new ArrayList<>();
        chemicals.add(new Chemical("Sodium Chloride 1",
                "โซเดียมคลอไรด์ 1",
                "วิงเวียนศรีษะ, ปวดหัว, ระคายเคือง",
                "58.44",
                "ต่ำ"));
        chemicals.add(new Chemical("Sodium Chloride 2",
                "โซเดียมคลอไรด์ 2",
                "วิงเวียนศรีษะ, ปวดหัว, ระคายเคือง",
                "58.44",
                "ปานกลาง"));
        chemicals.add(new Chemical("Sodium Chloride 3",
                "โซเดียมคลอไรด์ 3",
                "วิงเวียนศรีษะ, ปวดหัว, ระคายเคือง",
                "58.44",
                "สูง"));

        RecyclerView chemicalList = getView().findViewById(R.id.chemical_information_list_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        chemicalList.setLayoutManager(mLayoutManager);

        ChemicalInformationAdapter mAdapter = new ChemicalInformationAdapter(getContext(),chemicals);
        chemicalList.setAdapter(mAdapter);


        ArrayList<String> effectsOfChemical = new ArrayList<>();
//        split comma "a,b,c" > ["a","b","c"]
        effectsOfChemical = splitEffects(chemicals);


        ListView effectsListView = getView().findViewById(R.id.effects_of_chemical_list_view);
        EffectOfChemicalAdapter effectOfChemicalAdapter = new EffectOfChemicalAdapter(getContext(),R.layout.accident_information_fragment,effectsOfChemical);
        effectsListView.setAdapter(effectOfChemicalAdapter);



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
