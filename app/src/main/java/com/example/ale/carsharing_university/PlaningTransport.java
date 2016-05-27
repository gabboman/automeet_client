package com.example.ale.carsharing_university;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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

public class PlaningTransport extends AppCompatActivity {

    Button button_selec_hora;
    static final int Dialogo_ID = 0;
    int hora_x;
    int minutos_x;



    //String tokenString;

    Spinner dia;
    String diaString;

    String horaString;

    String minutosString;

    //String horaLlegadaString;

    //String minutosLlegadaString;

    //String plazasString;

    EditText precio;
    String precioString;

    //String detallesString;

    Spinner spinner2;
    String destinoString;


    String esdevueltaString;

    // Iniciamos los radiobutton
    private RadioButton rbOpcion1;
    private RadioButton rbOpcion2;





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
        setContentView(R.layout.activity_planing_transport);
        showTimePickerDialogo();

        // Spinner estático dia
        dia = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.diassemana, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dia.setAdapter(adapter);

        // Spinner estático Provincia
        Spinner spinner = (Spinner) findViewById(R.id.spinner4);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.Provincia, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter1);

        // Spinner estático campus
        spinner2 = (Spinner) findViewById(R.id.spinner5);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.campus, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);



        //radiobutton
        rbOpcion1 = (RadioButton)findViewById(R.id.radioButton);
        rbOpcion2 = (RadioButton)findViewById(R.id.radioButton2);


        Button boton = (Button)findViewById(R.id.button);
        assert boton != null;
        boton.setOnClickListener(new View.OnClickListener() {




            @Override
            public void onClick(View v) {

                //Cogemos el valor del precio
                precio =  (EditText)findViewById(R.id.editText4);
                precioString = precio.getText().toString();

                //Cogemos el valor que contiene el spinner dia
                diaString = dia.getSelectedItem().toString();

                // Transformamos los dias a números
                if(diaString.contains("Lunes")){
                    diaString = "1";
                }
                if(diaString.contains("Martes")){
                    diaString = "2";
                }
                if(diaString.contains("Miercoles")){
                    diaString = "3";
                }
                if(diaString.contains("Jueves")){
                    diaString = "4";
                }
                if(diaString.contains("Viernes")){
                    diaString = "5";
                }

                //Cogemos el valor que contiene el spinner campus
                destinoString = spinner2.getSelectedItem().toString();

                //Transformamos los destinos a números
                if(destinoString.contains("Reina Mercedes")) {
                    destinoString = "1";
                }
                if(destinoString.contains("Pabo de olavide")) {
                    destinoString = "2";
                }
                if(destinoString.contains("Cartuja")) {
                    destinoString = "3";
                }
                if(destinoString.contains("Ramón y Cajal")) {
                    destinoString = "4";
                }
                if(destinoString.contains("Macarena")) {
                    destinoString = "5";
                }



                //llevamos a acabo la acción
                enviar();

            }
        });

    }


    public void showTimePickerDialogo(){
        button_selec_hora = (Button)findViewById(R.id.button8);
        button_selec_hora.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                showDialog(Dialogo_ID);
            }
        });
    }

    protected Dialog onCreateDialog(int id){
        if(id == Dialogo_ID){
            return new TimePickerDialog(PlaningTransport.this,kTimePickerListner, hora_x,minutos_x,false);
        }
        return null;
    }

    protected TimePickerDialog.OnTimeSetListener kTimePickerListner = new TimePickerDialog.OnTimeSetListener(){
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            hora_x = hourOfDay;
            minutos_x = minute;
            TextView HORA = (TextView)findViewById(R.id.textView14);
            String hora_xString=Integer.toString(hora_x);
            String minutos_xString=Integer.toString(minutos_x);
            HORA.setText(hora_xString + " : " + minutos_xString);
            Toast.makeText(PlaningTransport.this, hora_x + " : " + minutos_x,Toast.LENGTH_LONG).show();

            // Cogemos la hora y minutos por separado
            horaString = hora_xString;
            minutosString = minutos_xString;
        }
    };


    //Cogemos los valores de los radiobutton y haremos el POST a la API
    public void enviar() {
        if (rbOpcion1.isChecked()==true) {
            esdevueltaString = "0";
        }
        if(rbOpcion2.isChecked()==true) {
               esdevueltaString = "1";
        }


        //Si todos los campos son correctos continuamos y registramos en la base de datos
        // Invocamos a la Petición POST de la API
        try {
            new PostCommentTask().execute(new URL("http://mc.hamburcraft.xyz:5000/creaviaje/"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }



    // Envio del viaje a la base de datos

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

                String token=Global.token;
                String dia=diaString;
                String hora=horaString;
                String minutos=minutosString;
                String horaLlegada="0";
                String minutosLlegada="0";
                String plazas="0";
                String precio=precioString;
                String detalles="Esto es una prueba";
                String destino=destinoString;
                String esdevuelta=esdevueltaString;

                String query = String.format("token=%s&dia=%s&hora=%s&minutos=%s&horaLlegada=%s&minutosLlegada=%s&plazas=%s&precio=%s&detalles=%s&destino=%s&esdevuelta=%s",
                        URLEncoder.encode(token, charset),
                        URLEncoder.encode(dia, charset),
                        URLEncoder.encode(hora, charset),
                        URLEncoder.encode(minutos, charset),
                        URLEncoder.encode(horaLlegada, charset),
                        URLEncoder.encode(minutosLlegada, charset),
                        URLEncoder.encode(plazas, charset),
                        URLEncoder.encode(precio, charset),
                        URLEncoder.encode(detalles, charset),
                        URLEncoder.encode(destino, charset),
                        URLEncoder.encode(esdevuelta, charset));

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
            Toast.makeText(getBaseContext(), "Viaje creado", Toast.LENGTH_LONG).show();
        }
    }

}
