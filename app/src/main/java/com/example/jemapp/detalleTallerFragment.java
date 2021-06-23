package com.example.jemapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class detalleTallerFragment extends Fragment implements View.OnClickListener {

    private String iduser, idtaller, turno, direccionIP;
    private direccionIP URL;
    private TextView txtNombre, txtDescripcion, txtHorarios;
    private JSONArray ja;
    private Button BTNinscribirse;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       //return inflater.inflate(R.layout.fragment_perfil, container, false);
        View view = inflater.inflate(R.layout.fragment_detalle_taller, container, false);
        iduser = getArguments().getString("iduser");
        idtaller = getArguments().getString("idtaller");
        turno = getArguments().getString("turno");
        //Toast.makeText(getActivity(),"turno: "+turno,Toast.LENGTH_SHORT).show();
        URL=new direccionIP();
        direccionIP=URL.getIP();

        txtNombre = (TextView) view.findViewById(R.id.nombreTaller);
        txtDescripcion = (TextView) view.findViewById(R.id.descripcionTaller);
        txtHorarios = (TextView) view.findViewById(R.id.textHorarios);
        BTNinscribirse = (Button) view.findViewById(R.id.inscribirseOK);
        BTNinscribirse.setOnClickListener(this);

        requestDetalle(direccionIP+"detalletaller.php?idtaller="+idtaller.toString());
        requestHorarios(direccionIP+"horariosTaller.php?idtaller="+idtaller.toString());

        return view;
    }

    private void requestDetalle(String URL) {

        Log.i("url",""+URL);

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest =  new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    ja = new JSONArray(response);
                    String nombre = ja.getString(0);
                    String descripcion = ja.getString(1);
                    txtNombre.setText(nombre+"\n");
                    txtDescripcion.setText(descripcion+"\n");

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),"error "+iduser,Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(stringRequest);

    }

    private void requestHorarios(String URL) {

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        response = response.replace("][",",");
                        //Toast.makeText(getActivity(),"horarios "+response,Toast.LENGTH_SHORT).show();
                        if (response.length()>0){
                            try {
                                JSONArray ja = new JSONArray(response);
                                Log.i("largoarray", " "+ja.length());
                                cargarHorario(ja);
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else {
                            Log.i("EEError", "de que no devolvio nada la consulta");
                            //listaResultado.setVisibility(View.INVISIBLE);
                        }
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //listaResultado.setVisibility(View.INVISIBLE);
                Log.i("EEError11", "de Volley");
            }
        });
        requestQueue.add(stringRequest);

    }

    private void cargarHorario(JSONArray ja) {
        ArrayList<String> lista = new ArrayList<>();
        for (int i = 0; i < ja.length(); i += 3) {
            try {
                String fecha=ja.getString(i);
                String anio=fecha.substring(0,4);
                String mes=fecha.substring(5,7);
                String dia=fecha.substring(8,10);
                String horaInicio=ja.getString(i+1);
                String horaFin=ja.getString(i+2);
                lista.add(dia+"/"+mes+"/"+anio+" de "+horaInicio.substring(0,5)+ " a "+horaFin.substring(0,5)+" horas");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String cadena="";
        for (int i = 0; i < lista.size(); i++)
        {
           cadena=cadena+lista.get(i)+"\n";
        }

        txtHorarios.setText(cadena);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.inscribirseOK:
                inscripcionOK(direccionIP+"inscripcion.php?iduser="+iduser+"&idtaller="+idtaller+"&turno="+turno);
        }

    }

    public void inscripcionOK(String URL)
    {
        Log.i("url",""+URL);

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest =  new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    Object json = new JSONObject(response);
                    //Toast.makeText(getApplicationContext(),"json:" +((JSONObject) json).get("code").toString(),Toast.LENGTH_LONG).show();
                    if(((JSONObject) json).get("code").equals(0))
                    {
                        //Toast.makeText(getApplicationContext(),"Te has inscripto al curso",Toast.LENGTH_LONG).show();
                        mostrarDialogo("Te has inscripto al curso");

                    }
                    else {
                        if(((JSONObject) json).get("code").equals(1))
                        {
                            //Toast.makeText(getApplicationContext(),"Ya estas inscripto en este curso",Toast.LENGTH_LONG).show();
                            mostrarDialogo("Ya estas inscripto a un curso en este turno");
                        }
                        else
                        {
                            if(((JSONObject) json).get("code").equals(3))
                            {
                                //Toast.makeText(getApplicationContext(),"Ya estas inscripto en este curso",Toast.LENGTH_LONG).show();
                                mostrarDialogo("El periodo de inscripcion ya finalizo");
                            }
                            else
                            {
                                //Toast.makeText(getApplicationContext(),"No puedes inscribirte al curso xq ya no hay cupo",Toast.LENGTH_LONG).show();
                                mostrarDialogo("No puedes inscribirte al curso por que ya no hay cupo");
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),"error",Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(stringRequest);
    }

    public void mostrarDialogo(String mensaje)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Importante");
        builder.setMessage(mensaje);
        builder.setPositiveButton("OK",null);
        builder.create();
        builder.show();
    }

}
