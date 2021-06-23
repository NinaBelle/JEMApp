package com.example.jemapp;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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


public class VistaTalleresFragment extends Fragment  implements View.OnClickListener{

    private String iduser, idtaller, turno, direccionIP;
    private direccionIP URL;
    private TextView txtNombre, txtDescripcion, txtHorarios, txtCupo;
    private JSONArray ja;
    private Button BTNCupo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vista_talleres, container, false);
        // Inflate the layout for this fragment
        //bundle.putString("iduser", iduser);
        //bundle.putString("tipouser", tipouser);
        //bundle.putString("codTaller",""+listaTalleres.get(i).getCodigo());
        //bundle.putString("nombTaller",""+listaTalleres.get(i).getNombre());
        //bundle.putString("turno", listaTalleres.get(i).getTurno());

        iduser = getArguments().getString("iduser");
        idtaller = getArguments().getString("codTaller");
        turno = getArguments().getString("turno");
        //Toast.makeText(getActivity(),"turno: "+turno,Toast.LENGTH_SHORT).show();
        URL=new direccionIP();
        direccionIP=URL.getIP();

        txtNombre = (TextView) view.findViewById(R.id.nombreTaller);
        txtDescripcion = (TextView) view.findViewById(R.id.descripcionTaller);
        txtHorarios = (TextView) view.findViewById(R.id.textHorarios);
        txtCupo = (TextView) view.findViewById(R.id.textCupo);
        BTNCupo = (Button) view.findViewById(R.id.btnCupo);
        BTNCupo.setOnClickListener(this);

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
                    txtCupo.setText(txtCupo.getText()+cupo+"\n");

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
            case R.id.btnCupo:
                cambiarCupo();
        }

    }

    public void cambiarCupo()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        View v_iew=inflater.inflate(R.layout.modificar_cupo_dialog, null) ;
        builder.setView(v_iew);
        final EditText uCupo = v_iew.findViewById(R.id.editCupo);
        uCupo.setText(txtCupo.getText().toString().substring(6,8));
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Toast.makeText(getActivity(),"Cambiar Cupo",Toast.LENGTH_LONG).show();
                String nvoCupo=uCupo.getText().toString();
                try
                {
                    // the String to int conversion happens here
                    int nro = Integer.parseInt(nvoCupo);
                    if(nro<=0)
                    {
                        mostrarDialogo("Ingrese un número válido");
                    }
                    else
                    {
                        String url3=direccionIP+"actualizarCupo.php?idtaller="+idtaller+"&nvocupo="+nro;
                        updateCupo(url3);
                    }

                }
                catch (NumberFormatException nfe)
                {
                    mostrarDialogo("Ingrese un número válido");
                }

               /* if(nvoCupo.equals("")||Integer.parseInt(nvoCupo)<=0)
                {
                    //mostrarMensaje();
                    //Toast.makeText(getApplicationContext(),"Faltan Completar Datos",Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(),"Ingrese un numero valido",Toast.LENGTH_SHORT).show();
                }
                else{
                    String url3=direccionIP+"actualizarCupo.php?idtaller="+idtaller+"&nvocupo="+nvoCupo;
                    updateCupo(url3);
                }*/
            }
        });
        builder.setNegativeButton("CANCELAR",null);
        builder.create();
        builder.show();
    }

    public void updateCupo(String URL)
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
                        Toast.makeText(getActivity(),"Se actualizo el cupo correctamente",Toast.LENGTH_SHORT).show();
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.detach(VistaTalleresFragment.this).attach(VistaTalleresFragment.this).commit();
                        //mostrarDialogo("Te has dado de baja al curso");
                    }
                    else {
                        //Toast.makeText(getApplicationContext(),"No se pudieron actualizar los datos",Toast.LENGTH_LONG).show();
                        Toast.makeText(getActivity(),"error",Toast.LENGTH_SHORT).show();
                        //mostrarDialogo("No puedes inscribirte al curso por que ya no hay cupo");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_LONG).show();
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
       // builder.setPositiveButton("OK",null);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cambiarCupo();
            }
        });

        builder.create();
        builder.show();
    }

}