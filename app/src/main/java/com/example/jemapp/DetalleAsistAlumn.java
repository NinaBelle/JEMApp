package com.example.jemapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

public class DetalleAsistAlumn extends AppCompatActivity {
    direccionIP URL;
    String direIP;
    ImageView foto;
    TextView titulo, nombreComp, dniComp, diauno, diados, diatres, HE1, HS1, HE2, HS2, HE3, HS3;
    TextView etiq1,etiq2,etiq3,etiq4,etiq5,etiq6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_asist_alumn);
        URL = new direccionIP();
        direIP=URL.getIP();
        titulo = (TextView)findViewById(R.id.txtTitComp);
        nombreComp = (TextView)findViewById(R.id.txtNombComp);
        dniComp= (TextView)findViewById(R.id.txtDniComp);
        diauno= (TextView)findViewById(R.id.txtdiauno);
        diados= (TextView)findViewById(R.id.txtdiados);
        diatres= (TextView)findViewById(R.id.txtdiatres);
        HE1= (TextView)findViewById(R.id.txtHE1);
        HE2= (TextView)findViewById(R.id.txtHE2);
        HE3= (TextView)findViewById(R.id.txtHE3);
        HS1= (TextView)findViewById(R.id.txtHS1);
        HS2= (TextView)findViewById(R.id.txtHS2);
        HS3= (TextView)findViewById(R.id.txtHS3);
        foto= (ImageView)findViewById(R.id.fotoComp);

        etiq1 = (TextView)findViewById(R.id.textView10);
        etiq2 = (TextView)findViewById(R.id.textView12);
        etiq3 = (TextView)findViewById(R.id.textView15);
        etiq4 = (TextView)findViewById(R.id.textView17);
        etiq5 = (TextView)findViewById(R.id.textView20);
        etiq6 = (TextView)findViewById(R.id.textView22);

        Bundle recibe = getIntent().getExtras();
        String codPers = recibe.getString("codP");
        String codTaller = recibe.getString("codTaller");
        String nombTaller = recibe.getString("nombTaller");

        titulo.setText(nombTaller);

        String dire = direIP+"consultaNombFoto.php?id="+codPers+"";
        StringRequest bufot = new StringRequest(Request.Method.GET, dire, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (response.length()>0){
                        try {
                            JSONArray ja = new JSONArray(response);
                            Log.i("EncuentraNombre", " "+ja.length());
                            SetearImagen(ja);
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
                Log.i("Erroralbuscar:", "NombFoto"+error);
            }
        });
        //buscarFoto.add(bufot);
        MySingleton.getInstance(this).addToRequestQueue(bufot);

        cargarDatos(codPers,codTaller);
        limpiar(codTaller);
    }

    private void SetearImagen(JSONArray ja) {
        String muestra="";
        try {
            muestra= ja.getString(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //RequestQueue requestQueue = Volley.newRequestQueue(this);

        String imgURL = direIP+"imgPers/"+muestra+".JPG";
        // Initialize a new ImageRequest
        ImageRequest imageRequest = new ImageRequest(
                imgURL, // Image URL
                new Response.Listener<Bitmap>() { // Bitmap listener
                    @Override
                    public void onResponse(Bitmap response) {
                        // Do something with response
                        Log.i("DeberiaCargar", "Foto");
                        foto.setImageBitmap(response);
                    }
                },
                0, // Image width
                0, // Image height
                ImageView.ScaleType.CENTER_CROP, // Image scale type
                Bitmap.Config.RGB_565, //Image decode configuration
                new Response.ErrorListener() { // Error listener
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something with error response
                        error.printStackTrace();
                        Log.i("ErrorCargaImagen", "En SetearImagen");
                    }
                }
        );
        // Add ImageRequest to the RequestQueue
        //requestQueue.add(imageRequest);
        MySingleton.getInstance(this).addToRequestQueue(imageRequest);
    }

    private void cargarDatos(String codPers, String codTaller) {
        Log.i("CargaDat","entra");
        //RequestQueue cargaDat = Volley.newRequestQueue(this);
        String url = direIP+"consultaDatPers.php?id="+codPers+"";

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
        MySingleton.getInstance(this).addToRequestQueue(busqDDAT);
        CargarDias(codPers,codTaller);
    }

    private void CargarDias(String codPers, String codTaller) {
        Log.i("CargaDatFech","entra");
        //RequestQueue cargaDatF = Volley.newRequestQueue(this);
        String url = direIP+"consultaFechAsist.php?codP="+codPers+"&codT="+codTaller+"";

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
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
        //cargaDatF.add(stringRequest);
    }

    private void limpiar(String codTaller) {
        String direc = direIP+"consultaTipo.php?id="+codTaller+"";

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
                                    etiq1.setVisibility(View.INVISIBLE);
                                    etiq2.setVisibility(View.INVISIBLE);
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
        MySingleton.getInstance(this).addToRequestQueue(busqTipo);
    }
}
