package com.example.jemapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class VistaAcredAsistFragment extends Fragment {
    TextView estado, fechEst;
    String direIP, iduser, tipouser;
    direccionIP URL;
    private ListView listaCertif;
    private ArrayList<DatosCertif> lista = new ArrayList<DatosCertif>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vista_acred_asist, container, false);

        estado = (TextView)view.findViewById(R.id.txtest);
        fechEst = (TextView)view.findViewById(R.id.fechyhs);
        listaCertif = (ListView)view.findViewById(R.id.lvCertifi);

        URL = new direccionIP();
        direIP=URL.getIP();
        iduser = getArguments().getString("iduser");
        tipouser = getArguments().getString("tipouser");

        FijarCertif(iduser);

        String dire = direIP+"consultaAcreditacion.php?id="+iduser+"";
        StringRequest consAcred = new StringRequest(Request.Method.GET, dire, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (response.length()>0){
                        try {
                            JSONArray ja = new JSONArray(response);
                            Log.i("EncuentraAcreditacion", " "+ja.length());
                            String estAcred = ja.getString(0);
                            String fechAcred = ja.getString(1);
                            String dato1 = fechAcred.substring(8,10);
                            String dato2 = fechAcred.substring(5,7);
                            String dato3 = fechAcred.substring(0,4);
                            String hs = fechAcred.substring(11,19);
                            if (estAcred.equals("SI")){
                                estado.setText("Acreditado");
                            }
                            fechEst.setText("Fecha/Hs: "+dato1+"/"+dato2+"/"+dato3+" "+hs);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Erroralbuscar:", " "+error);
            }
        });
        //buscarFoto.add(bufot);
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(consAcred);
        // Inflate the layout for this fragment
        return view;
    }

    private void FijarCertif(String iduser) {
        String dire = direIP+"vistacertificados.php?id="+iduser+"";
        JsonObjectRequest jsonOR = new JsonObjectRequest(Request.Method.GET, dire, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //DatosAsistF unDato= null;
                JSONArray json=response.optJSONArray("certifi");

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
                    String nombre=obj.optString("NombTall");
                    String estT=obj.optString("Est_Taller");
                    String estC=obj.optString("Est_Certif");
                    lista.add(new DatosCertif(i+1, nombre, estT, estC));
                }
                AdaptadorCertif miadapt = new AdaptadorCertif(getActivity().getApplicationContext(), lista);
                listaCertif.setAdapter(miadapt);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Erroralbuscar:", " "+error);
            }
        });
        //buscarFoto.add(bufot);
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonOR);
    }

}
