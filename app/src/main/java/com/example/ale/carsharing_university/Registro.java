package com.example.ale.carsharing_university;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class Registro extends AppCompatActivity {

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

    Spinner universidad;
    String universidadString;

    Spinner campus;
    String campusString;

    EditText precio;
    String precioString;

    //Variable para la conexion
    Context ctx=this;


    //Invocación a la acitividad
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);


        // Acción al precionar boton de registro
        Button botonRegistrar = (Button)findViewById(R.id.button5);

        assert botonRegistrar != null;
        botonRegistrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Convertimos las variables a String para su tratamiento
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


  /*             pueblo = (Spinner)findViewById(R.id.spinner);

                    puebloString = pueblo.getSelectedItem().toString();


                universidad = (Spinner)findViewById(R.id.spinner2);

                    universidadString = universidad.getSelectedItem().toString();


                campus = (Spinner)findViewById(R.id.spinner3);

                    campusString = campus.getSelectedItem().toString();
*/

                precio = (EditText)findViewById(R.id.editText4);
                if (precio != null) {
                    precioString = precio.getText().toString();
                }


                // Probamos que los campos no sean vacíos
                getNombre();
                getCorreo();
                getTelefono();
                getContraseña();
                //getPueblo();
                //getUniversidad();
                //getCampus();
                getPrecio();

                //Si todos los campos son correctos continuamos
                if (getNombre() && getCorreo() && getTelefono() && getContraseña() && getPrecio()){

                    //Hacemos la conexión con el servidor y enviamos los datos del registro a la base de datos
                    BackGround b = new BackGround();
                    b.execute(nombreString, correoString, telefonoString, contraseñaString, puebloString,
                            universidadString,campusString,precioString);

                    //Si ha ido bien pasamos a la activity de confirmación
                    Intent intent = new Intent(Registro.this, MensajeConfirmacion.class);
                    startActivity(intent);
                }
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
        }
        return aux;
    }

    public boolean getTelefono(){
        boolean aux = true;
        if (telefonoString.isEmpty()){
            Toast.makeText(this, "Por favor, introduzca un número de teléfono", Toast.LENGTH_LONG).show();
            aux = false;
        }
        return aux;
    }

    public boolean getContraseña(){
        boolean aux = true;
        if (contraseñaString.isEmpty()){
            Toast.makeText(this, "Tiene que indicar una contraseña", Toast.LENGTH_LONG).show();
            aux = false;
        }
        return aux;
    }
/*
    public boolean getPueblo(){
        boolean aux = true;
        if (puebloString.isEmpty()){
            Toast.makeText(this, "Por favor, seleccione un pueblo", Toast.LENGTH_LONG).show();
            aux = false;
        }
        return aux;
    }

    public boolean getUniversidad(){
        boolean aux = true;
        if (universidadString.isEmpty()){
            Toast.makeText(this, "Por favor, introduzca una universidad", Toast.LENGTH_LONG).show();
            aux = false;
        }
        return aux;
    }

    public boolean getCampus(){
        boolean aux = true;
        if (campusString.isEmpty()){
            Toast.makeText(this, "Por favor, introduzca un campus de la universidad", Toast.LENGTH_LONG).show();
            aux = false;
        }
        return aux;
    }
*/
    public boolean getPrecio(){
        boolean aux = true;
        if (precioString.isEmpty()){
            Toast.makeText(this, "Indique una cuantía para compartir los gastos del viaje", Toast.LENGTH_LONG).show();
            aux = false;
        }
        return aux;
    }



    // Registro en el servidor

    class BackGround extends AsyncTask<String, String, String>{

        /*protected String doInBackgroud(String... params){
            String nombre = params[0];
            String correo = params[1];
            String telefono = params[2];
            String contraseña = params[3];
            String pueblo = params[4];
            String universidad = params[5];
            String campus = params[6];
            String precio = params[7];

            String data="";

            int tmp;

            try{
                URL url = new URL("http://localhost/registro.php");

                String urlParams = "nombre"+nombre+"&correo"+correo+"&telefono"+telefono+"&contraseña"+contraseña+"&pueblo"+pueblo+"&universidad"+universidad
                        +"&campus"+campus+"&precio"+precio;

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();
                InputStream is = httpURLConnection.getInputStream();
                while((tmp = is.read())!=1){
                    data+= (char)tmp;
                }

                is.close();
                httpURLConnection.disconnect();

                return data;

            }catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: "+e.getMessage();
            }catch (IOException e) {
                e.printStackTrace();
                return "Exception: "+e.getMessage();
            }
        }

        protected void onPostExcecute(String s){
            if(s.equals("")) {
                s = "Data saved successfully.";
            }
            Toast.makeText(ctx, s, Toast.LENGTH_LONG).show();
        }
        */

        protected String doInBackground(String... params) {
            String nombre = params[0];
            String correo = params[1];
            String telefono = params[2];
            String contraseña = params[3];
            //String pueblo = params[4];
            //String universidad = params[5];
            //String campus = params[5];
            String precio = params[4];

            String data="";

            int tmp;

            try{
                URL url = new URL("http://localhost/registro.php");

                String urlParams = "nombre"+nombre+"&correo"+correo+"&telefono"+telefono+"&contraseña"+contraseña +"&precio"+precio;

                new Thread(new Runnable(){
                    public void run()
                    {
                        try
                        {

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
               // os.write(urlParams.getBytes());
                os.flush();
                os.close();
                InputStream is = httpURLConnection.getInputStream();
                while((tmp = is.read())!=1){
                    data+= (char)tmp;
                }

                is.close();
                httpURLConnection.disconnect();

                        }
                        catch (Exception e)
                        {}
                    }
                }).start();

                return data;

            }catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: "+e.getMessage();
            }catch (IOException e) {
                e.printStackTrace();
                return "Exception: "+e.getMessage();
            }
        }
        protected void onPostExcecute(String s){
            if(s.equals("")) {
                s = "Data saved successfully.";
            }
            Toast.makeText(ctx, s, Toast.LENGTH_LONG).show();
        }
    }
}
