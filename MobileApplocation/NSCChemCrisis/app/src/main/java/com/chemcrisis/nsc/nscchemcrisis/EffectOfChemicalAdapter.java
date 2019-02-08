package com.chemcrisis.nsc.nscchemcrisis;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class EffectOfChemicalAdapter extends ArrayAdapter<String> {
    private Context context;
    private ArrayList<String> effect;

    public EffectOfChemicalAdapter(@NonNull Context context, int resource, ArrayList<String> effect) {
        super(context, resource, effect);
        this.context = context;
        this.effect = effect;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View effectItem = LayoutInflater.from(context).inflate(
                R.layout.effect_of_chemical_adapter,
                parent,
                false
        );
        TextView chemicalThaiName = (TextView)effectItem.findViewById(R.id.chemicalThaiName);
        chemicalThaiName.setText(effect.get(position));
        return effectItem;
    }
}
