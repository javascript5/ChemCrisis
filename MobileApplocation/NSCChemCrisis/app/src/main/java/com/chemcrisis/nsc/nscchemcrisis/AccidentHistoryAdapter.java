package com.chemcrisis.nsc.nscchemcrisis;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class AccidentHistoryAdapter extends ArrayAdapter<History> {
    private Context context;
    private ArrayList<History> histories;

    public AccidentHistoryAdapter(@NonNull Context context, int resource, ArrayList<History> histories) {
        super(context, resource, histories);
        this.context = context;
        this.histories = histories;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View historyItem = LayoutInflater.from(context).inflate(
                R.layout.history_adapter,
                parent,
                false
        );
        TextView dateTimeHistory = (TextView)historyItem.findViewById(R.id.date_time_history_adapter);
        TextView effectiveStackHeight = (TextView)historyItem.findViewById(R.id.effectiveStackHeight_history_adapter);
        TextView massEmissionRate = (TextView)historyItem.findViewById(R.id.massEmissionRate_history_adapter);
        TextView chemical = (TextView)historyItem.findViewById(R.id.chemical_history_adapter);

        TextView factory = (TextView)historyItem.findViewById(R.id.factory_history_adapter);

        dateTimeHistory.setText(histories.get(position).getDateTime());
        effectiveStackHeight.setText(histories.get(position).getEffectiveStackHeight()+" m");
        massEmissionRate.setText(histories.get(position).getMassEmissionRate()+" g/s");
        chemical.setText(histories.get(position).getAccidentChemical());
        factory.setText("โรงงาน "+histories.get(position).getFactoryId());

        return historyItem;
    }
}

