package com.example.jemapp;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class AdaptadorRv extends RecyclerView.Adapter<AdaptadorRv.LineaView> {

    List<Acreditados> listaAcred;

    public AdaptadorRv(List<Acreditados> listaAcred) {
        this.listaAcred = listaAcred;
    }

    @NonNull
    @Override
    public AdaptadorRv.LineaView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.vistareview,viewGroup,false);
        LineaView lineaView = new LineaView(v);

        return lineaView;
    }

    @Override
    public void onBindViewHolder(@NonNull LineaView lineaView, int i) {

        lineaView.anomb.setText(listaAcred.get(i).getNombre());
        lineaView.adni.setText(listaAcred.get(i).getDni());
        lineaView.aestac.setText(listaAcred.get(i).getAcredestado());

    }

    @Override
    public int getItemCount() {
        return listaAcred.size();
    }

    public static class LineaView extends RecyclerView.ViewHolder {
        TextView anomb, adni, aestac;

        public LineaView(@NonNull View itemView) {
            super(itemView);

            anomb = (TextView)itemView.findViewById(R.id.nombre);
            adni = (TextView)itemView.findViewById(R.id.dni);
            aestac = (TextView)itemView.findViewById(R.id.est_acred);
        }
    }
}
