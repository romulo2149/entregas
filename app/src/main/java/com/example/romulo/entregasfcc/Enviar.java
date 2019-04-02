package com.example.romulo.entregasfcc;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.github.gcacace.signaturepad.views.SignaturePad;
import org.json.JSONException;
import org.json.JSONObject;

public class Enviar extends AppCompatActivity {
    LinearLayout l1, l2, l3, l4, l5, l6, l7, l8;
    TextView tv1, tv2, tv3;
    EditText et;
    ImageView iv1, iv2, iv3, iv4;
    CheckBox cb;
    Spinner sp;
    IResult mResultCallback = null;
    IResult eResultCallback = null;
    VolleyService mVolleyService;
    VolleyService eVolleyService;
    String frente64 = Defecto.frente64;
    String atras64 = Defecto.atras64;
    String paquete64 = Defecto.paquete64;
    ConvertirImagen con = new ConvertirImagen();
    SignaturePad pad;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_enviar );
        initVolleyCallback();
        initVolleyCallbackEnviar();
        final Bundle extras = getIntent().getExtras();
        mVolleyService = new VolleyService( mResultCallback, this );
        eVolleyService = new VolleyService( eResultCallback, this );
        mVolleyService.unPedido( extras.getString( "id" ) );
        tv1 = findViewById( R.id.textDescripcion );
        tv2 = findViewById( R.id.textDireccion );
        iv1 = findViewById( R.id.imagenFrente );
        iv2 = findViewById( R.id.imagenAtras );
        iv3 = findViewById( R.id.imagenPaquete );
        l1 = findViewById( R.id.layoutPaq );
        l2 = findViewById( R.id.layoutFrente );
        l3 = findViewById( R.id.layoutAtras );
        l4 = findViewById( R.id.layoutPad );
        l5 = findViewById( R.id.layoutBoton );
        l6 = findViewById( R.id.layoutSpinner );
        l7 = findViewById( R.id.layouttexto2 );
        l8 = findViewById( R.id.layoutRechazado );
        et = findViewById( R.id.textob );
    }

    void initVolleyCallback() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                int contador = 0;
                try {
                    tv1.setTextColor( Color.BLACK );
                    tv2.setTextColor( Color.BLACK );
                    tv1.setTextSize( 14 );
                    tv2.setTextSize( 14 );
                    tv1.setText( response.getString( "descripcion" ) );
                    tv2.setText( response.getString( "direccion" ) );
                    if (response.getString( "imagen_paquete" ).length() > 15)
                    {
                        iv3.setImageBitmap( con.StringABitmap( response.getString( "imagen_paquete" ) ) );
                    }
                    else
                    {
                        contador++;
                        Button b1 = new Button( Enviar.this );
                        b1.setText( "Tomar foto de paquete" );
                        setOnClick( b1, 0 );
                        iv3.setImageBitmap( con.StringABitmap( paquete64 ) );
                        l1.addView( b1 );
                    }
                    if (response.getString( "id_frente" ).length() > 15)
                    {
                        iv1.setImageBitmap( con.StringABitmap( response.getString( "id_frente" ) ) );
                    }
                    else
                    {
                        contador++;
                        Button b1 = new Button( Enviar.this );
                        b1.setText( "Tomar foto de id por adelante" );
                        setOnClick( b1 ,1);
                        iv1.setImageBitmap( con.StringABitmap( frente64 ) );
                        l2.addView( b1 );
                    }
                    if (response.getString( "id_atras" ).length() > 15)
                    {
                        iv2.setImageBitmap( con.StringABitmap( response.getString( "id_atras" ) ) );
                    }
                    else
                    {
                        contador++;
                        Button b1 = new Button( Enviar.this );
                        b1.setText( "Tomar foto de id por atras" );
                        setOnClick( b1 , 2);
                        iv2.setImageBitmap( con.StringABitmap( atras64 ) );
                        l3.addView( b1 );
                    }
                    if (response.getString( "firma" ).length() > 15)
                    {
                        iv4 = new ImageView( Enviar.this );
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams( 700,700 );
                        layoutParams.gravity = Gravity.CENTER;
                        iv4.setImageBitmap( con.StringABitmap( response.getString( "firma" ) ) );
                        iv4.setLayoutParams( layoutParams );
                        l4.addView( iv4 );

                    }
                    else
                    {

                        pad = new SignaturePad(Enviar.this, null );
                        l4.addView( pad );
                    }
                    if (response.getString( "parentezco" ).length() > 15)
                    {
                        et.setVisibility( View.GONE );
                        tv3 = new TextView( Enviar.this );
                        LinearLayout.LayoutParams layoutParams;
                        layoutParams = new LinearLayout.LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT,90 );
                        layoutParams.gravity = Gravity.CENTER;
                        tv3.setText(response.getString( "parentezco" )  );
                        tv3.setLayoutParams( layoutParams );
                        tv3.setTextSize( 14 );
                        tv3.setTextColor( Color.BLACK );
                        l7.addView( tv3);

                    }
                    else
                    {
                       // LinearLayout.LayoutParams layoutParams;
                        //layoutParams = new LinearLayout.LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT,50 );

                    }
                    if(contador > 0)
                    {
                        cb = new CheckBox( Enviar.this );
                        cb.setText( "No se entregó el paquete" );
                        final Bundle extras = getIntent().getExtras();
                        Button b1 = new Button( Enviar.this );
                        b1.setText( "Enviar Cambios" );
                        setOnEnviar( b1 , extras.getString( "id_usuario" ) , response.getString( "id" ));
                        l5.addView( b1 );
                        l8.addView( cb );
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                Toast.makeText( Enviar.this, error.toString(), Toast.LENGTH_LONG ).show();
            }
        };
    }

    private void setOnClick(final Button btn, final int opc) {
        btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
                startActivityForResult( intent, opc );
            }
        } );
    }

    private void setOnEnviar(final Button btn, final String id_user, final String id) {
        btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb.isChecked())
                {
                    eVolleyService.rechazado(id);
                }
                else
                {
                    et = findViewById( R.id.textob );
                    Bitmap bitmap = pad.getSignatureBitmap();
                    String paq = con.BitmapAString( ((BitmapDrawable) iv3.getDrawable()).getBitmap() );
                    String frente = con.BitmapAString( ((BitmapDrawable) iv1.getDrawable()).getBitmap() );
                    String atras = con.BitmapAString( ((BitmapDrawable) iv2.getDrawable()).getBitmap() );
                    String firma = con.BitmapAString( bitmap );
                    String paren = et.getText().toString();
                    eVolleyService.editarPedido( id, paq, frente, atras, firma, id_user, "si", paren );
                }
            }
        } );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        ImageView iv;
        Bitmap bitmap;
        switch (requestCode) {
            case 0:
                bitmap = (Bitmap) data.getExtras().get( "data" );
                iv = findViewById( R.id.imagenPaquete );
                iv.setImageBitmap( bitmap );
                break;
            case 1:
                bitmap = (Bitmap) data.getExtras().get( "data" );
                iv = findViewById( R.id.imagenFrente );
                iv.setImageBitmap( bitmap );
                break;
            case 2:
                bitmap = (Bitmap) data.getExtras().get( "data" );
                iv = findViewById( R.id.imagenAtras );
                iv.setImageBitmap( bitmap );
                break;
        }

    }

    void initVolleyCallbackEnviar()
    {
        eResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,JSONObject response)
            {
                Toast.makeText( Enviar.this, response.toString(), Toast.LENGTH_LONG ).show();
                Intent intent = new Intent("com.example.romulo.entregasfcc.Exito");
                final Bundle extras = getIntent().getExtras();
                intent.putExtra( "id_usuario" , extras.getString( "id_usuario" ));
                startActivity( intent );
            }

            @Override
            public void notifyError(String requestType,VolleyError error)
            {
                Toast.makeText( Enviar.this, error.toString(), Toast.LENGTH_LONG ).show();
            }
        };
    }
}
