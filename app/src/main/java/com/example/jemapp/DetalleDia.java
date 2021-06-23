package com.example.jemapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetalleDia extends AppCompatActivity {
    private TextView taller, dia, etiqueta;
    private RecyclerView rvDetalleD;
    private String codigo,direIP;
    direccionIP URL;

    private ArrayList<ItemRVDD> listadoDet = new ArrayList<ItemRVDD>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_dia);

        taller=(TextView)findViewById(R.id.txtNombTal);
        dia=(TextView)findViewById(R.id.txtFecha);
        etiqueta=(TextView)findViewById(R.id.txtLabel9);
        rvDetalleD=(RecyclerView)findViewById(R.id.rvInfoDia);
        URL=new direccionIP();
        direIP=URL.getIP();

        LinearLayoutManager loyoutMan = new LinearLayoutManager(this);
        rvDetalleD.setLayoutManager(loyoutMan);

        Bundle recibe = getIntent().getExtras();
        String nomb = recibe.getString("nombTaller");
        String nombdia = recibe.getString("dia");
        String label1 = recibe.getString("etiqueta1");
        final String codHora = recibe.getString("codHora");
        String real = recibe.getString("real");

        taller.setText(nomb);
        dia.setText(nombdia);
        etiqueta.setText(label1);
        codigo=codHora;
        final JSONArray na = new JSONArray();

        //RequestQueue enviosDD = Volley.newRequestQueue(this);
        String dire = direIP+"buscarMarcaTot.php?id="+codHora+"";

        StringRequest deta= new StringRequest(Request.Method.GET, dire, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                response = response.replace("][",",");
                if (response.length()>0){
                    try {
                        JSONArray ja = new JSONArray(response);
                        Log.i("existeMarca", response);
                        CargarRecyDD(ja,codHora);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }else {
                    Log.i("Marca", "nada en la marca");
                    CargarRecyDD(na,codHora);// igual tiene q cargar algo el recycler
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("EEError12", "de Volley");

            }
        });
        //enviosDD.add(deta);
        MySingleton.getInstance(this).addToRequestQueue(deta);
    }

    private void CargarRecyDD(final JSONArray ja, String codHora) {
        //direIP=URL.getIP();
        final String[] hE = new String[1];
        final String[] hS = new String[1];
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url =direIP+"buscarAlumnos.php?id="+codHora+"";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //textView.setText("Response: " + response.toString());
                        Log.i("resultadoDD", response.toString());

                        ItemRVDD unAlumno= null;
                        JSONArray json=response.optJSONArray("alumnos");

                        for (int i=0; i<json.length(); i++){
                            int ban=0;
                            unAlumno=new ItemRVDD();
                            JSONObject obj=null;
                            try {
                                obj=json.getJSONObject(i);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            unAlumno.setNombDD(obj.optString("Apellido")+" "+obj.optString("Nombre"));
                            unAlumno.setDniDD(obj.optString("DNI"));
                            //unAlumno.setImageI(R.drawable.eliminar);
                            Log.i("carga", ""+ja.length());
                            if (ja.length()>0){
                                Log.i("Entra", " "+i);
                                try {
                                    for (int ind = 0; ind < ja.length(); ind += 3) {
                                        Log.i("array", " "+ja.getString(ind));
                                        Log.i("objeto", " "+obj.opt("Cod_Pers"));
                                        String var1=ja.getString(ind);
                                        String var2=obj.optString("Cod_Pers");
                                        if (var1.equals(var2)){
                                            //if (ja.getString(ind) == obj.optString("Cod_Pers")) {

                                            // if (ja.getString(ind+1) == "SI") {
                                            // unAlumno.setImageI(R.drawable.verificacion);
                                            ban=1;
                                            Log.i("Bandera", "Cambio");
                                            hE[0] =ja.getString(ind+1);
                                            hS[0]=ja.getString(ind+2);
                                            Log.i("HoraE",""+hE[0]);
                                            Log.i("HoraS",""+hS[0]);
                                            // }
                                        }
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (ban==1){
                                // if (hE[0]== "00:00:00"){hE[0]="--";}
                                //if (hS[0]== "00:00:00"){hS[0]="--";}
                                unAlumno.setHoraEDD(hE[0]);
                                unAlumno.setHoraSDD(hS[0]);
                                Log.i("ban"+i, ""+ban);
                            }
                            listadoDet.add(unAlumno);

                        }
                        AdaptadorRVDD nuevo = new AdaptadorRVDD(listadoDet);
                        rvDetalleD.setAdapter(nuevo);

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //el error
                        rvDetalleD.setVisibility(View.INVISIBLE);
                    }
                });
        //requestQueue.add(jsonObjectRequest);
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}
