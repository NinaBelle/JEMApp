package com.example.jemapp;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.util.ArrayList;


public class detalleMiCursoFragment extends Fragment implements View.OnClickListener {


    private String iduser, tipouser, idtaller, turno, direccionIP;
    private direccionIP URL;
    private TextView txtNombre, txtDescripcion, txtHorarios;
    private JSONArray ja;
    private Button BTNbaja, BTNasistencias;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detalle_mi_curso, container, false);

        URL=new direccionIP();
        direccionIP=URL.getIP();
        iduser = getArguments().getString("iduser");
        tipouser = getArguments().getString("tipouser");
        idtaller = getArguments().getString("idtaller");
        //turno = getArguments().getString("turno");

        txtNombre = (TextView) view.findViewById(R.id.nombreTaller);
        txtDescripcion = (TextView) view.findViewById(R.id.descripcionTaller);
        txtHorarios = (TextView) view.findViewById(R.id.textHorarios);
        BTNbaja = (Button) view.findViewById(R.id.bajaOK);
        BTNbaja.setOnClickListener(this);
        BTNasistencias = (Button) view.findViewById(R.id.verAsistencia);
        BTNasistencias.setOnClickListener(this);

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
                    String cupo = ja.getString(2);
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
            case R.id.bajaOK:
                mostrarDialogo("Confirma que quiere darse de baja a este curso?");
                //bajaOK(direccionIP+"baja.php?iduser="+iduser+"&idtaller="+idtaller);
                break;
            case R.id.verAsistencia:
                Bundle bundle = new Bundle();
                bundle.putString("iduser", iduser);
                bundle.putString("tipouser", tipouser);
                bundle.putString("idtaller",idtaller);
                bundle.putString("nombTaller", txtNombre.getText().toString());
                MiAsistenciaFragment myFrag = new MiAsistenciaFragment();
                myFrag.setArguments(bundle);
                FragmentManager fm = getFragmentManager();
                if (tipouser.equals("5")) {
                    fm.beginTransaction().replace(R.id.escenarioColaborador, myFrag).addToBackStack(null).commit();
                }else if (tipouser.equals("6")){
                    fm.beginTransaction().replace(R.id.escenarioAsistente, myFrag).addToBackStack(null).commit();
                }
                //mostrarDialogo("Confirma que quiere darse de baja a este curso?");
                //break;
        }

    }

    public void mostrarDialogo(String mensaje)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Importante");
        builder.setMessage(mensaje);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                bajaOK(direccionIP+"bajaOK.php?iduser="+iduser+"&idtaller="+idtaller);
                // Crea el nuevo fragmento y la transacción.
               /* Bundle bundle = new Bundle();
                bundle.putString("iduser", iduser);
                bundle.putString("tipouser",tipouser);
                misCursosFragment myFrag = new misCursosFragment();
                myFrag.setArguments(bundle);
                if(iduser.equals("2"))
                {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.escenarioColaborador, myFrag );
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                else{
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.escenarioAsistente, myFrag );
                    transaction.addToBackStack(null);
                    transaction.commit();
                }*/
                // Commit a la transacción

            }
        });
        builder.setNegativeButton("Cancelar",null);
        builder.create();
        builder.show();
    }

    public void bajaOK(String URL)
    {
        Log.i("url",""+URL);

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest =  new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    Object json = new JSONObject(response);
                    //Toast.makeText(getApplicationContext(),"json:" +((JSONObject) json).get("code").toString(),Toast.LENGTH_LONG).show();
                    if(((JSONObject) json).get("code").equals(0))
                    {
                        //Toast.makeText(getApplicationContext(),,Toast.LENGTH_LONG).show();
                        Toast.makeText(getActivity(),"Te has dado de baja al curso",Toast.LENGTH_SHORT).show();
                        //mostrarDialogo("Te has dado de baja al curso");
                        BTNbaja.setVisibility(View.INVISIBLE);
                        BTNasistencias.setVisibility(View.INVISIBLE);
                    }
                    else {
                        Toast.makeText(getActivity(),"Ya no puedes darte de baja",Toast.LENGTH_SHORT).show();
                        //mostrarDialogo("No puedes inscribirte al curso por que ya no hay cupo");
                        BTNbaja.setVisibility(View.INVISIBLE);
                        BTNasistencias.setVisibility(View.INVISIBLE);
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

        requestQueue.add(stringRequest);
    }

}
