package com.example.jemapp;

import android.app.Activity;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class EscanerEntradaActivity extends Activity implements ZBarScannerView.ResultHandler {
    private ZBarScannerView mScannerView;
    direccionIP URL;
    String direIP;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZBarScannerView(this);    // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();// Stop camera on pause
        //Intent viaje= new Intent(this, Main3Activity.class);
        //startActivity(viaje);
        finish();
    }

    @Override
    public void handleResult(Result rawResult) {
        URL= new direccionIP();
        direIP=URL.getIP();
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        }catch (Exception e){
            Log.e("Error de sonido", e.getLocalizedMessage());
        }
        Log.v("El resultado", rawResult.getContents()); // Para ver el resultado en el Log
        Log.v("Formato codigo", rawResult.getBarcodeFormat().getName()); // Muestra el tipo de formato del codigo

        String linea1 = "J+E+M+19";
        String mensaje;
        mensaje=rawResult.getContents();
        if (mensaje.contains(linea1)) {
            final String[] fecha= new String[1];
            final String auxcp;
            int initit;
            int fintit;
            initit = mensaje.indexOf("q3-"); //codigo persona
            fintit = mensaje.indexOf("-q3");
            initit = initit+3;
            auxcp = mensaje.substring(initit,fintit);

            Bundle recibo = getIntent().getExtras();    ///
            final String codigoH = recibo.getString("codHora");

            //RequestQueue requestQ = Volley.newRequestQueue(this);
            String url =direIP+"buscarFecha.php";
            StringRequest busqF= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("LA FECHA ES", response);
                    fecha[0] = response;
                    Log.i("LO Q DEBERIA DEVOLVER",fecha[0]);
                    String date = fecha[0];
                    Log.i("EEE-La fecha es ", date);
                    correlacion(auxcp, date, codigoH);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("Error de la fecha", error.toString());
                }
            });
            //requestQ.add(busqF);
            MySingleton.getInstance(this).addToRequestQueue(busqF);
        }else {
            Toast.makeText(this, "Codigo QR NO es Valido", Toast.LENGTH_SHORT).show();
        }


        mScannerView.resumeCameraPreview(this);
    }

    private void correlacion(final String auxcp, final String date, final String codigoH) {
        String url= direIP+"correlacion.php?acp="+auxcp+"&codh="+codigoH;

        StringRequest buscCorr= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("1")) {
                    //Toast.makeText(getApplicationContext(), "ASISITENCIA INGRESADA", Toast.LENGTH_SHORT).show();
                    endGuardar(auxcp, date, codigoH);
                }else if(response.contains("0")){
                    Toast.makeText(getApplicationContext(), "TALLER O MESA INCORRECTO", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ErrordeGuardado", error.toString());
            }
        });
        //hilo.add(insertar);
        MySingleton.getInstance(this).addToRequestQueue(buscCorr);

    }

    private void endGuardar(String auxcp, String date, String codigoH) {

        //direIP=URL.getIP();
        String newdate = date.substring(13,21);
        //RequestQueue hilo = Volley.newRequestQueue(this);
        String url =direIP+"registroAsistencia.php?acp="+auxcp+"&hrs="+newdate+"&codh="+codigoH;
        StringRequest insertar= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "ASISITENCIA INGRESADA", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error de Guardado", error.toString());
            }
        });
        //hilo.add(insertar);
        MySingleton.getInstance(this).addToRequestQueue(insertar);
    }
}
