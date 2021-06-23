package com.example.jemapp;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.StringRequest;


public class CodigoQRFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;
    private ImageView imagen;
    private String iduser;
    private direccionIP URL;
    private String direIP;

    public CodigoQRFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CodigoQRFragment.
     */

    public static CodigoQRFragment newInstance(String param1, String param2) {
        CodigoQRFragment fragment = new CodigoQRFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_codigo_qr, container, false);
        imagen=(ImageView)view.findViewById(R.id.codigoQR);
        URL = new direccionIP();
        direIP=URL.getIP();
        final Bitmap[] imgbitmap = new Bitmap[1];
        //iduser="1";
        iduser = getArguments().getString("iduser"); //mostrarcodQR.php
        String url =direIP+"mostrarcodQR.php?id="+iduser+"";
        StringRequest bufot = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    byte [] byteCode = Base64.decode(response,Base64.DEFAULT);
                    imgbitmap[0] = BitmapFactory.decodeByteArray(byteCode,0,byteCode.length);
                    imagen.setImageBitmap(imgbitmap[0]);

                } catch (Exception e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        //buscarFoto.add(bufot);
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(bufot);
        // Inflate the layout for this fragment
        return view;
    }


}
