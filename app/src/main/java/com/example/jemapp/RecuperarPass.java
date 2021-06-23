package com.example.jemapp;

import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class RecuperarPass extends AppCompatActivity {

    //private static final String TAG = 1;
    private Button btnRecupera;
    private EditText editDNI;
    private direccionIP direccion;
    private ImageView imagen;
    private String url;
    private  JSONArray ja;
    private String mImageURLString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_pass);
            getSupportActionBar().hide();
        direccion=new direccionIP();

        imagen=(ImageView) findViewById(R.id.imageLogo);
        editDNI=(EditText) findViewById(R.id.dni);
        btnRecupera=(Button) findViewById(R.id.btnRecuperar);

        //RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        mImageURLString = direccion.getIP()+"img/logo_JEM.png";
        // Initialize a new ImageRequest
        ImageRequest imageRequest = new ImageRequest(
                mImageURLString, // Image URL
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
                        makeText(RecuperarPass.this,"error", LENGTH_LONG).show();
                    }
                }
        );
        // Add ImageRequest to the RequestQueue
        //requestQueue.add(imageRequest);
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(imageRequest);
        btnRecupera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your handler code here
                String nroDNI=editDNI.getText().toString();
                if(nroDNI.equals(""))
                {
                    Toast toast3 = new Toast(getApplicationContext());
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.lytLayout));
                    TextView txtMsg = (TextView)layout.findViewById(R.id.txtMensaje);
                    txtMsg.setText("Debe ingresar su nro de DNI");
                    toast3.setDuration(Toast.LENGTH_SHORT);
                    toast3.setView(layout);
                    toast3.show();
                }
                else
                {
                    url=direccion.getIP()+"recuperarUsuario.php?dni="+nroDNI;
                    recuperarUsuario(url);
                }
            }
        });
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

    public void recuperarUsuario(String URL)
    {

        Log.i("url","");
        //Toast.makeText(this, "Paso x aca", Toast.LENGTH_SHORT).show();
        //RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest =  new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    ja = new JSONArray(response);
                    String user = ja.getString(0);
                    String pass = ja.getString(2);
                    String email= ja.getString(1);
                    enviarCorreo(email,user,pass);
                } catch (JSONException e) {
                    e.printStackTrace();
                    makeText(getApplicationContext(),"No se encuentra registrado", LENGTH_LONG).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        //queue.add(stringRequest);
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


    public void enviarCorreo(String email, String user, String pass)
    {
        String to=email;
        String subject="Recuperar Contraseña";
        String message="Estimado asistente. Le mandamos los datos para poder ingresar a JEMapp. " +
                "\n Nombre de usuario: "+user + "\n Contraseña: "+pass;

        //Creating SendMail object
        SendMail sm = new SendMail(this, to, subject, message);

        //Executing sendmail to send email
        sm.execute();
    }
}
