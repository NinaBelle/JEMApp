package com.example.jemapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class AsistenciaDCFragment extends Fragment {

    private static final String ARG_PARAM1 = "iduser";
    private static final String ARG_PARAM2 = "codTaller";
    private static final String ARG_PARAM3 = "nombTaller";
    private static final String ARG_PARAM4 = "codHora";
    private static final String ARG_PARAM5 = "dia";
    private static final String ARG_PARAM6 = "real";

    private direccionIP URL;
    private String iduser;
    private String codTaller;
    private String nombTaller, direIP;
    private String codHora;
    private String dia;
    private String real;

    private TextView informacion;
    private ListView listadocomp;
    //direccionIP URL;
    //String direIP;
    private ArrayList<ItemRVDC> lista = new ArrayList<ItemRVDC>();

    //private OnFragmentInteractionListener mListener;

    public AsistenciaDCFragment() {
        // Required empty public constructor
    }


    public static AsistenciaDCFragment newInstance(String param1, String param2) {
        AsistenciaDCFragment fragment = new AsistenciaDCFragment();
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
            iduser = getArguments().getString(ARG_PARAM1);
            codTaller = getArguments().getString(ARG_PARAM2);
            nombTaller = getArguments().getString(ARG_PARAM3);
            codHora = getArguments().getString(ARG_PARAM4);
            dia = getArguments().getString(ARG_PARAM5);
            real = getArguments().getString(ARG_PARAM6);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_asistencia_dc, container, false);
        informacion = (TextView)view.findViewById(R.id.txtNTal);
        listadocomp = (ListView)view.findViewById(R.id.InfoDiaComp);
        URL=new direccionIP();
        direIP=URL.getIP();
        // Inflate the layout for this fragment
        informacion.setText(nombTaller);

        //RequestQueue busqDC = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url =direIP+"buscarAluDC.php?id="+codTaller+"";
        JsonObjectRequest jsonOR = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("resultadoDC", response.toString());

                //ItemRVDC unAlumno= null;
                JSONArray json=response.optJSONArray("alumnos");

                for (int i=0; i<json.length(); i++) {

                    // unAlumno = new ItemRVDC();
                    JSONObject obj = null;
                    try {
                        obj = json.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // unAlumno.setNombDC(obj.optString("Apellido")+" "+obj.optString("Nombre"));
                    // unAlumno.setDniDC(obj.optString("DNI"));
                    //listadoDet.add(unAlumno);
                    // lista.add(new Datos(1,"Asistencia Entrada", R.drawable.derecha));
                    String nombre=obj.optString("Apellido")+" "+obj.optString("Nombre");
                    String dni=obj.optString("DNI");
                    String codigo=obj.optString("Cod_Pers");
                    lista.add(new ItemRVDC(i+1, nombre, dni, codigo));
                }
                AdaptadorRVDC miadapt = new AdaptadorRVDC(getActivity().getApplicationContext(), lista);
                listadocomp.setAdapter(miadapt);

                //listaDatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                listadocomp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        ItemRVDC plan= lista.get(i);
                        String codPersona= plan.getCodP();
                        Intent intentoC= new Intent(getActivity(), DetalleAsistAlumn.class);
                        intentoC.putExtra("codP", codPersona);
                        intentoC.putExtra("codTaller", codTaller);
                        intentoC.putExtra("nombTaller", nombTaller);
                        startActivity(intentoC);
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("resultadoDC", ""+error);
                listadocomp.setVisibility(View.INVISIBLE);
            }
        });
        //busqDC.add(jsonOR);
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonOR);
        return view;
    }


}
