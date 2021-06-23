package com.example.jemapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_LONG;

public class acreditacionFragment extends Fragment {
    private direccionIP URL;
    private TextView txtShowTextResult;
    private String direIP;
    private Button btnAcred;
    RecyclerView rvDatos;

    String iduser, tipouser;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_acreditacion, container, false);
        URL=new direccionIP();
        direIP=URL.getIP();

        iduser = getArguments().getString("iduser");
        tipouser= getArguments().getString("tipouser");
        //return inflater.inflate(R.layout.fragment_acreditacion, container, false);
        txtShowTextResult = (TextView)view.findViewById(R.id.txtDisplay);
        btnAcred = (Button) view.findViewById(R.id.btnAcred);
        rvDatos = (RecyclerView) view.findViewById(R.id.rvDatos);
        //rvDatos.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvDatos.setLayoutManager(layoutManager);

        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        //RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url =direIP+"consultaAcred.php";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        view.findViewById(R.id.progressBar).setVisibility(View.GONE);
                        // txtShowTextResult.setText("Response is: "+response);

                        response = response.replace("][",",");
                        if (response.length()>0){
                            try {
                                JSONArray ja = new JSONArray(response);
                                Log.i("EEEEEEEElargoarray", " "+ja.length());
                                CargarListView(ja);
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else {
                            view.findViewById(R.id.progressBar).setVisibility(View.GONE);
                            rvDatos.setVisibility(View.INVISIBLE);
                        }
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                view.findViewById(R.id.progressBar).setVisibility(View.GONE);
                txtShowTextResult.setText("No hay conexion a Internet!");
                rvDatos.setVisibility(View.INVISIBLE);
            }
        });

// Add the request to the RequestQueue.
        //queue.add(stringRequest);
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);

        btnAcred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent escanear = new Intent(getActivity(), escanerAcredActivity.class);
                //startActivity(escanear);
                IntentIntegrator integrator = new IntentIntegrator(getActivity());
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Posicione el codigo QR");
                integrator.setOrientationLocked(true);
                integrator.setBarcodeImageEnabled(false);
                //integrator.initiateScan();
                integrator.forSupportFragment(acreditacionFragment.this).initiateScan();
            }
        });

        return  view;
    }

    private void CargarListView(JSONArray ja) {
        ArrayList<Acreditados> listaarreglo = new ArrayList<Acreditados>();

        for (int i=0; i<ja.length(); i+=4){
            try {
                Acreditados listado = new Acreditados(ja.getString(i+1)+" "+ja.getString(i),ja.getString(i+2),ja.getString(i+3));
                listaarreglo.add(listado);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        AdaptadorRv nuevo = new AdaptadorRv(listaarreglo);
        rvDatos.setAdapter(nuevo);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.i("pasaaqui", "1");
        String linea1 = "J+E+M+19";
        String mensaje;
        System.out.println("never here");
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {// handle scan result
            Log.i("pasaaqui", "2");
            if(scanResult.getContents() == null) {
                Toast.makeText(getActivity(), "Cancelado...", LENGTH_LONG).show();

            } else {
                mensaje = scanResult.getContents();
                if (mensaje.contains(linea1)) {
                    Log.i("pasaaqui", "3");
                    String auxnomb;
                    int ininb;
                    int finnb;
                    ininb = mensaje.indexOf("q1-"); //nombre persona
                    finnb = mensaje.indexOf("-q1");
                    ininb = ininb + 3;
                    auxnomb = mensaje.substring(ininb, finnb);

                    String auxdni;
                    int inidni;
                    int findni;
                    inidni = mensaje.indexOf("q2-"); //dni persona
                    findni = mensaje.indexOf("-q2");
                    inidni = inidni + 3;
                    auxdni = mensaje.substring(inidni, findni);

                    final String auxcp;
                    int initit;
                    int fintit;
                    initit = mensaje.indexOf("q3-"); //codigo persona
                    fintit = mensaje.indexOf("-q3");
                    initit = initit + 3;
                    auxcp = mensaje.substring(initit, fintit);
                    Bundle bundle = new Bundle();
                    bundle.putString("iduser", iduser);
                    bundle.putString("tipouser", tipouser);
                    bundle.putString("nombPers", auxnomb);
                    bundle.putString("dniPers", auxdni);
                    bundle.putString("codPers", auxcp);
                    escanerAcredFragment myFrag = new escanerAcredFragment();
                    myFrag.setArguments(bundle);
                    FragmentManager fm = getFragmentManager();
                    if (tipouser.equals("5")) {
                        fm.beginTransaction().replace(R.id.escenarioColaborador, myFrag).addToBackStack(null).commit();
                    }else if (tipouser.equals("4")){
                        fm.beginTransaction().replace(R.id.escenarioOrganizador, myFrag).addToBackStack(null).commit();
                    }
                    //fm.beginTransaction().replace(R.id.escenarioColaborador, myFrag).addToBackStack(null).commit();

                } else {
                    Toast.makeText(getActivity(), "El codigo QR no es valido", LENGTH_LONG).show();

                }
            }
        }else {// else continue with any other code you need in the method
            super.onActivityResult(requestCode, resultCode, intent);
        }

    }
}
