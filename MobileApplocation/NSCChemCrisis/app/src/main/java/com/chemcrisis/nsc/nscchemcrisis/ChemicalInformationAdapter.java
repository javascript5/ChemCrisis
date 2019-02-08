package com.chemcrisis.nsc.nscchemcrisis;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ChemicalInformationAdapter extends RecyclerView.Adapter<ChemicalInformationAdapter.ViewHolder> {
    private ArrayList<Chemical> chemicals;
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView chemicalThaiName;
        public CardView cardView;
        public ViewHolder(View view) {
            super(view);
            chemicalThaiName = (TextView) view.findViewById(R.id.thaiName);
            cardView = (CardView) view.findViewById(R.id.chemical_information_cardView);
        }
    }
    public ChemicalInformationAdapter(Context context, ArrayList<Chemical> dataset) {
        chemicals = dataset;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.chemical_information_adapter, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Chemical chemical = chemicals.get(i);
        viewHolder.chemicalThaiName.setText(chemical.getChemicalThaiName());
        String violenceOfChemica = chemical.getViolenceOfChemical();

        switch(violenceOfChemica)
        {
            case "สูง":
                viewHolder.cardView.setCardBackgroundColor(Color.parseColor("#FC402A"));
                break;
            case "ปานกลาง":
                viewHolder.cardView.setCardBackgroundColor(Color.parseColor("#FC8B2A"));
                break;
            case "ต่ำ":
                viewHolder.cardView.setCardBackgroundColor(Color.parseColor("#F8B500"));
                break;
            default:
                System.out.println("no match");
        }
    }


    @Override
    public int getItemCount() {
        return chemicals.size();
    }
}
