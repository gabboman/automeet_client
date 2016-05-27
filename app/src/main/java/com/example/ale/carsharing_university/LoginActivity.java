package com.example.ale.carsharing_university;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
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
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {

    EditText correo;
    String correoString;

    EditText password;
    String passwordString;

    String respuestaLogin;

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





    // Invocación a la acitividad
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Acceder

        Button botonAcceder = (Button)findViewById(R.id.button10);
        botonAcceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Cogemos el valor del precio
                correo =  (EditText)findViewById(R.id.editText6);
                correoString = correo.getText().toString();

                //Cogemos el valor del precio
                password =  (EditText)findViewById(R.id.editText7);
                passwordString = password.getText().toString();

                //llevamos a acabo la acción
                enviar();

            }
        });

        //Registrarse
        Button boton = (Button)findViewById(R.id.button4);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, Registro.class);
                startActivity(intent);
            }
        });

        //boton de acceso temporal

        Button boton2 = (Button)findViewById(R.id.button6);
        boton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, PlaningTransport.class);
                startActivity(intent);
            }
        });
    }
    //Haremos el POST a la API
    public void enviar() {

        //Si todos los campos son correctos continuamos y registramos en la base de datos
        // Invocamos a la Petición POST de la API
        try {
            new PostCommentTask().execute(new URL("http://mc.hamburcraft.xyz:5000/login/"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
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

                String correo = correoString;
                String password = passwordString;

                String query = String.format("correo=%s&password=%s",
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
                Log.w("Login",json.toString());

                respuestaLogin = json.toString();

                if(respuestaLogin.contains("Error")){
                    Toast.makeText(getBaseContext(), "Correo o contraseña incorrecto", Toast.LENGTH_LONG).show();
                }

                List<String> tokenLogin = new ArrayList<>();

                try {
                    Global.token = json.getString("token");//.getJSONArray("token");
                    //JSONArray json_array = json.getJSONArray("token");;
                    Log.w("Login","EXITO "+Global.token);
                }catch(Exception e){
                    Log.w("Login","ERROR CON EL TOKEN");

                }



                /*for (int i = 0; i < json_array.length(); i++) {
                    JSONObject c = json_array.getJSONObject(i);
                    Global.token = c.getString("token");
                }*/
                Log.w("Login",Global.token);



                if(respuestaLogin.contains("token")){
                    Intent intent = new Intent(LoginActivity.this, PlaningTransport.class);
                    startActivity(intent);
                }



            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(con!=null)
                    con.disconnect();
            }

            return null;

        }

        /*@Override
        protected void onPostExecute(Void s) {
            Toast.makeText(getBaseContext(), "Logueado", Toast.LENGTH_LONG).show();
        }
        */
    }
}

