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
import org.json.JSONArray;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    JSONObject jsonobject;
    JSONArray jsonarray;
    ProgressDialog mProgressDialog;
    ArrayList<String> listaPueblos;
    //ArrayLIst<Pueblo> pueblo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Descargar archivo JSON
        new DescargaJSON().execute();

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

    private //Descargar archivo JSON AsyncTask
    class DescargaJSON extends AsyncTask<Void, Void, Void>{
        protected Void doInBackground(Void... params){
            // Locate the Pueblo Class
            //pueblo = new ArrayList<Pueblo>();
            // Create an array to populate the spinner
            listaPueblos = new ArrayList<String>();
            // JSON file URL address
            jsonobject = JSONfunctions.getJSONfromURL("http://mc.hamburcraft.xyz:5000/pueblos/");

            try {
                // Locate the NodeList name
                for (int i = 0; i <= 179; i++) {
                    //jsonarray = jsonobject.getJSONArray(i);
                }

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;

        }
    }
}
