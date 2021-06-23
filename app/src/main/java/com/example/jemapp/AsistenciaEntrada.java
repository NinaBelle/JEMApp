package com.example.jemapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.net.URI;
import java.util.ArrayList;

public class AsistenciaEntrada extends AppCompatActivity implements View.OnClickListener  {
    private TextView dia, titulo, laber;
    private Button escaner;
    private RecyclerView rvListado;
    private String codigo;
    direccionIP dddIP;
    String direIP;
    private ArrayList<ItemRV> listaarreglo = new ArrayList<ItemRV>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asistencia_entrada);

        titulo = (TextView)findViewById(R.id.txtTutulo);
        dia = (TextView)findViewById(R.id.txtMismo);
        laber = (TextView)findViewById(R.id.txtLabel);
        escaner = (Button)findViewById(R.id.btnEscaner);
        rvListado= (RecyclerView)findViewById(R.id.rvDatos);
        dddIP = new direccionIP();
        direIP=dddIP.getIP();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvListado.setLayoutManager(layoutManager);
        //final ArrayList<ItemRV> listaarreglo = new ArrayList<ItemRV>();

        Bundle recibe = getIntent().getExtras();
        String nomb = recibe.getString("nombTaller");
        String nombdia = recibe.getString("dia");
        String label1 = recibe.getString("etiqueta1");
        final String codHora = recibe.getString("codHora");
        String real = recibe.getString("real");

        titulo.setText(nomb);
        dia.setText(nombdia);
        codigo=codHora;

        obtenerFecha(real,codHora);
        laber.setText(label1);
        final JSONArray na = new JSONArray();

        escaner.setOnClickListener(this);

        //RequestQueue envios = Volley.newRequestQueue(this);
        String dire = direIP+"buscarMarca.php?id="+codHora+"";

        StringRequest detalles= new StringRequest(Request.Method.GET, dire, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                response = response.replace("][",",");
                if (response.length()>0){
                    try {
                        JSONArray ja = new JSONArray(response);
                        Log.i("existeMarca", response);
                        CargarRecy(ja,codHora);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }else {
                    Log.i("Marca", "nada en la marca");
                    CargarRecy(na,codHora);// igual tiene q cargar algo el recycler
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("EEError12", "de Volley");

            }
        });
        //envios.add(detalles);
        MySingleton.getInstance(this).addToRequestQueue(detalles);
    }
    private void CargarRecy(final JSONArray ja, String codHora) {
        //direIP=dddIP.getIP();
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url =direIP+"buscarAlumnos.php?id="+codHora+"";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //textView.setText("Response: " + response.toString());
                        Log.i("resulbusq", response.toString());

                        ItemRV unAlumno= null;
                        JSONArray json=response.optJSONArray("alumnos");

                        for (int i=0; i<json.length(); i++){
                            int ban=0;
                            unAlumno=new ItemRV();
                            JSONObject obj=null;
                            try {
                                obj=json.getJSONObject(i);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            unAlumno.setNomreI(obj.optString("Apellido")+" "+obj.optString("Nombre"));
                            unAlumno.setDniI(obj.optString("DNI"));
                            //unAlumno.setImageI(R.drawable.eliminar);
                            Log.i("carga", ""+ja.length());
                            if (ja.length()>0){
                                Log.i("Entra", " "+i);
                                try {
                                    for (int ind = 0; ind < ja.length(); ind += 2) {
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
                                            // }
                                        }
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (ban==0){
                                unAlumno.setImageI(R.drawable.eliminar);
                                Log.i("ban"+i, ""+ban);
                            }else{
                                unAlumno.setImageI(R.drawable.verificacion);
                                Log.i("ban"+i, ""+ban);
                            }
                            listaarreglo.add(unAlumno);

                        }
                        AdaptadorRVSalida nuevo = new AdaptadorRVSalida(listaarreglo);
                        rvListado.setAdapter(nuevo);

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //el error
                        rvListado.setVisibility(View.INVISIBLE);
                    }
                });
        //requestQueue.add(jsonObjectRequest);
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
    private String obtenerFecha(final String real, String codHora) {
        final String[] fecha = new String[1];
        // direIP=dddIP.getIP();

        //RequestQueue requestQueue = Volley.newRequestQueue(this);

        String url2 =direIP+"control.php?id="+codHora+"&real="+real;
        StringRequest busq = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("LAFECHAES", response);
                /*String val = ""+response;
                String hora = val.substring(13,21);
                fecha[0] = response.substring(13,21);
                Log.i("LOQDEBERIADEVOLVER", hora);
                int hora1,min1,hora2,min2;
                String h1,h2,m1,m2;
               // if (fecha[0] > "19:20:00")*/

                if (response.contains("1")){
                    escaner.setVisibility(View.VISIBLE);
                }else{
                    escaner.setVisibility(View.INVISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Toast.makeText(getApplicationContext(), "Se produjo un error: "+error.toString(), LENGTH_LONG).show();
                Log.i("EEEError de la fecha", error.toString());

            }
        });
        //requestQueue.add(busq);
        MySingleton.getInstance(this).addToRequestQueue(busq);
        return fecha[0];
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnEscaner:
                Intent inten= new Intent(this, EscanerEntradaActivity.class);
                inten.putExtra("codHora", codigo);
                startActivity(inten);
                break;

        }

    }

}
