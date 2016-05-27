package com.example.ale.carsharing_university;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MensajeConfirmacion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensaje_confirmacion);

        // Creamos la acción para que al precionar el boton volvamos al inico
        Button boton7 = (Button)findViewById(R.id.button7);
        boton7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MensajeConfirmacion.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Creamos la acción para que al precionar el boton volvamos al inicio
        Button boton9 = (Button)findViewById(R.id.button9);
        boton9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MensajeConfirmacion.this, PlaningTransport.class);
                startActivity(intent);
            }
        });
    }
}
