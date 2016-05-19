package com.example.ale.carsharing_university;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    JSONObject jsonobject;
    JSONArray jsonarray;
    ProgressDialog mProgressDialog;
    ArrayList<String> listaPueblos;
    //ArrayLIst<Pueblo> pueblo;
    EditText etResponse;
    TextView tvIsConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Descargar archivo JSON
        //new DescargaJSON().execute();
        new AsyncRequest().execute("GET","http://mc.hamburcraft.xyz:5000/pueblos/");

        /*
        //Creación archivo xml pueblos
        try {

            OutputStreamWriter pueblos = new OutputStreamWriter(openFileOutput("pueblos.xml", Context.BIND_AUTO_CREATE));

            StringBuilder pueblo = new StringBuilder();

        //Construimos el XML

            pueblo.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<resources>\n" +
                "    <string-array name=\"pueblos\">\n" +
                "        <item>Los Palacios</item>\n" +
                "        <item>Utrera</item>\n" +
                "    </string-array>\n" +
                "</resources>");

        //Escribimos el resultado a un fichero

            pueblos.write(pueblo.toString());
            pueblos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

        setContentView(R.layout.activity_main);
        Button boton = (Button)findViewById(R.id.button);
        Button boton2 = (Button)findViewById(R.id.button2);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Filtro.class);
                startActivity(intent);
            }
        });
        boton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
    public static void invokeMiActivity(Activity activity){
        String actionName = "android.intent.action.MAIN";
        Intent intent = new Intent(actionName);
        activity.startActivity(intent);
    }

    //Descargar archivo JSON AsyncTask
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


    private class AsyncRequest extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String ... urls) {

            return new String(ApiPetition(urls[0],urls[1]));
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            try {
                JSONObject json = new JSONObject(result);
                // Creamos una lista de ciudades para el spinner
                listaPueblos = new ArrayList<String>();
                //etResponse.setText(json.toString(1));

                // Nos recorremos con un iterador todos los pueblos de la API
                for(Iterator<String> iter = json.keys();iter.hasNext();){
                    String id_pueblo = iter.next();

                    // Añadimos los pueblos a la lista de pueblos
                    listaPueblos.add(json.getString(id_pueblo));

                }
                etResponse.setText(json.optString("3"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //etResponse.setText(result);
        }
    }

}
