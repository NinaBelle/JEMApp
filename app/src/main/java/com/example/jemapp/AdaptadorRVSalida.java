package com.example.jemapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AdaptadorRVSalida extends RecyclerView.Adapter<AdaptadorRVSalida.AlumnoViewHolder>  {
    List<ItemRV> listaAlumnos;

    public AdaptadorRVSalida(List<ItemRV> listaAlumnos) {
        this.listaAlumnos = listaAlumnos;
    }

    @NonNull
    @Override
    public AlumnoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itemrecyclerv,viewGroup,false);
        AlumnoViewHolder lineaView = new AlumnoViewHolder(v);

        return lineaView;
    }

    @Override
    public void onBindViewHolder(@NonNull AlumnoViewHolder alumnoViewHolder, int i) {

        alumnoViewHolder.nombreI.setText(listaAlumnos.get(i).getNomreI().toString());
        alumnoViewHolder.DNII.setText(listaAlumnos.get(i).getDniI().toString());
        alumnoViewHolder.imageI.setImageResource(listaAlumnos.get(i).getImageI());
    }

    @Override
    public int getItemCount() {
        return listaAlumnos.size();
    }

    public static class AlumnoViewHolder extends RecyclerView.ViewHolder {
        TextView nombreI, DNII;
        ImageView imageI;

        public AlumnoViewHolder(@NonNull View itemView) {
            super(itemView);

            nombreI=(TextView) itemView.findViewById(R.id.txtNombre);
            DNII=(TextView)itemView.findViewById(R.id.txtdni);
            imageI=(ImageView)itemView.findViewById(R.id.imageI);
        }
    }
}
