package com.example.jemapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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


public class escanerAcredFragment extends Fragment {
    private TextView txtnom;
    private TextView txtdni;
    private ImageView foto;
    //private Button btnCancel;
    private Button btnAceptar;
    private TextView txt7;
    private direccionIP URL;
    private String direIP, iduser, tipouser, nombPers, dniPers, codPers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_escaner_acred, container, false);

        txtnom = (TextView)view.findViewById(R.id.txtNombreA);
        txtdni = (TextView)view.findViewById(R.id.txtDniA);
        foto = (ImageView)view.findViewById(R.id.imgFotoA);
        btnAceptar = (Button)view.findViewById(R.id.button2);
        //btnCancel = (Button)view.findViewById(R.id.btnCancelar);
        txt7 = (TextView)view.findViewById(R.id.textView7);
        //mifrag= new inscripcionFragment();
        URL=new direccionIP();
        direIP=URL.getIP();

        iduser = getArguments().getString("iduser");
        tipouser= getArguments().getString("tipouser");
        nombPers = getArguments().getString("nombPers");
        dniPers= getArguments().getString("dniPers");
        codPers = getArguments().getString("codPers");


        this.txtnom.setText(nombPers);
        this.txtdni.setText(dniPers);
        cargarImagen(codPers);

        /*btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft= getFragmentManager().beginTransaction();
                ft.detach(escanerAcredFragment.this).commit();
            }
        });*/

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarDatos(codPers);

            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    private void cargarImagen(String auxcp) {

        String dire = direIP+"consultaNombFoto.php?id="+auxcp+"";
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
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(bufot);
    }

    private void SetearImagen(JSONArray ja) {
        String muestra="";
        try {
            muestra= ja.getString(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

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
        requestQueue.add(imageRequest);

    }


    private void guardarDatos(final String auxcp) {

        final String[] fecha = {" "};

        //RequestQueue requestQueue = Volley.newRequestQueue(this);

        String url2 =direIP+"buscarFecha.php";
        StringRequest busq = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("LA FECHA ES", response);
                fecha[0] = response;
                Log.i("LO Q DEBERIA DEVOLVER",fecha[0]);
                String date = fecha[0];
                Log.i("EEE-La fecha es ", date);
                terminarGuardar(auxcp, date);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Toast.makeText(getApplicationContext(), "Se produjo un error: "+error.toString(), LENGTH_LONG).show();
                Log.i("EEEError de la fecha", error.toString());
            }
        });
        //requestQueue.add(busq);
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(busq);
    }

    protected void terminarGuardar(String auxcp, String date){
        Log.i("DATO1 ", auxcp);
        Log.i("DATO2 ", date);
        String auxfech = date.substring(2,21);
        Log.i("DATOAGUARDAR ", auxfech);
        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url =direIP+"registroAcred.php?id="+auxcp+"&fecha="+auxfech+"";
        url=url.replace(" ", "%20");
        StringRequest pedido = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        txt7.setText("Se realizo con éxito la acreditación");
                        //rvDatos.setVisibility(View.INVISIBLE);
                        btnAceptar.setVisibility(View.INVISIBLE);
                        //btnCancel.setVisibility(View.INVISIBLE);
                        //Intent volver = new Intent(Main2Activity.this, MainActivity.class);
                        //startActivity(volver);

                        //FragmentManager fm=getFragmentManager();
                        //fm.beginTransaction().replace(R.id.escenarioColaborador,mifrag).commit();
                        //este tambien deberia ser un intent con finish
                        //final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        //ft.replace(R.id.escenarioColaborador, new acreditacionFragment());
                        //ft.addToBackStack(null);
                        //ft.commit();
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                txt7.setText("No se pudo Acreditar, verifique estado Inscripcion");
                btnAceptar.setVisibility(View.INVISIBLE);
                //finish();
            }
        });

        //requestQueue.add(pedido);
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(pedido);

    }
}
