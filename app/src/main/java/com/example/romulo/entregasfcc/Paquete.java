package com.example.romulo.entregasfcc;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class Paquete extends AppCompatActivity implements LocationListener
{
    EditText date;
    EditText fecha,desc,entregado;
    DatePickerDialog datePickerDialog;
    Button botonEn, botonOpt;
    IResult mResultCallback = null;
    VolleyService mVolleyService;
    TableLayout tl;
    RadioButton r1, r2, r3;
    LocationManager locationManager;
    String lon,lat;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_paquete );
        initVolleyCallback();
        final Bundle extras = getIntent().getExtras();
        mVolleyService = new VolleyService(mResultCallback,this);
        date = (EditText) findViewById(R.id.date);
        botonEn = findViewById( R.id.botonEnviar );
        botonOpt = findViewById(R.id.optBoton);
        date.setInputType(InputType.TYPE_NULL);
        tl = findViewById( R.id.tablaPadre );
        r1 = findViewById( R.id.radio_entregado );
        r2 = findViewById( R.id.radio_pendiente );
        r3 = findViewById( R.id.radio_rech );
        CheckPermission();
        // perform click event on edit text
        //final Bundle extras = getIntent().getExtras();.

        date.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                datePickerDialog = new DatePickerDialog(Paquete.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                    {
                        date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        botonOpt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    //Toast.makeText( Paquete.this, pref.getString("pedidos", "nada"), Toast.LENGTH_LONG).show();
                    if(pref.contains("pedidos"))
                    {
                        getLocation();
                        editor.putString("latitud", lat);
                        editor.putString("longitud", lon);
                        editor.apply();
                        Intent intent = new Intent("com.example.romulo.entregasfcc.Ruta");
                        startActivity(intent);
                    }

            }
        });

        botonEn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String fecha = date.getText().toString();
                String estatus;
                if(r1.isChecked())
                {
                    estatus = "si";
                    mVolleyService.verPedidos(estatus,fecha);
                }
                else if(r2.isChecked())
                {
                    estatus = "pendiente";
                    mVolleyService.verPedidos(estatus,fecha);
                }
                else if(r3.isChecked())
                {
                    estatus = "no";
                    mVolleyService.verPedidos(estatus,fecha);
                }
                else
                {
                    Toast.makeText( Paquete.this, "Selecciona una opción", Toast.LENGTH_LONG).show();
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
                final Bundle extras = getIntent().getExtras();
                JSONArray arreglo = null;
                String respuesta = "";
                try {
                    arreglo = response.getJSONArray( "pedidos" );
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                    final SharedPreferences.Editor editor = pref.edit();
                    editor.putString( "pedidos", arreglo.toString() );
                    editor.apply();
                    respuesta = arreglo.getJSONObject( 1 ).getString( "direccion" );
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                try
                {
                    tl.removeAllViews();
                    for(int i = 0; i < arreglo.length(); i++)
                    {
                        TableRow tr = new TableRow( Paquete.this );
                        tr.setLayoutParams( new TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT ) );
                        TextView et1 = new TextView( Paquete.this );
                        TextView et2 = new TextView( Paquete.this );
                      //  TextView et3 = new TextView( Paquete.this );
                        et1.setLayoutParams( new TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT )  );
                        et2.setLayoutParams( new TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT )  );
                        et1.setTextColor( Color.RED );
                        et2.setTextColor( Color.BLACK );
                      //  et3.setLayoutParams( new TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT )  );
                        et1.setText( arreglo.getJSONObject( i ).getString( "fecha_entrega" ) );
                        et2.setText( arreglo.getJSONObject( i ).getString("direccion") );
                        //et3.setText( "hola" );
                        tr.addView( et1, new TableRow.LayoutParams( TableRow.LayoutParams.WRAP_CONTENT, 190 ) );
                        tr.addView( et2, new TableRow.LayoutParams( TableRow.LayoutParams.WRAP_CONTENT, 190 ) );
                      //  tr.addView( et3, new TableRow.LayoutParams( TableRow.LayoutParams.WRAP_CONTENT, 150 ) );
                        setOnClick( tr, arreglo.getJSONObject( i ).getString("id"), extras.getString( "id_usuario" ));
                        tl.addView( tr,i );
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }

            @Override
            public void notifyError(String requestType,VolleyError error)
            {
                Toast.makeText( Paquete.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        };
    }

    public void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void setOnClick(final TableRow btn, final String id, final String id_user)
    {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent( "com.example.romulo.entregasfcc.Enviar" );
                intent.putExtra( "id", id );
                intent.putExtra("id_usuario", id_user);
                startActivity( intent );
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        getLocation();
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }
    public void CheckPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        lon = String.valueOf(location.getLongitude());
        lat = String.valueOf(location.getLatitude());
        //Toast.makeText( Paquete.this, lat+" "+lon, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
