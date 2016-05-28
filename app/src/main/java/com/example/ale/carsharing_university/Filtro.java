package com.example.ale.carsharing_university;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Filtro extends AppCompatActivity {

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
        setContentView(R.layout.activity_filtro);

        Button boton = (Button)findViewById(R.id.button3);
        assert boton != null;
        boton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {


                //llevamos a acabo la acción
                enviar();

            }
        });

    }

    //Haremos el POST a la API
    public void enviar() {

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

                String dia="1";
                String hora="0";
                String minutos="0";
                String destino="2";
                String origen="5";
                String margen="120";
                String vuelta="0";

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
                    String salidas = viaje.getString("salida");
                    Log.w("salida3",salidas);

                    //GABRIEL MIRA AQUI HOSTIA PUTA
                    //viajes_array.(viaje);
                    String salidastr=viaje.getString("salida");
                    SimpleDateFormat salida=new SimpleDateFormat (salidastr);



                    for (int i = 0; i < viaje.length(); i++) {
                        JSONObject c = viaje.getJSONObject(String.valueOf(i));
                        String salida1 = c.getString("salida");
                        Log.w("Viajes2",salida1);
                        //lista_viaje.add(new String(String.valueOf(viajes_array.getJSONObject(i)))); //creamos un objeto Fruta y lo insertamos en la lista

                    }

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
