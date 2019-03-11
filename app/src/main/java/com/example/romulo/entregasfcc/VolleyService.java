package com.example.romulo.entregasfcc;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class VolleyService
{

    IResult mResultCallback = null;
    Context mContext;

    VolleyService(IResult resultCallback, Context context)
    {
        mResultCallback = resultCallback;
        mContext = context;
    }


    public void Login(final String usuario, String pass)
    {
        String url = "http://10.0.2.2:8000/api/loginUsuario";
        JSONObject json = new JSONObject();
        try
        {
            json.put("correo",usuario);
            json.put("pass",pass);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if(mResultCallback != null)
                        mResultCallback.notifySuccess("POST", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(mResultCallback != null)
                        mResultCallback.notifyError("POST", error);
                }
            });

            queue.add(jsonObj);

        }
        catch(Exception e)
        {

        }
    }

    public void CambiarFoto(String id, String foto)
    {
        String url = "http://10.0.2.2:8000/api/cambiarFoto";
        JSONObject json = new JSONObject();
        try
        {
            json.put("id",id);
            json.put("foto",foto);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.PUT, url, json, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if(mResultCallback != null)
                        mResultCallback.notifySuccess("POST", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(mResultCallback != null)
                        mResultCallback.notifyError("POST", error);
                }
            });

            queue.add(jsonObj);

        }
        catch(Exception e)
        {

        }
    }

    public void verPedidos()
    {
        String url = "http://10.0.2.2:8000/api/verPedidos";

        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if(mResultCallback != null)
                        mResultCallback.notifySuccess("GET", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(mResultCallback != null)
                        mResultCallback.notifyError("GET", error);
                }
            });

            queue.add(jsonObj);

        }
        catch(Exception e)
        {

        }
    }
}