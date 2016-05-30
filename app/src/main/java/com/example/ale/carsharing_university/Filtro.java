package com.example.ale.carsharing_university;

import android.app.Dialog;
import android.app.TimePickerDialog;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Filtro extends AppCompatActivity {


    Button button_selec_hora;
    static final int Dialogo_ID = 0;
    int hora_x;
    int minutos_x;


    String horaString;

    String minutosString;

    Spinner dia;
    String diaString;

    Spinner spinner2;
    String destinoString;

    String esdevueltaString;

    EditText margen;
    String margenString;

    Spinner pueblo;
    String puebloString;

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

                pueblo.setAdapter(new ArrayAdapter<String>(Filtro.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        nombresPueblos));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //etResponse.setText(result);
        }
    }


    // Invocación a la acitividad
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro);
        showTimePickerDialogo();

        // Invocamos a la Petición GET de la API para el spinner dinámico
        pueblo=(Spinner)findViewById(R.id.spinner_pueblos);
        new AsyncRequest().execute("GET", "http://mc.hamburcraft.xyz:5000/pueblos/");


        // Spinner estático dia
        dia = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.diassemana, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dia.setAdapter(adapter);

        // Spinner estático campus
        spinner2 = (Spinner) findViewById(R.id.spinner5);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.campus, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        //radiobutton
        rbOpcion1 = (RadioButton)findViewById(R.id.radioButton);
        rbOpcion2 = (RadioButton)findViewById(R.id.radioButton2);

        Button boton = (Button)findViewById(R.id.button3);
        assert boton != null;
        boton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                //Cogemos el valor del margen

                margen = (EditText)findViewById(R.id.editText8);
                margenString = margen.getText().toString();


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

                //Cogemos el valor que contiene el spinner
                puebloString = pueblo.getSelectedItem().toString();


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
            return new TimePickerDialog(Filtro.this,kTimePickerListner, hora_x,minutos_x,false);
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
            Toast.makeText(Filtro.this, hora_x + " : " + minutos_x,Toast.LENGTH_LONG).show();

            // Cogemos la hora y minutos por separado
            horaString = hora_xString;
            minutosString = minutos_xString;
        }
    };

    //Haremos el POST a la API
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
            new PostCommentTask().execute(new URL("http://mc.hamburcraft.xyz:5000/viajesFiltrados/"));
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

                String dia=diaString;
                String hora=horaString;
                String minutos=minutosString;
                String destino=destinoString;
                String origen=puebloString;
                String margen=margenString;
                String vuelta=esdevueltaString;

                String query = String.format("dia=%s&hora=%s&minutos=%s&destino=%s&origen=%s&margen=%s&EsDeVuelta=%s",
                        URLEncoder.encode(dia, charset),
                        URLEncoder.encode(hora, charset),
                        URLEncoder.encode(minutos, charset),
                        URLEncoder.encode(destino, charset),
                        URLEncoder.encode(origen, charset),
                        URLEncoder.encode(margen, charset),
                        URLEncoder.encode(vuelta, charset));



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
                Log.w("filtraViajes",json.toString());



                JSONArray viajes_array = new JSONArray();
                List<String> lista_viaje = new ArrayList<>();
                for (Iterator<String> viajes = json.keys();viajes.hasNext();){
                    JSONObject viaje=json.getJSONObject(viajes.next());
                    Log.w("Viajes",viaje.toString());

                    //JSONObject habilidades = viaje.getJSONObject("salida");
                    //String salidas = viaje.getString("minutos");
                    //Log.w("salida3",salidas);

                    //GABRIEL MIRA AQUI HOSTIA PUTA
                    //viajes_array.(viaje);
                    //String salidastr=viaje.getString("salida");
                    //SimpleDateFormat salida=new SimpleDateFormat (salidastr);



                    //for (int i = 0; i < viaje.length(); i++) {
                        //JSONObject c = viaje.getJSONObject(String.valueOf(i));
                        String minutos2 = viaje.getString("minutos");
                        Log.w("Resultado",minutos2);
                        String precio = viaje.getString("precio");
                        Log.w("Resultado",precio);
                        String plazas = viaje.getString("plazas");
                        Log.w("Resultado",plazas);
                        String id_usuario = viaje.getString("id_usuario");
                        Log.w("Resultado",id_usuario);
                        String telefono = viaje.getString("telefono");
                        Log.w("Resultado",telefono);
                        String detalles = viaje.getString("detalles");
                        Log.w("Resultado",detalles);
                        String EsDeVuelta = viaje.getString("EsDeVuelta");
                        Log.w("Resultado",EsDeVuelta);
                        String dia2 = viaje.getString("dia");
                        Log.w("Resultado",dia2);
                        String hora2 = viaje.getString("hora");
                        Log.w("Resultado",hora2);

                        //lista_viaje.add(new String(String.valueOf(viajes_array.getJSONObject(i)))); //creamos un objeto Fruta y lo insertamos en la lista

                   // }

                    Log.w("Viajes filtrados",viaje.toString());

                }




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
            Toast.makeText(getBaseContext(), "Búsqueda realizada", Toast.LENGTH_LONG).show();
        }
    }
}
