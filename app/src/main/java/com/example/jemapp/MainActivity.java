package com.example.jemapp;


import android.Manifest;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.widget.Toast.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private EditText editPass;
    private EditText editUser;
    private Button ingresar, recuperar;
    private JSONArray ja;
    private direccionIP URL;
    private String direccionIP, imgURL;
    private ImageView imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        URL=new direccionIP();
        direccionIP=URL.getIP();

        imagen = (ImageView) findViewById(R.id.imageLogo);
        editUser = (EditText) findViewById(R.id.editUsuario);
        editPass = (EditText) findViewById(R.id.editContrasenia);
        ingresar=(Button) findViewById(R.id.btnLOGUIN);
        recuperar=(Button) findViewById(R.id.btnRecuperar);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                new SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("¡Atención!")
                        .setContentText("Debes otorgar el permiso de la CAMARA para poder continuar")
                        .setConfirmText("Solicitar permiso")
                        .setCancelText("Cancelar")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.CAMERA},
                                        MY_PERMISSIONS_REQUEST_CAMERA);
                            }
                        })
                        .show();


            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);

            }
        }else {

            ingresar.setOnClickListener(this);
            recuperar.setOnClickListener(this);

            //RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            imgURL = direccionIP+"img/logo_JEM.png";
            // Initialize a new ImageRequest
            ImageRequest imageRequest = new ImageRequest(
                    imgURL, // Image URL
                    new Response.Listener<Bitmap>() { // Bitmap listener
                        @Override
                        public void onResponse(Bitmap response) {
                            // Do something with response
                            imagen.setImageBitmap(response);

                            // Save this downloaded bitmap to internal storage
                            Uri uri = saveImageToInternalStorage(response);

                            // Display the internal storage saved image to image view
                            //mImageViewInternal.setImageURI(uri);
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
                            makeText(MainActivity.this,"error", LENGTH_LONG).show();
                        }
                    }
            );
            // Add ImageRequest to the RequestQueue
            //requestQueue.add(imageRequest);
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(imageRequest);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    ingresar.setOnClickListener(this);
                    recuperar.setOnClickListener(this);

                    //RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    imgURL = direccionIP+"img/logo_JEM.png";
                    // Initialize a new ImageRequest
                    ImageRequest imageRequest = new ImageRequest(
                            imgURL, // Image URL
                            new Response.Listener<Bitmap>() { // Bitmap listener
                                @Override
                                public void onResponse(Bitmap response) {
                                    // Do something with response
                                    imagen.setImageBitmap(response);

                                    // Save this downloaded bitmap to internal storage
                                    Uri uri = saveImageToInternalStorage(response);

                                    // Display the internal storage saved image to image view
                                    //mImageViewInternal.setImageURI(uri);
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
                                    makeText(MainActivity.this,"error", LENGTH_LONG).show();
                                }
                            }
                    );
                    // Add ImageRequest to the RequestQueue
                    //requestQueue.add(imageRequest);
                    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(imageRequest);

                } else {
                    Toast.makeText(this, "No se otorgaron los permisos", Toast.LENGTH_LONG).show();
                    //this.mensaje.setText("No se otorgaron permisos para usar la camara");
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    // Custom method to save a bitmap into internal storage
    protected Uri saveImageToInternalStorage(Bitmap bitmap){
        // Initialize ContextWrapper
        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());

        // Initializing a new file
        // The bellow line return a directory in internal storage
        File file = wrapper.getDir("Images",MODE_PRIVATE);

        // Create a file to save the image
        file = new File(file, "UniqueFileName"+".jpg");

        try{
            // Initialize a new OutputStream
            OutputStream stream = null;

            // If the output file exists, it can be replaced or appended to it
            stream = new FileOutputStream(file);

            // Compress the bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);

            // Flushes the stream
            stream.flush();

            // Closes the stream
            stream.close();

        }catch (IOException e) // Catch the exception
        {
            e.printStackTrace();
        }

        // Parse the gallery image url to uri
        Uri savedImageURI = Uri.parse(file.getAbsolutePath());

        // Return the saved image Uri
        return savedImageURI;
    }


    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.btnLOGUIN:
                String usuario=editUser.getText().toString();
                String contrasenia=editPass.getText().toString();
                if (!usuario.equals("") && !contrasenia.equals(""))
                {
                    ConsultarUsuario(direccionIP+"consulta.php?user="+usuario+"&pass="+contrasenia);

                }
                else
                {
                    makeText(this, "Faltan completar datos", LENGTH_SHORT).show();

                }
                break;
            case R.id.btnRecuperar:
                Intent intentR = new Intent (v.getContext(), RecuperarPass.class);
                startActivityForResult(intentR, 0);
        }

    }

    private void ConsultarUsuario(String URL) {


        Log.i("url",""+URL);
        //Toast.makeText(this, "Paso x aca", Toast.LENGTH_SHORT).show();
        //RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest =  new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    ja = new JSONArray(response);
                    String tipouser = ja.getString(3);
                    String iduser =  ja.getString(0);
                    if (tipouser.equals("4"))
                    {
                        Intent intent = new Intent(MainActivity.this, menuOrganizadorActivity.class);
                        intent.putExtra("iduser", iduser);
                        intent.putExtra("tipouser", tipouser);
                        startActivity(intent);

                    }
                    if (tipouser.equals("5"))
                    {
                        Intent intent = new Intent(MainActivity.this, menuColaboradorActivity.class);
                        intent.putExtra("iduser", iduser);
                        intent.putExtra("tipouser", tipouser);
                        startActivity(intent);
                    }
                    if (tipouser.equals("6"))
                    {
                        makeText(getApplicationContext(),"Bienvenido Asistente", LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, menuAsistenteActivity.class);
                        intent.putExtra("iduser", iduser);
                        intent.putExtra("tipouser", tipouser);
                        startActivity(intent);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                    makeText(getApplicationContext(),"El usuario no existe en la base de datos", LENGTH_LONG).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
        //queue.add(stringRequest);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
