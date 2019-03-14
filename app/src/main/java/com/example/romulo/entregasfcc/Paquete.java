package com.example.romulo.entregasfcc;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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

public class Paquete extends AppCompatActivity
{
    EditText date;
    EditText fecha,desc,entregado;
    DatePickerDialog datePickerDialog;
    Button botonEn;
    IResult mResultCallback = null;
    VolleyService mVolleyService;
    TableLayout tl;
    RadioButton r1, r2;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_paquete );
        initVolleyCallback();
        final Bundle extras = getIntent().getExtras();
        mVolleyService = new VolleyService(mResultCallback,this);
        date = (EditText) findViewById(R.id.date);
        botonEn = findViewById( R.id.botonEnviar );
        date.setInputType(InputType.TYPE_NULL);
        tl = findViewById( R.id.tablaPadre );
        r1 = findViewById( R.id.radio_entregado );
        r2 = findViewById( R.id.radio_pendiente );
        // perform click event on edit text
        //final Bundle extras = getIntent().getExtras();
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
                        TextView et3 = new TextView( Paquete.this );
                        et1.setLayoutParams( new TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT )  );
                        et2.setLayoutParams( new TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT )  );
                        et3.setLayoutParams( new TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT )  );
                        et1.setText( arreglo.getJSONObject( i ).getString( "fecha_entrega" ) );
                        et2.setText( arreglo.getJSONObject( i ).getString("direccion") );
                        et3.setText( "hola" );
                        tr.addView( et1, new TableRow.LayoutParams( TableRow.LayoutParams.WRAP_CONTENT, 150 ) );
                        tr.addView( et2, new TableRow.LayoutParams( TableRow.LayoutParams.WRAP_CONTENT, 150 ) );
                        tr.addView( et3, new TableRow.LayoutParams( TableRow.LayoutParams.WRAP_CONTENT, 150 ) );
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


}
