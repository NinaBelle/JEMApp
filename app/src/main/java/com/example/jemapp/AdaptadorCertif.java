package com.example.jemapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class AdaptadorCertif extends BaseAdapter {
    Context contexto;
    List<DatosCertif> listaObjetos;

    public AdaptadorCertif(Context contexto, List<DatosCertif> listaObjetos) {
        this.contexto = contexto;
        this.listaObjetos = listaObjetos;
    }

    @Override
    public int getCount() {
        return listaObjetos.size();
    }

    @Override
    public Object getItem(int i) {
        return listaObjetos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return listaObjetos.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vista = view;
        LayoutInflater inflado = LayoutInflater.from(contexto);
        vista = inflado.inflate(R.layout.itemlvcertif,null);

        TextView nombT = (TextView)vista.findViewById(R.id.txNomTall);
        TextView estT = (TextView)vista.findViewById(R.id.txEstTall);
        TextView estC = (TextView)vista.findViewById(R.id.txEstCertif);

        nombT.setText(listaObjetos.get(i).getNombT());
        estT.setText(listaObjetos.get(i).getEstT());
        estC.setText(listaObjetos.get(i).getEstCertif());

        return vista;
    }
}
