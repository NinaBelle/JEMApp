package com.example.jemapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;


public class MiAsistenciaFragment extends Fragment {
    direccionIP URL;
    String direIP;

    TextView titulo, nombreComp, dniComp, diauno, diados, diatres, HE1, HS1, HE2, HS2, HE3, HS3;
    TextView etiq1,etiq2,etiq3,etiq4,etiq5,etiq6;

    private static final String ARG_PARAM1 = "iduser";
    private static final String ARG_PARAM2 = "idtaller";
    private static final String ARG_PARAM3 = "nombTaller";

    private String idusr;
    private String idtaller;//, idPers;
    private String nombTaller;

    public MiAsistenciaFragment() {
        // Required empty public constructor
    }


    public static MiAsistenciaFragment newInstance(String param1, String param2) {
        MiAsistenciaFragment fragment = new MiAsistenciaFragment();
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
            idusr = getArguments().getString(ARG_PARAM1);
            idtaller = getArguments().getString(ARG_PARAM2);
            nombTaller = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mi_asistencia, container, false);
                URL = new direccionIP();
        direIP=URL.getIP();
        titulo = (TextView)view.findViewById(R.id.txtTitComp);
        nombreComp = (TextView)view.findViewById(R.id.txtNombComp);
        dniComp= (TextView)view.findViewById(R.id.txtDniComp);
        diauno= (TextView)view.findViewById(R.id.txtdiauno);
        diados= (TextView)view.findViewById(R.id.txtdiados);
        diatres= (TextView)view.findViewById(R.id.txtdiatres);
        HE1= (TextView)view.findViewById(R.id.txtHE1);
        HE2= (TextView)view.findViewById(R.id.txtHE2);
        HE3= (TextView)view.findViewById(R.id.txtHE3);
        HS1= (TextView)view.findViewById(R.id.txtHS1);
        HS2= (TextView)view.findViewById(R.id.txtHS2);
        HS3= (TextView)view.findViewById(R.id.txtHS3);

        etiq1 = (TextView)view.findViewById(R.id.textView10);
        etiq2 = (TextView)view.findViewById(R.id.textView12);
        etiq3 = (TextView)view.findViewById(R.id.textView15);
        etiq4 = (TextView)view.findViewById(R.id.textView17);
        etiq5 = (TextView)view.findViewById(R.id.textView20);
        etiq6 = (TextView)view.findViewById(R.id.textView22);

        titulo.setText(nombTaller);

        String url = direIP+"consultaDatPerscIDusr.php?id="+idusr+"";

