package com.example.jemapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;



public class Asistencia2Fragment extends Fragment {

    private static final String ARG_PARAM1 = "codHora";
    private static final String ARG_PARAM2 = "nombTaller";
    private static final String ARG_PARAM3 = "dia";
    private static final String ARG_PARAM4 = "real";

    TextView codigo, dia;
    ListView listaDatos;
    ArrayList<Datos> lista;


    private String codHora;
    private String dato;
    private String datodia;
    private String real;

   // private OnFragmentInteractionListener mListener;

    public Asistencia2Fragment() {
        // Required empty public constructor
    }


    public static Asistencia2Fragment newInstance(String param1, String param2) {
        Asistencia2Fragment fragment = new Asistencia2Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            codHora = getArguments().getString(ARG_PARAM1);
            dato = getArguments().getString(ARG_PARAM2);
            datodia = getArguments().getString(ARG_PARAM3);
            real = getArguments().getString(ARG_PARAM4);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_asistencia2, container, false);
        codigo = (TextView)view.findViewById(R.id.txtCodigo);
        dia = (TextView)view.findViewById(R.id.txtDia);
        listaDatos = (ListView)view.findViewById(R.id.lvAcciones) ;
        lista = new ArrayList<Datos>();

        codigo.setText(dato);
        dia.setText(datodia);

        lista.add(new Datos(1,"Asistencia Entrada", R.drawable.derecha));
        lista.add(new Datos(2, "Asistencia Salida", R.drawable.izquierda));
        lista.add(new Datos(3, "Detalle Listado Alumnos", R.drawable.multiedic));

        Adaptador miadaptador = new Adaptador(getActivity().getApplicationContext(), lista);
        listaDatos.setAdapter(miadaptador);//el list view punto el adaptador hecho por mi

        listaDatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            String label;
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0){
                    label = "ENTRADA: Lista de Asistentes";
                    Intent intento= new Intent(getActivity(), AsistenciaEntrada.class);//Main3Activity
                    intento.putExtra("codHora",codHora);
                    intento.putExtra("nombTaller", dato);
                    intento.putExtra("dia", datodia);
                    intento.putExtra("real", real);
                    intento.putExtra("etiqueta1", label);
                    startActivity(intento);
                }else {
                    if (i==1){
                        label = "SALIDA: Lista de Asistentes";
                        Intent intento= new Intent(getActivity(), AsistenciaSalida.class);//AsistenciaSalida
                        intento.putExtra("codHora",codHora);
                        intento.putExtra("nombTaller", dato);
                        intento.putExtra("dia", datodia);
                        intento.putExtra("real", real);
                        intento.putExtra("etiqueta1", label);
                        startActivity(intento);
                    }else{
                        if (i==2){
                            label = "Detalle de Asistencia del Día";
                            Intent intento= new Intent(getActivity(), DetalleDia.class);
                            intento.putExtra("codHora",codHora);
                            intento.putExtra("nombTaller", dato);
                            intento.putExtra("dia", datodia);
                            intento.putExtra("real", real);
                            intento.putExtra("etiqueta1", label);
                            startActivity(intento);
                        }
                    }
                }

            }
        });
        // Inflate the layout for this fragment
        return view;
    }


}
