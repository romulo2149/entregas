package com.example.romulo.entregasfcc;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class Perfil extends AppCompatActivity
{
    Button bt1,bt2,bt3;
    TextView tv,tv2;
    ImageView iv;
    IResult mResultCallback = null;
    VolleyService mVolleyService;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_perfil );
        bt1 = (Button) findViewById( R.id.btn1 );
        bt2 = (Button) findViewById( R.id.btn2 );
        bt3 = (Button) findViewById( R.id.btn3 );
        tv = (TextView) findViewById( R.id.tv1 );
        tv2 = (TextView) findViewById( R.id.tv2 );
        iv = (ImageView) findViewById( R.id.iv1 );
        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallback,this);
        final Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            String value = extras.getString("usuario");
            String value2 = extras.getString( "imagen" );
            String id = extras.getString( "id" );
            value2 = value2.replace("dataimage/jpegbase64","");
            tv.setText( value);
            tv2.setText( id );
            byte[] decodedString = Base64.decode(value2.getBytes(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,decodedString.length);
            iv.setImageBitmap(decodedByte);
        }

        bt1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra( "id", extras.getString( "id" ) );
                startActivityForResult( intent,0 );
            }
        });

        bt2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Toast.makeText( MainActivity.this, "Hola Mundo", Toast.LENGTH_LONG).show();
                ByteArrayOutputStream byt = new ByteArrayOutputStream(  );
                Bitmap bitmap = ((BitmapDrawable)iv.getDrawable()).getBitmap();
                bitmap.compress( Bitmap.CompressFormat.PNG, 100, byt );
                byte[] byteA = byt.toByteArray();
                String foto = Base64.encodeToString( byteA, Base64.DEFAULT );
                mVolleyService.CambiarFoto( extras.getString("id"), foto );
            }
        });

        bt3.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent( "com.example.romulo.entregasfcc.Paquete" );
                startActivity( intent );
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult( requestCode, resultCode, data );
        Bitmap bitmap = (Bitmap)data.getExtras().get("data");
        iv.setImageBitmap( bitmap );
    }

    void initVolleyCallback()
    {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,JSONObject response)
            {
                try {
                    Toast.makeText( Perfil.this, response.getString( "mensaje" ), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(String requestType,VolleyError error)
            {
                Toast.makeText( Perfil.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        };
    }
}