        StringRequest busqDDAT = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        response = response.replace("][",",");
                        if (response.length()>0){
                            Log.i("CargaDatREs",response);
                            try {
                                JSONArray ja = new JSONArray(response);
                                String nombre = ja.getString(1)+" "+ja.getString(0);
                                String dni = ja.getString(2);
                                //idPers = ja.getString(3);
                                nombreComp.setText(nombre);
                                dniComp.setText(dni);

                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("ErrorCargaDatREs", " "+error);

            }
        });

        //cargaDat.add(busqDDAT);
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(busqDDAT);
        CargarDias(idusr,idtaller);
        // Inflate the layout for this fragment
        limpiar(idtaller);
        return view;
    }


    private void CargarDias(String codUsr, String codTaller) {
        Log.i("CargaDatFech","entra"+codUsr+"-"+codTaller);
        //RequestQueue cargaDatF = Volley.newRequestQueue(this);
        String url = direIP+"consultaFechAsistcIDUser.php?codU="+codUsr+"&codT="+codTaller+"";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        response = response.replace("][",",");
                        if (response.length()>0){
                            Log.i("CargaDatFechResp",response);

                            JSONArray ja = null;
                            try {
                                ja = new JSONArray(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.i("LargoResponse", " "+ja.length());
                            if (ja.length()==3) {
                                try {
                                    //JSONArray ja = new JSONArray(response);
                                    String auxvar = ja.getString(0);
                                    String dato1 = auxvar.substring(8,10);
                                    String dato2 = auxvar.substring(5,7);
                                    String dato3 = auxvar.substring(0,4);
                                    if(dato1.equals("29")) {
                                        diauno.setText(dato1 + "/" + dato2 + "/" + dato3);
                                        if (ja.getString(1).equals("00:00:00")) {
                                            HE1.setText("--");
                                        } else {
                                            HE1.setText(ja.getString(1));
                                        }
                                        if (ja.getString(2).equals("00:00:00")) {
                                            HS1.setText("--");
                                        } else {
                                            HS1.setText(ja.getString(2));
                                        }
                                        HE2.setText("Ausente");
                                        HS2.setText("Ausente");
                                        HE3.setText("Ausente");
                                        HS3.setText("Ausente");
                                    }else if (dato1.equals("30")){
                                        diados.setText(dato1 + "/" + dato2 + "/" + dato3);
                                        if (ja.getString(1).equals("00:00:00")) {
                                            HE2.setText("--");
                                        } else {
                                            HE2.setText(ja.getString(1));
                                        }
                                        if (ja.getString(2).equals("00:00:00")) {
                                            HS2.setText("--");
                                        } else {
                                            HS2.setText(ja.getString(2));
                                        }
                                        HE1.setText("Ausente");
                                        HS1.setText("Ausente");
                                        HE3.setText("Ausente");
                                        HS3.setText("Ausente");
                                    }else if (dato1.equals("31")){
                                        diatres.setText(dato1 + "/" + dato2 + "/" + dato3);
                                        if (ja.getString(1).equals("00:00:00")) {
                                            HE3.setText("--");
                                        } else {
                                            HE3.setText(ja.getString(1));
                                        }
                                        if (ja.getString(2).equals("00:00:00")) {
                                            HS3.setText("--");
                                        } else {
                                            HS3.setText(ja.getString(2));
                                        }
                                        HE2.setText("Ausente");
                                        HS2.setText("Ausente");
                                        HE1.setText("Ausente");
                                        HS1.setText("Ausente");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (ja.length()==6){
                                try {
                                    // JSONArray ja = new JSONArray(response);
                                    String auxvar = ja.getString(0);
                                    String dato1 = auxvar.substring(8,10);
                                    String dato2 = auxvar.substring(5,7);
                                    String dato3 = auxvar.substring(0,4);
                                    if (dato1.equals("29")) {
                                        diauno.setText(dato1 + "/" + dato2 + "/" + dato3);
                                        if (ja.getString(1).equals("00:00:00")) {
                                            HE1.setText("--");
                                        } else {
                                            HE1.setText(ja.getString(1));
                                        }
                                        if (ja.getString(2).equals("00:00:00")) {
                                            HS1.setText("--");
                                        } else {
                                            HS1.setText(ja.getString(2));
                                        }
                                        HE2.setText("Ausente");
                                        HS2.setText("Ausente");
                                    }else if (dato1.equals("30")){
                                        diados.setText(dato1 + "/" + dato2 + "/" + dato3);
                                        if (ja.getString(1).equals("00:00:00")) {
                                            HE2.setText("--");
                                        } else {
                                            HE2.setText(ja.getString(1));
                                        }
                                        if (ja.getString(2).equals("00:00:00")) {
                                            HS2.setText("--");
                                        } else {
                                            HS2.setText(ja.getString(2));
                                        }
                                        HE1.setText("Ausente");
                                        HS1.setText("Ausente");
                                    }
                                    String auxvar2 = ja.getString(3);
                                    String dato12 = auxvar2.substring(8,10);
                                    String dato22 = auxvar2.substring(5,7);
                                    String dato32 = auxvar2.substring(0,4);
                                    if (dato12.equals("30")) {
                                        diados.setText(dato12 + "/" + dato22 + "/" + dato32);
                                        if (ja.getString(4).equals("00:00:00")) {
                                            HE2.setText("--");
                                        } else {
                                            HE2.setText(ja.getString(4));
                                        }
                                        if (ja.getString(5).equals("00:00:00")) {
                                            HS2.setText("--");
                                        } else {
                                            HS2.setText(ja.getString(5));
                                        }
                                        HE3.setText("Ausente");
                                        HS3.setText("Ausente");
                                    }else if (dato12.equals("31")){
                                        diatres.setText(dato12 + "/" + dato22 + "/" + dato32);
                                        if (ja.getString(4).equals("00:00:00")) {
                                            HE3.setText("--");
                                        } else {
                                            HE3.setText(ja.getString(4));
                                        }
                                        if (ja.getString(5).equals("00:00:00")) {
                                            HS3.setText("--");
                                        } else {
                                            HS3.setText(ja.getString(5));
                                        }

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (ja.length()==9){
                                try {
                                    // JSONArray ja = new JSONArray(response);
                                    String auxvar = ja.getString(0);
                                    String dato1 = auxvar.substring(8,10);
                                    String dato2 = auxvar.substring(5,7);
                                    String dato3 = auxvar.substring(0,4);
                                    diauno.setText(dato1+"/"+dato2+"/"+dato3);
                                    if (ja.getString(1).equals("00:00:00")){
                                        HE1.setText("--");
                                    }else {
                                        HE1.setText(ja.getString(1));
                                    }
                                    if (ja.getString(2).equals("00:00:00")){
                                        HS1.setText("--");
                                    }else {
                                        HS1.setText(ja.getString(2));
                                    }
                                    String auxvar2 = ja.getString(3);
                                    String dato12 = auxvar2.substring(8,10);
                                    String dato22 = auxvar2.substring(5,7);
                                    String dato32 = auxvar2.substring(0,4);
                                    diados.setText(dato12+"/"+dato22+"/"+dato32);
                                    if (ja.getString(4).equals("00:00:00")){
                                        HE2.setText("--");
                                    }else {
                                        HE2.setText(ja.getString(4));
                                    }
                                    if (ja.getString(5).equals("00:00:00")) {
                                        HS2.setText("--");
                                    }else{
                                        HS2.setText(ja.getString(5));
                                    }
                                    String auxvar3 = ja.getString(6);
                                    String dato13 = auxvar3.substring(8,10);
                                    String dato23 = auxvar3.substring(5,7);
                                    String dato33 = auxvar3.substring(0,4);
                                    diatres.setText(dato13+"/"+dato23+"/"+dato33);
                                    if (ja.getString(7).equals("00:00:00")){
                                        HE3.setText("--");
                                    }else {
                                        HE3.setText(ja.getString(7));
                                    }
                                    if (ja.getString(8).equals("00:00:00")){
                                        HS3.setText("--");
                                    }else {
                                        HS3.setText(ja.getString(8));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        }else{
                            HE1.setText("Ausente");
                            HS1.setText("Ausente");
                            HE2.setText("Ausente");
                            HS2.setText("Ausente");
                            HE3.setText("Ausente");
                            HS3.setText("Ausente");
                        }
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("ErrorCargaDAtF", " "+error);

            }
        });
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);
        //cargaDatF.add(stringRequest);
    }

    private void limpiar(String idtaller) {
        String direc = direIP+"consultaTipo.php?id="+idtaller+"";

        StringRequest busqTipo = new StringRequest(Request.Method.GET, direc,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        response = response.replace("][",",");
                        if (response.contains("Mesa")){
                            Log.i("Tipo",response);
                            try {
                                JSONArray ja = new JSONArray(response);
                                String fecha = ja.getString(0);
                                String dat1 = fecha.substring(8,10);
                                //String dat2 = fecha.substring(5,7);
                                //String dat3 = fecha.substring(0,4);
                                if (dat1.equals("30")){
                                    diauno.setVisibility(View.INVISIBLE);
                                    diatres.setVisibility(View.INVISIBLE);
                                    etiq1.setVisibility(View.INVISIBLE);
                                    etiq2.setVisibility(View.INVISIBLE);
                                    etiq5.setVisibility(View.INVISIBLE);
                                    etiq6.setVisibility(View.INVISIBLE);
                                    HE1.setVisibility(View.INVISIBLE);
                                    HS1.setVisibility(View.INVISIBLE);
                                    HE3.setVisibility(View.INVISIBLE);
                                    HS3.setVisibility(View.INVISIBLE);
                                }else if (dat1.equals("31")){
                                    diados.setVisibility(View.INVISIBLE);
                                    diauno.setVisibility(View.INVISIBLE);
                                    etiq3.setVisibility(View.INVISIBLE);
                                    etiq4.setVisibility(View.INVISIBLE);
                                    etiq2.setVisibility(View.INVISIBLE);
                                    etiq1.setVisibility(View.INVISIBLE);
                                    HE2.setVisibility(View.INVISIBLE);
                                    HS2.setVisibility(View.INVISIBLE);
                                    HE1.setVisibility(View.INVISIBLE);
                                    HS1.setVisibility(View.INVISIBLE);
                                }

                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("ErrorCargaDatREs", " "+error);

            }
        });

        //cargaDat.add(busqDDAT);
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(busqTipo);
    }
}
