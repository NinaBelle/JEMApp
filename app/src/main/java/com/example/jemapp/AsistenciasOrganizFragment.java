package com.example.jemapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class AsistenciasOrganizFragment extends Fragment {
    ListView listaResultado;
    String iduser, direccionIP,tipouser, opcion;
    ArrayList<DatosTaller> listaTalleres = new ArrayList<>();
    direccionIP URL;

    public AsistenciasOrganizFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_asistencias_organiz, container, false);
        // Inflate the layout for this fragment
        iduser = getArguments().getString("iduser");
        tipouser = getArguments().getString("tipouser");
        opcion = getArguments().getString("opcion");
        //Toast.makeText(getActivity(),"Id Usuario: "+iduser,Toast.LENGTH_SHORT).show();

        URL = new direccionIP();
        direccionIP = URL.getIP();
        listaResultado = (ListView)view.findViewById(R.id.lvlista);

        //RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String url =direccionIP+"talleres.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,

                new Response.Listener<String>() {
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
        //requestQueue.add(stringRequest);
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        return view;


    }

    private void CargarListView(JSONArray ja) {

        ArrayList<String> lista = new ArrayList<>();

        for (int i=0; i<ja.length(); i+=4){
            try {
                DatosTaller datosTaller=new DatosTaller();
                datosTaller.setCodigo(Integer.parseInt(ja.getString(i)));
                datosTaller.setNombre(ja.getString(i+1));
                datosTaller.setCupo(Integer.parseInt(ja.getString(i+2)));
                datosTaller.setTurno(ja.getString(i+3));
                listaTalleres.add(datosTaller);

                lista.add(ja.getString(i+1)+" \n Turno: "+ja.getString(i+3));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, lista);
        listaResultado.setAdapter(adaptador);
        listaResultado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Bundle bundle = new Bundle();
                bundle.putString("iduser", iduser);
                bundle.putString("tipouser", tipouser);
                bundle.putString("codTaller",""+listaTalleres.get(i).getCodigo());
                bundle.putString("nombTaller",""+listaTalleres.get(i).getNombre());
                bundle.putString("turno", listaTalleres.get(i).getTurno());
                if (opcion.equals("asistencias")) {
                    AsistenciaDCFragment myFrag = new AsistenciaDCFragment();
                    myFrag.setArguments(bundle);
                    FragmentManager fm = getFragmentManager();
                    fm.beginTransaction().replace(R.id.escenarioOrganizador, myFrag).addToBackStack(null).commit();
                }else if (opcion.equals("talleres")){
                    VistaTalleresFragment myFrag = new VistaTalleresFragment();
                    myFrag.setArguments(bundle);
                    FragmentManager fm = getFragmentManager();
                    fm.beginTransaction().replace(R.id.escenarioOrganizador, myFrag).addToBackStack(null).commit();
                }
            }
        });
    }
}
