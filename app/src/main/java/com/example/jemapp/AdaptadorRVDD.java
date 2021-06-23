package com.example.jemapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class AdaptadorRVDD extends RecyclerView.Adapter<AdaptadorRVDD.DetalleDiaViewHolder> {
    List<ItemRVDD> listadetalledia;

    public AdaptadorRVDD(List<ItemRVDD> listadetalledia) {
        this.listadetalledia = listadetalledia;
    }

    @NonNull
    @Override
    public DetalleDiaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itemrvdetalledia,viewGroup,false);
        DetalleDiaViewHolder lineaView = new DetalleDiaViewHolder(v);

        return lineaView;
    }

    @Override
    public void onBindViewHolder(@NonNull DetalleDiaViewHolder detalleDiaViewHolder, int i) {

        detalleDiaViewHolder.nomb.setText(listadetalledia.get(i).getNombDD().toString());
        detalleDiaViewHolder.dni.setText(listadetalledia.get(i).getDniDD().toString());
        detalleDiaViewHolder.horae.setText(listadetalledia.get(i).getHoraEDD());
        detalleDiaViewHolder.horas.setText(listadetalledia.get(i).getHoraSDD());

    }

    @Override
    public int getItemCount() {
        return listadetalledia.size();
    }

    public static class DetalleDiaViewHolder extends RecyclerView.ViewHolder {

        TextView nomb, dni, horae, horas;

        public DetalleDiaViewHolder(@NonNull View itemView) {
            super(itemView);

            nomb = (TextView)itemView.findViewById(R.id.NombDetD);
            dni = (TextView)itemView.findViewById(R.id.DniDetD);
            horae = (TextView)itemView.findViewById(R.id.txtHE);
            horas= (TextView)itemView.findViewById(R.id.txtHS);

        }
    }
}
