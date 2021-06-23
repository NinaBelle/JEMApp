package com.example.jemapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class AdaptadorAsistF extends BaseAdapter {
    Context contexto;
    List<DatosAsistF> listaObjetos;

    public AdaptadorAsistF(Context contexto, List<DatosAsistF> listaObjetos) {
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
        vista = inflado.inflate(R.layout.itemlistviewprinc,null);

        TextView opcAF = (TextView)vista.findViewById(R.id.txtopPrinc);

        opcAF.setText(listaObjetos.get(i).getOpciones());

        return vista;
    }
}
