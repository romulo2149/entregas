package com.example.romulo.entregasfcc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Exito extends AppCompatActivity
{
    Button b1;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_exito );
        b1 = findViewById( R.id.botonVolver );



        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent("com.example.romulo.entregasfcc.Paquete");
                final Bundle extras = getIntent().getExtras();
                intent.putExtra( "id_usuario" , extras.getString( "id_usuario" ));
                startActivity( intent );
            }
        });

    }
}
