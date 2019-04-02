package com.example.romulo.entregasfcc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import org.json.JSONException;
import org.json.JSONObject;


public class Login extends AppCompatActivity
{
    Button botonEntrar;
    TextView textoUsuario;
    TextView textoPass;
    IResult mResultCallback = null;
    VolleyService mVolleyService;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );
        botonEntrar = findViewById( R.id.botonEntrar );
        textoUsuario = findViewById( R.id.textUsuario );
        textoPass = findViewById( R.id.textPassword );
        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallback,this);
        botonEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textoUsuario.getText().toString() == "" || textoPass.getText().toString() == "")
                {
                    Toast.makeText( Login.this, "Completa los campos", Toast.LENGTH_LONG).show();
                }
                else
                {
                    mVolleyService.Login( textoUsuario.getText().toString(), textoPass.getText().toString() );
                }
            }
        });
    }

    void initVolleyCallback()
    {
        mResultCallback = new IResult()
        {
            @Override
            public void notifySuccess(String requestType,JSONObject response)
            {
                try {
                    if (response.getBoolean( "login" ) == true) {
                        Intent intent = new Intent( "com.example.romulo.entregasfcc.Perfil" );

                        intent.putExtra( "usuario", response.get( "nombre" ).toString() );
                        intent.putExtra( "imagen", response.get( "foto" ).toString() );
                        intent.putExtra( "id", response.get( "id" ).toString() );

                        startActivity( intent );
                    } else {
                        Toast.makeText( Login.this, "Datos incorrectos", Toast.LENGTH_LONG ).show();
                    }
                }
                catch (JSONException e) {
                        e.printStackTrace();
                    }
            }

            @Override
            public void notifyError(String requestType,VolleyError error)
            {
                Toast.makeText( Login.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        };
    }

}


