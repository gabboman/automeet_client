package com.example.ale.carsharing_university;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;


public class Registro extends AppCompatActivity {


    JSONObject jsonobject;
    JSONArray jsonarray;
    ProgressDialog mProgressDialog;
    ArrayList<String> pueblos;

    ArrayList<String> listaPueblos;
    //ArrayLIst<Pueblo> pueblo;
    EditText etResponse;
    TextView tvIsConnected;

    //Inicializamos las variales de los campos a rellenar del registro
    EditText nombre;
    String nombreString;

    EditText correo;
    String correoString;

    EditText telefono;
    String telefonoString;

    EditText contraseña;
    String contraseñaString;

    Spinner pueblo;
    String puebloString;




    //Conexión Petición GET a la API
    private StringBuffer ApiPetition(String method,String urlString) {


        StringBuffer chaine = new StringBuffer("");
        try{
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestProperty("User-Agent", "");
            connection.setRequestMethod(method);//connection.setRequestMethod("POST")
            connection.setDoInput(true);
            connection.connect();

            InputStream inputStream = connection.getInputStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = rd.readLine()) != null) {
                chaine.append(line);
            }

        } catch (IOException e) {
            // writing exception to log
            e.printStackTrace();
        }

        return chaine;
    }

    //Conexión Petición POST a la API
    private StringBuffer ApiPost(String urlString ) {


        StringBuffer chaine = new StringBuffer("");
        try{
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("User-Agent", "");
            connection.setRequestMethod("POST");//connection.setRequestMethod("POST")
            connection.setDoInput(true);
            connection.connect();

            InputStream inputStream = connection.getInputStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = rd.readLine()) != null) {
                chaine.append(line);
            }

        } catch (IOException e) {
            // writing exception to log
            e.printStackTrace();
        }

        return chaine;
    }



    // Petición GET a la API
    private class AsyncRequest extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String ... urls) {

            return new String(ApiPetition(urls[0],urls[1]));
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            try {
                JSONObject json = new JSONObject(result);
                //etResponse.setText(json.toString(1));
                //etResponse.setText(json.optString("3"));
                ArrayList<String> nombresPueblos=new ArrayList<>();
                for(Iterator<String> iter = json.keys();iter.hasNext();){
                    String id_pueblo = iter.next();

                    // Añadimos los pueblos a la lista de pueblos
                    nombresPueblos.add(json.getString(id_pueblo));

                }

                pueblo.setAdapter(new ArrayAdapter<String>(Registro.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        nombresPueblos));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //etResponse.setText(result);
        }
    }


    // Invocación a la acitividad
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);



        // Invocamos a la Petición GET de la API para el spinner dinámico
        pueblo=(Spinner)findViewById(R.id.spinner_pueblos);
        new AsyncRequest().execute("GET", "http://mc.hamburcraft.xyz:5000/pueblos/");



        // Acción al precionar boton de registro
        Button botonRegistrar = (Button)findViewById(R.id.button5);

        assert botonRegistrar != null;
        botonRegistrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Convertimos las variables a String para su tratamiento para comprobar que los campos no son vacíos
                nombre = (EditText)findViewById(R.id.editText);
                if (nombre != null) {
                    nombreString = nombre.getText().toString();
                }

                correo = (EditText)findViewById(R.id.editText2);
                if (correo != null) {
                    correoString = correo.getText().toString();
                }

                telefono = (EditText)findViewById(R.id.editText3);
                if (telefono != null) {
                    telefonoString = telefono.getText().toString();
                }


                contraseña = (EditText)findViewById(R.id.editText5);
                if (contraseña != null) {
                    contraseñaString = contraseña.getText().toString();
                }



                // Probamos que los campos no sean vacíos
                getNombre();
                getCorreo();
                getTelefono();
                getContraseña();

                //Cogemos el valor que contiene el spinner
                puebloString = pueblo.getSelectedItem().toString();


                //Si todos los campos son correctos continuamos y registramos en la base de datos
                // Invocamos a la Petición POST de la API
                try {
                    new PostCommentTask().execute(new URL("http://mc.hamburcraft.xyz:5000/registro/"));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                // Vamos a la ventana de confirmación del registro
                Intent intent = new Intent(Registro.this, MensajeConfirmacion.class);
                startActivity(intent);

            }
        });

    }


    //Restricciones de los campos no pueden ser vacíos
    public boolean getNombre(){
        boolean aux = true;
        if (nombreString.isEmpty()){
            Toast.makeText(this, "Tiene que indicar un nombre", Toast.LENGTH_LONG).show();
            aux = false;
        }
        return aux;
    }

    public boolean getCorreo(){
        boolean aux = true;
        if (correoString.isEmpty()){
            Toast.makeText(this, "Tiene que indicar un correo", Toast.LENGTH_LONG).show();
            aux = false;
        }else{
            if(!correoString.contains("@")){
                Toast.makeText(this, "Por favor, introduzca un correo válido", Toast.LENGTH_LONG).show();
                aux = false;
            }
        }
        return aux;
    }

    public boolean getTelefono(){
        boolean aux = true;
        if (telefonoString.isEmpty()){
            Toast.makeText(this, "Por favor, introduzca un teléfono", Toast.LENGTH_LONG).show();
            aux = false;
        }else{
            if(telefonoString.length()!=9){
                Toast.makeText(this, "Por favor, introduzca un teléfono válido", Toast.LENGTH_LONG).show();
                aux = false;
            }
        }
        return aux;
    }

    public boolean getContraseña(){
        boolean aux = true;
        if (contraseñaString.isEmpty()){
            Toast.makeText(this, "Tiene que indicar una contraseña", Toast.LENGTH_LONG).show();
            aux = false;
        }else{
            if(contraseñaString.length()<6){
                Toast.makeText(this, "La contraseña debe de tener 6 o más carácteres", Toast.LENGTH_LONG).show();
                aux = false;
            }
        }
        return aux;
    }




    //// Petición POST a la API (Descargar archivo JSON método AsyncTask)
    public class PostCommentTask extends AsyncTask<URL, Void, Void> {

        @Override
        protected Void doInBackground(URL... urls) {
            // Obtener la conexión
            HttpURLConnection con = null;

            try {

                con = (HttpURLConnection)urls[0].openConnection();


                // Variables de la base de datos
                String charset = "UTF-8";  // Or in Java 7 and later, use the constant: java.nio.charset.StandardCharsets.UTF_8.name()

                String nombre = nombreString;
                String apellidos = "Vacío";
                String telefono= telefonoString;
                String pueblo= puebloString;
                String correo= correoString;
                String password= contraseñaString;

                String query = String.format("nombre=%s&apellidos=%s&telefono=%s&pueblo=%s&correo=%s&password=%s",
                        URLEncoder.encode(nombre, charset),
                        URLEncoder.encode(apellidos, charset),
                        URLEncoder.encode(telefono, charset),
                        URLEncoder.encode(pueblo, charset),
                        URLEncoder.encode(correo, charset),
                        URLEncoder.encode(password, charset));

                // Activar método POST
                con.setDoOutput(true);

                // Tamaño previamente conocido
                con.setFixedLengthStreamingMode(query.getBytes().length);

                // Establecer application/x-www-form-urlencoded debido al formato clave-valor
                con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

                /*
                OutputStream out = new BufferedOutputStream(con.getOutputStream());

                out.write(data.getBytes());
                out.flush();
                out.close();*/
                OutputStream output = con.getOutputStream();
                output.write(query.getBytes(charset));
                InputStream response = con.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(response));
                StringBuffer chaine = new StringBuffer("");
                String line="";
                while ((line = rd.readLine()) != null) {
                    chaine.append(line);
                }

                JSONObject json = new JSONObject(chaine.toString());
                Log.w("respuesta",json.toString());


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(con!=null)
                    con.disconnect();
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void s) {
            Toast.makeText(getBaseContext(), "Registro realizado", Toast.LENGTH_LONG).show();
        }
    }
}
