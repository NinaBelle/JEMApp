package com.example.jemapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class AsistenciaFragment extends Fragment {
    direccionIP URL;
    String iduser, direIP;
    TextView tit;
    ListView listaResultado;
    ArrayList<DatosAsistF> lista;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_asistencia, container, false);
        iduser = getArguments().getString("iduser");
        // Inflate the layout for this fragment
        URL=new direccionIP();
        direIP=URL.getIP();

        lista = new ArrayList<DatosAsistF>();
        listaResultado = (ListView)view.findViewById(R.id.lvDias);
        tit = (TextView)view.findViewById(R.id.txtNombTaller);


        //RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url =direIP+"consultaFechas.php?id="+iduser+"";

        StringRequest busqueda = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                response = response.replace("][",",");
                if (response.length()>0){
                    try {
                        JSONArray ja = new JSONArray(response);
                        Log.i("largoarray", " "+ja.length());
                        CargarListView(ja);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }else {
                    Log.i("EEError", "de que no devolvio nada la consulta");
                    listaResultado.setVisibility(View.INVISIBLE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listaResultado.setVisibility(View.INVISIBLE);
                Log.i("EEError11", "de Volley");

            }
        });
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(busqueda);
        //requestQueue.add(busqueda);
        return view;
    }

    private void CargarListView(JSONArray ja) {
        try {
            String titulo = ja.getString(2);
            tit.setText(titulo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //ArrayList<String> lista = new ArrayList<>();
        int car = 0;
        final String[] codHora = new String[4];
        final String [] nombT = new String[1];
        final String [] codtall = new String[1];
        final String [] etiq = new String[4];
        final String [] real = new String[4];
        try {
            nombT[0]= ja.getString(2);
            codtall[0]= ja.getString(3);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String auxvar;
        String dato1;
        String dato2;
        String dato3;
        int auxcar;

        for (int i=0; i<ja.length(); i+=4){

            try {
                codHora[car] = ja.getString(i);
                real[car]= ja.getString(i+1);
                auxcar = car;
                auxvar = ja.getString(i+1);
                dato1 = auxvar.substring(8,10);
                dato2 = auxvar.substring(5,7);
                dato3 = auxvar.substring(0,4);
                car = car+1;
                etiq[auxcar]= car+"° Día:  "+dato1+"/"+dato2+"/"+dato3; ////NO SE si esto se puede hacer

                String valor= car+"° Día:  "+dato1+"/"+dato2+"/"+dato3+"=> Tomar Asistencia";///ja.getString(i+1)
                lista.add( new DatosAsistF(i, valor));
                Log.i("indiceDetC", " "+i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.i("indice", " "+ja.length());
        //lista.add("Detalle Completo de Asistencia");
        lista.add(new DatosAsistF(3, "Detalle Completo de Asistencia"));

        // Adaptador miadaptador = new Adaptador(getActivity().getApplicationContext(), lista);
        AdaptadorAsistF miadaptador = new AdaptadorAsistF(getActivity().getApplicationContext(), lista);
       // ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, lista);
        listaResultado.setAdapter(miadaptador);

        listaResultado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DatosAsistF indice = lista.get(i);
                int indDF= indice.getId();
                Bundle bundle = new Bundle();
                bundle.putString("iduser", iduser);
                bundle.putString("codTaller", codtall[0]);
                bundle.putString("nombTaller",nombT[0]);
                bundle.putString("codHora", codHora[i]);
                bundle.putString("dia", etiq[i]);
                bundle.putString("real", real[i]);
                if (indDF==3){
                    /*Intent intento = new Intent(getActivity().getApplicationContext(), AsistenciaDCFragment.class);
                    intento.putExtra("codTaller", codtall);
                    intento.putExtra("nombTaller", nombT[0]);
                    startActivity(intento);*/
                    AsistenciaDCFragment myFrag = new AsistenciaDCFragment();
                    myFrag.setArguments(bundle);
                    FragmentManager fm = getFragmentManager();
                    fm.beginTransaction().replace(R.id.escenarioColaborador,myFrag).addToBackStack(null).commit();
                }else {
                    /*Intent intento = new Intent(getActivity().getApplicationContext(), Asistencia2Fragment.class);
                    intento.putExtra("codHora", codHora[i]);
                    intento.putExtra("nombTaller", nombT[0]);
                    intento.putExtra("dia", etiq[i]);
                    intento.putExtra("real", real[i]);
                    startActivity(intento);*/
                    Asistencia2Fragment myFrag = new Asistencia2Fragment();
                    myFrag.setArguments(bundle);
                    FragmentManager fm = getFragmentManager();
                    fm.beginTransaction().replace(R.id.escenarioColaborador,myFrag).addToBackStack(null).commit();
                }
            }
        });
    }

}
