package com.example.romulo.entregasfcc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class Ruta extends AppCompatActivity
{
    String urlpart = "";
    IResult mResultCallback = null;
    VolleyService mVolleyService;
    TableLayout t1, t2;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruta);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        initVolleyCallback();
        t1 = findViewById(R.id.tablaOpt);
        t2 = findViewById(R.id.tablaOpt2);
        final Bundle extras = getIntent().getExtras();
        mVolleyService = new VolleyService(mResultCallback,this);
        try {
            JSONArray arreglo = new JSONArray(pref.getString("pedidos", "nada"));
            for(int i = 0; i < arreglo.length(); i++)
            {
                urlpart = urlpart + "|" + arreglo.getJSONObject( i ).getString("direccion");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mVolleyService.verRuta(urlpart, pref.getString("latitud", "nada"), pref.getString("longitud", "nada"));

    }

    void initVolleyCallback()
    {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) throws JSONException {

                JSONObject js = new JSONObject(response.getJSONArray("routes").get(0).toString());
                Toast.makeText( Ruta.this, js.getJSONArray("waypoint_order").toString(), Toast.LENGTH_LONG).show();
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                JSONArray arreglo = new JSONArray(pref.getString("pedidos", "nada"));
                JSONArray arreglo2 = new JSONArray(js.getJSONArray("waypoint_order").toString());
                String arr[] = new String[20];
                for(int i = 0; i < arreglo.length(); i++)
                {
                    arr[i] = arreglo.getJSONObject( i ).getString("direccion");
                }
                for(int i = 0; i < arreglo2.length(); i++)
                {
                    TableRow tr = new TableRow( Ruta.this );
                    tr.setLayoutParams( new TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT ) );
                    TextView et1 = new TextView( Ruta.this );
                    TextView et2 = new TextView( Ruta.this );
                    //  TextView et3 = new TextView( Paquete.this );
                    et1.setLayoutParams( new TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT )  );
                    et2.setLayoutParams( new TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT )  );
                    et1.setTextColor( Color.RED );
                    et2.setTextColor( Color.BLACK );
                    //  et3.setLayoutParams( new TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT )  );
                    et1.setText( Integer.toString(i+1) );
                    et2.setText( arr[arreglo2.getInt(i)] );
                    //et3.setText( "hola" );
                    tr.addView( et1, new TableRow.LayoutParams( TableRow.LayoutParams.WRAP_CONTENT, 190 ) );
                    tr.addView( et2, new TableRow.LayoutParams( TableRow.LayoutParams.WRAP_CONTENT, 190 ) );
                    //  tr.addView( et3, new TableRow.LayoutParams( TableRow.LayoutParams.WRAP_CONTENT, 150 ) );
                    t2.addView( tr,i );
                }
            }

            @Override
            public void notifyError(String requestType, VolleyError error)
            {
                Toast.makeText( Ruta.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        };
    }
}
