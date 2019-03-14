package com.example.romulo.entregasfcc;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class ConvertirImagen
{
    public Bitmap StringABitmap(String bytes)
    {
        byte[] decodedString = Base64.decode(bytes.getBytes(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,decodedString.length);
        return decodedByte;
    }

    public String BitmapAString(Bitmap bitmap)
    {
        ByteArrayOutputStream byt = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, byt );
        byte[] byteA = byt.toByteArray();
        String foto = Base64.encodeToString( byteA, Base64.DEFAULT );
        return foto;
    }
}
