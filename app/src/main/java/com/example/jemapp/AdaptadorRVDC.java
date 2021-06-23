package com.example.jemapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class AdaptadorRVDC extends BaseAdapter {
    Context contexto;
    List<ItemRVDC> listaalumcomp;

    public AdaptadorRVDC(Context contexto, List<ItemRVDC> listaalumcomp) {
        this.contexto = contexto;
        this.listaalumcomp = listaalumcomp;
    }

    @Override
    public int getCount() {
        return listaalumcomp.size();
    }

    @Override
    public Object getItem(int i) {
        return listaalumcomp.get(i);
    }

    @Override
    public long getItemId(int i) {
        return listaalumcomp.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vista = view;
        LayoutInflater inflado = LayoutInflater.from(contexto);
        vista = inflado.inflate(R.layout.itemrvdetallediacomp,null);

        TextView nombreC = (TextView)vista.findViewById(R.id.txtNombAlum);
        TextView dniC = (TextView)vista.findViewById(R.id.txtdniDDC);
        TextView codP = (TextView)vista.findViewById(R.id.txtCodP);

        //accion.setText(listaObjetos.get(i).getAccion().toString());
        nombreC.setText(listaalumcomp.get(i).getNombDC());
        dniC.setText(listaalumcomp.get(i).getDniDC());
        codP.setText(listaalumcomp.get(i).getCodP());

        return vista;
    }
}
