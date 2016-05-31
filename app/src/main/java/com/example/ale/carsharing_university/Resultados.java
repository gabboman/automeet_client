package com.example.ale.carsharing_university;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Resultados extends AppCompatActivity {
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados);
        listView = (ListView) findViewById(R.id.list);
        JSONObject json=Global.viajes;
        List<String> datos=new ArrayList<String>();
        for (Iterator<String> viajes = json.keys(); viajes.hasNext();){
            try {
                JSONObject viaje = json.getJSONObject(viajes.next());
                String minutos2 = viaje.getString("minutos");
                String precio = viaje.getString("precio");
                String plazas = viaje.getString("plazas");
                String id_usuario = viaje.getString("id_usuario");
                String telefono = viaje.getString("telefono");
                String detalles = viaje.getString("detalles");
                String EsDeVuelta = viaje.getString("EsDeVuelta");
                String dia2 = viaje.getString("dia");
                String hora2 = viaje.getString("hora");

                String resultado="Salida a las "+hora2+":"+minutos2+", "+precio+" "+telefono;
                datos.add(resultado);

            }catch(Exception e){
                Log.w("Error resultados","Excepcion en el bucle de rellenar");

            }

        }

        String[] valores= new String[datos.size()];
        valores=datos.toArray(valores);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, valores);
        listView.setAdapter(adapter);

    }


}
