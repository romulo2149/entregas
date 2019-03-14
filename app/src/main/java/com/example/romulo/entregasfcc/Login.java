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
    private String TAG = "MainActivity";
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
                mVolleyService.Login( textoUsuario.getText().toString(), textoPass.getText().toString() );
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
                //Toast.makeText( Login.this, response.toString(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent("com.example.romulo.entregasfcc.Perfil");
                try
                {
                    intent.putExtra("usuario",response.get( "nombre" ).toString());
                    intent.putExtra("imagen",response.get( "foto" ).toString());
                    intent.putExtra("id", response.get("id").toString());
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                startActivity( intent );
            }

            @Override
            public void notifyError(String requestType,VolleyError error)
            {
                Toast.makeText( Login.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        };
    }

}


