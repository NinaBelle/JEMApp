package com.example.jemapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class Adaptador extends BaseAdapter {
    Context contexto;
    List<Datos> listaObjetos;

    public Adaptador(Context contexto, List<Datos> listaObjetos) {
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
        vista = inflado.inflate(R.layout.itemslistview,null);

        ImageView imagen = (ImageView) vista.findViewById(R.id.imgIcon);
        TextView accion = (TextView)vista.findViewById(R.id.txtAccion);

        accion.setText(listaObjetos.get(i).getAccion().toString());
        imagen.setImageResource(listaObjetos.get(i).getIcono());

        return vista;
    }
}
