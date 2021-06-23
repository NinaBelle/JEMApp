package com.example.jemapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;
import static com.android.volley.VolleyLog.TAG;

public class perfilFragment extends Fragment implements View.OnClickListener {

    String iduser, tipouser, direccionIP, imgURL;
    String  apellido, nombre, dni, titulo, universidad, domicilio, correo, foto;
    direccionIP URL;
    private TextView textapellido, textnombre, textdni, texttitulo, textuniversidad, textdomicilio, textcorreo;
    private Button botonFoto, botonPass, botonMod, botonCodQR;
    private ImageView imagen;
    private JSONArray ja;
    private final int GALLERY = 1;
    JSONObject jsonObject;
    RequestQueue rQueue;

    String tag_json_obj = "json_obj_req";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_perfil, container, false);
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        iduser = getArguments().getString("iduser");
        tipouser = getArguments().getString("tipouser");
        //Toast.makeText(getActivity(),"Id Usuario: "+iduser,Toast.LENGTH_SHORT).show();
        URL=new direccionIP();
        direccionIP=URL.getIP();

        textapellido = (TextView)view.findViewById(R.id.textApellido);
        textnombre = (TextView) view.findViewById(R.id.textNombre);
        textdni = (TextView) view.findViewById(R.id.textDNI);
        texttitulo = (TextView) view.findViewById(R.id.textTitulo);
        textuniversidad = (TextView) view.findViewById(R.id.textUniversidad);
        textdomicilio = (TextView) view.findViewById(R.id.textDomicilio);
        textcorreo = (TextView) view.findViewById(R.id.textCorreo);

        botonFoto = (Button) view.findViewById(R.id.buttonFoto);
        botonFoto.setOnClickListener(this);
        botonPass = (Button) view.findViewById(R.id.buttonPass);
        botonPass.setOnClickListener(this);
        imagen=(ImageView) view.findViewById(R.id.foto);
        botonMod = (Button) view.findViewById(R.id.buttonMod);
        botonMod.setOnClickListener(this);
        botonCodQR = (Button)view.findViewById(R.id.buttonCodigoQR);
        botonCodQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("iduser", iduser);
                bundle.putString("tipouser", tipouser);

                CodigoQRFragment myFrag = new CodigoQRFragment();
                myFrag.setArguments(bundle);
                FragmentManager fm= getFragmentManager();
                if (tipouser.equals("5")) {
                    fm.beginTransaction().replace(R.id.escenarioColaborador, myFrag).addToBackStack(null).commit();
                }else if (tipouser.equals("6")){
                    fm.beginTransaction().replace(R.id.escenarioAsistente, myFrag).addToBackStack(null).commit();
                }
            }
        });
        //Toast.makeText(getApplicationContext(),"Id Usuario: "+iduser,Toast.LENGTH_SHORT).show();
        //nombre = findViewById(R.id.textNombre);
        //requestJSON();
        requestUsuario(direccionIP+"datosusuario.php?iduser="+iduser);

        return  view;

    }

    // Custom method to save a bitmap into internal storage
    protected Uri saveImageToInternalStorage(Bitmap bitmap){
        // Initialize ContextWrapper
        ContextWrapper wrapper = new ContextWrapper(getActivity());

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


    private void requestUsuario(String URL) {

        Log.i("url",""+URL);

        //RequestQueue queue = Volley.newRequestQueue(this);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest =  new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    ja = new JSONArray(response);
                    DatosUsuario userData = new DatosUsuario();

                    apellido = ja.getString(0);
                    userData.setApellido(apellido);
                    nombre = ja.getString(1);
                    userData.setNombre(nombre);
                    dni = ja.getString(2);
                    userData.setDNI(dni);
                    titulo = ja.getString(3);
                    userData.setTitulo(titulo);
                    universidad = ja.getString(4);
                    userData.setUniversidad(universidad);
                    domicilio= ja.getString(5);
                    userData.setDomicilio(domicilio);
                    correo= ja.getString(6);
                    userData.setCorreo(correo);
                    foto= ja.getString(7);

                    textapellido.setText(textapellido.getText()+userData.getApellido());
                    textnombre.setText(textnombre.getText()+ userData.getNombre());
                    textdni.setText(textdni.getText()+ userData.getDNI());
                    texttitulo.setText(texttitulo.getText()+ userData.getTitulo());
                    textuniversidad.setText(textuniversidad.getText()+userData.getUniversidad());
                    textdomicilio.setText(textdomicilio.getText()+ userData.getDomicilio());
                    textcorreo.setText(textcorreo.getText()+ userData.getCorreo());

                    if(!foto.equals(""))
                    {
                        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
                        imgURL = direccionIP+"imgPers/"+foto+".JPG";
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
                                        makeText(getActivity(),"error", LENGTH_LONG).show();
                                    }
                                }
                        );
                        // Add ImageRequest to the RequestQueue
                        requestQueue.add(imageRequest);
                    }

                    //String domicilio = ja.getString(7);
                    //Toast.makeText(getApplicationContext(),"Domicilio Usuario: "+domicilio,Toast.LENGTH_SHORT).show();
                    //datos.add(domicilio);
                    //Toast.makeText(getApplicationContext(),nombre.toString(),Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_LONG).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(stringRequest);


    }
    @Override
    public void onClick(View v) {
        //Mostrar el diálogo de progreso
        //final ProgressDialog loading = ProgressDialog.show(this,"Subiendo...","Espere por favor...",false,false);
        switch(v.getId()) {
            case R.id.buttonFoto:
                cargarImagen();
                break;
            case R.id.buttonPass:
                cambiarPass();
                break;
            case R.id.buttonMod:
                //Toast.makeText(getApplicationContext(),"boton apretado",Toast.LENGTH_SHORT).show();
                modificarDatos();
                break;
        }

    }


    public  void cargarImagen(){
        //Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //intent.setType("image/");
        //startActivityForResult(intent.createChooser(intent,"Seleccione"),10);
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    Bitmap reducida = Bitmap.createScaledBitmap(bitmap, (850*bitmap.getWidth())/bitmap.getHeight(), 850, true);
                    //Bitmap croppedBmp = Bitmap.createBitmap(reducida, 500, 500, 500, 500);
                    imagen.setImageBitmap(reducida);
                    uploadImage(reducida);

                } catch (IOException e) {
                    e.printStackTrace();
                    //Toast.makeText(getActivity().this, "Failed!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(),"Failed",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void uploadImage(Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        try {
            jsonObject = new JSONObject();
            String imgname = String.valueOf(Calendar.getInstance().getTimeInMillis());
            jsonObject.put("name", imgname);
            //  Log.e("Image name", etxtUpload.getText().toString().trim());
            jsonObject.put("image", encodedImage);
            // jsonObject.put("aa", "aa");
        } catch (JSONException e) {
            Log.e("JSONObject Here", e.toString());
        }
        String upload_URL=direccionIP+"uploadimg.php?iduser="+iduser;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, upload_URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.e("aaaaaaa", jsonObject.toString());
                        rQueue.getCache().clear();
                        //Toast.makeText(getApplication(), "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(),"Imagen actualizada",Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("aaaaaaa", volleyError.toString());

            }
        });

        rQueue = Volley.newRequestQueue(getActivity());
        rQueue.add(jsonObjectRequest);

    }


    public void cambiarPass()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        View v_iew=inflater.inflate(R.layout.pass_dialog, null) ;
        builder.setView(v_iew);
        builder.setNegativeButton("CANCELAR",null);
        final EditText uName = v_iew.findViewById(R.id.EditText_Pwd1);
        final EditText uName2 = v_iew.findViewById(R.id.EditText_Pwd2);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String pass=uName.getText().toString();
                String pass2=uName2.getText().toString();

                if(!pass.equals(pass2))
                {
                    Toast.makeText(getActivity(),"Las contraseñas no coinciden",Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(),"Las contraseñas no coinciden",Toast.LENGTH_SHORT).show();
                }
                else{
                    String url2=direccionIP+"actualizarPass.php?iduser="+ iduser+"&pass="+pass ;
                    updateContrasenia(url2);
                }
            }
        });

        builder.create();
        builder.show();
    }

    public void updateContrasenia(String URL)
    {
        Log.i("url",""+URL);

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest =  new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    Object json = new JSONObject(response);
                    //Toast.makeText(getApplicationContext(),"json:" +((JSONObject) json).get("code").toString(),Toast.LENGTH_LONG).show();
                    if(((JSONObject) json).get("code").equals(0))
                    {
                        //Toast.makeText(getApplicationContext(),"Tu contraseña se cambio exitosamente",Toast.LENGTH_LONG).show();
                        Toast.makeText(getActivity(),"Tu contraseña se cambio exitosamente",Toast.LENGTH_SHORT).show();
                        //mostrarDialogo("Te has dado de baja al curso");
                    }
                    else {
                         Toast.makeText(getActivity(),"No se pudo actualizar la contraseña",Toast.LENGTH_SHORT).show();
                        //mostrarDialogo("No puedes inscribirte al curso por que ya no hay cupo");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_LONG).show();

                }
                //Toast.makeText(getActivity(),"error",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(stringRequest);

    }

    public void modificarDatos()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        View v_iew=inflater.inflate(R.layout.modificar_datos_dialog, null) ;
        builder.setView(v_iew);
        builder.setNegativeButton("CANCELAR",null);
        final EditText uApellido = v_iew.findViewById(R.id.editApellido);
        uApellido.setText(apellido);
        final EditText uNombre = v_iew.findViewById(R.id.editNombre);
        uNombre.setText(nombre);
        final EditText uDNI = v_iew.findViewById(R.id.editDNI);
        uDNI.setText(dni);
        final EditText uTitulo = v_iew.findViewById(R.id.editTitulo);
        uTitulo.setText(titulo);
        final EditText uUniversidad = v_iew.findViewById(R.id.editUniversidad);
        uUniversidad.setText(universidad);
        final EditText uDomicilio = v_iew.findViewById(R.id.editDomicilio);
        uDomicilio.setText(domicilio);
        final EditText uCorreo = v_iew.findViewById(R.id.editCorreo);
        uCorreo.setText(correo);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String nvoApellido=uApellido.getText().toString();
                String nvoNombre=uNombre.getText().toString();
                String nvoDni=uDNI.getText().toString();
                String nvoTitulo=uTitulo.getText().toString();
                String nvoUniversidad=uUniversidad.getText().toString();
                String nvoDomicilio=uDomicilio.getText().toString();
                String nvoCorreo=uCorreo.getText().toString();

                if(nvoApellido.equals("")||nvoNombre.equals("")||nvoDni.equals("")||nvoTitulo.equals("")||nvoUniversidad.equals("")||nvoDomicilio.equals("")||nvoCorreo.equals(""))
                {
                    //mostrarMensaje();
                    //Toast.makeText(getApplicationContext(),"Faltan Completar Datos",Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(),"Faltan Completar Datos",Toast.LENGTH_SHORT).show();
                }
                else{
                    String url3=direccionIP+"modificarUsuario.php?iduser="+iduser+"&apellido="+nvoApellido+"&nombre="+nvoNombre+"&dni="+nvoDni+
                            "&titulo="+nvoTitulo+"&universidad="+nvoUniversidad+"&domicilio="+nvoDomicilio+"&correo="+nvoCorreo;
                    updateUsuario(url3);

                    Bundle bundle = new Bundle();
                    bundle.putString("iduser", iduser);
                    bundle.putString("tipouser", tipouser);

                    perfilFragment myFrag = new perfilFragment();
                    myFrag.setArguments(bundle);
                    FragmentManager fm= getFragmentManager();
                    if (tipouser.equals("5")) {
                        fm.beginTransaction().replace(R.id.escenarioColaborador, myFrag).addToBackStack(null).commit();
                    }else if (tipouser.equals("6")){
                        fm.beginTransaction().replace(R.id.escenarioAsistente, myFrag).addToBackStack(null).commit();
                    }
                }
            }
        });
        builder.create();
        builder.show();
    }


    public void updateUsuario(String URL)
    {
        Log.i("url",""+URL);

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest =  new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    Object json = new JSONObject(response);
                    //Toast.makeText(getApplicationContext(),"json:" +((JSONObject) json).get("code").toString(),Toast.LENGTH_LONG).show();
                    if(((JSONObject) json).get("code").equals(0))
                    {
                        Toast.makeText(getActivity(),"Los datos se actualizaron correctamente",Toast.LENGTH_SHORT).show();
                        //Intent intent = new Intent(PerfilColaborador.this, PerfilColaborador.class);
                        //intent.putExtra("iduser",iduser.toString());
                        //startActivity(intent);

                        //mostrarDialogo("Te has dado de baja al curso");
                    }
                    else {
                        //Toast.makeText(getApplicationContext(),"No se pudieron actualizar los datos",Toast.LENGTH_LONG).show();
                        Toast.makeText(getActivity(),"error",Toast.LENGTH_SHORT).show();
                        //mostrarDialogo("No puedes inscribirte al curso por que ya no hay cupo");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_LONG).show();
                    Toast.makeText(getActivity(),"error",Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(stringRequest);

    }

}
