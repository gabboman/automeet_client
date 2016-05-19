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

    Spinner universidad;
    String universidadString;

    Spinner campus;
    String campusString;

    EditText precio;
    String precioString;

    //Variable para la conexion
    Context ctx=this;







    //Metodos de gabriel para peticion json
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


    //Invocación a la acitividad
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        //Descargar archivo JSON
        //new DescargaJSON().execute();
        pueblo=(Spinner)findViewById(R.id.spinner_pueblos);
        new AsyncRequest().execute("GET", "http://mc.hamburcraft.xyz:5000/pueblos/");
        //new DownloadJSON().execute();

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
                    //BackGround b = new BackGround();
                    //b.execute(nombreString, correoString, telefonoString, contraseñaString, puebloString,
                         //   universidadString,campusString,precioString);

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

    public boolean getPrecio(){
        boolean aux = true;
        if (precioString.isEmpty()){
            Toast.makeText(this, "Indique una cuantía para compartir los gastos del viaje", Toast.LENGTH_LONG).show();
            aux = false;
        }
        return aux;
    }


        //Descargar archivo JSON AsyncTask
        private class DownloadJSON extends AsyncTask<Void, Void, Void> {
            protected Void doInBackground(Void... params) {
                // Locate the WorldPopulation Class
                pueblos = new ArrayList<String>();
                // Create an array to populate the spinner
                listaPueblos = new ArrayList<String>();
                // JSON file URL address
                jsonobject  = JSONfunctions.getJSONfromURL("http://mc.hamburcraft.xyz:5000/pueblos/");


                try {
                    Log.w("automeetjson", jsonobject.toString());
                    jsonarray = jsonobject.getJSONArray("");
                    listaPueblos.add(jsonobject.optString("3"));
                    /*for(int i=3; i<jsonarray.length(); i++){

                        listaPueblos.add(jsonobject.optString(i));
                    }*/
                    // Nos recorremos con un iterador todos los pueblos de la API
                    /*for(Iterator<String> iter = json.keys();iter.hasNext();){
                        String id_pueblo = iter.next();

                        // Añadimos los pueblos a la lista de pueblos
                        listaPueblos.add(json.getString(id_pueblo));

                    }*/
                    /*// Locate the NodeList name
                    jsonarray = jsonobject.getJSONArray("worldpopulation");
                    for (int i = 0; i < jsonarray.length(); i++) {
                        jsonobject = jsonarray.getJSONObject(i);

                        WorldPopulation worldpop = new WorldPopulation();

                        worldpop.setRank(jsonobject.optString("rank"));
                        worldpop.setCountry(jsonobject.optString("country"));
                        worldpop.setPopulation(jsonobject.optString("population"));
                        worldpop.setFlag(jsonobject.optString("flag"));
                        world.add(worldpop);

                        // Populate spinner with country names
                        worldlist.add(jsonobject.optString("country"));

                    }*/
                } catch (Exception e) {
                    //Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
                return null;
            }



            // Creamos el Spinner con los datos de la API
            protected void onPostExecute(Void args) {
                // Locate the spinner in activity_main.xml
                Spinner mySpinner = (Spinner) findViewById(R.id.spinner_pueblos);
                Log.w("automeet","definido pueblos");

                // Spinner adapter
                mySpinner.setAdapter(new ArrayAdapter<String>(Registro.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        listaPueblos));

                // Spinner on item click listener
                mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    //@Override
                    public void onItemSelected(AdapterView<?> arg0,
                                               View arg1, int position, long arg3) {
                        // TODO Auto-generated method stub
                        // Locate the textviews in activity_main.xml
                           /* TextView txtrank = (TextView) findViewById(R.id.rank);
                            TextView txtcountry = (TextView) findViewById(R.id.country);
                            TextView txtpopulation = (TextView) findViewById(R.id.population);

                            // Set the text followed by the position
                            txtrank.setText("Rank : "
                                    + world.get(position).getRank());
                            txtcountry.setText("Country : "
                                    + world.get(position).getCountry());
                            txtpopulation.setText("Population : "
                                    + world.get(position).getPopulation());
                                    */
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                });
            }
        }
}

