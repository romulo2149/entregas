package com.example.romulo.entregasfcc;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class VolleyService
{

    IResult mResultCallback = null;
    Context mContext;
    final public String dir = "192.168.1.71";
    //final public String dir = "192.168.43.113";
    //final public String dir = "172.31.5.158";

    VolleyService(IResult resultCallback, Context context)
    {
        mResultCallback = resultCallback;
        mContext = context;
    }


    public void Login(final String usuario, String pass)
    {
        String url = "http://"+dir+":8000/api/loginUsuario";
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
                    if(mResultCallback != null) {
                        try {
                            mResultCallback.notifySuccess("POST", response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
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
        String url = "http://"+dir+":8000/api/cambiarFoto";
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
                    if(mResultCallback != null) {
                        try {
                            mResultCallback.notifySuccess("POST", response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
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

    public void verPedidos(String estatus, String fecha)
    {
        if(fecha.length()<4)
        {
            fecha = "todos";
        }
        String url = "http://"+dir+":8000/api/verPedidos/"+estatus+"/"+fecha;

        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if(mResultCallback != null) {
                        try {
                            mResultCallback.notifySuccess("GET", response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
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

    public void verRuta(String urlparte, String lat, String lon)
    {
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=19.0001543,-98.18490077&destination=19.0001543,-98.18490077&waypoints=optimize:true"+urlparte+"&key=AIzaSyCVwIuBVBfrIzSxWJthR5BTi1e2uerXAUE";

        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if(mResultCallback != null) {
                        try {
                            mResultCallback.notifySuccess("GET", response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
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

    public void unPedido(String id)
    {
        String url = "http://"+dir+":8000/api/mostrarPedido/"+id;

        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if(mResultCallback != null) {
                        try {
                            mResultCallback.notifySuccess("GET", response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
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

    public void editarPedido(String id, String paquete, String frente, String atras, String firma, String repartidor, String estatus, String parentezco)
    {
        String url = "http://"+dir+":8000/api/editarPedido";
        JSONObject json = new JSONObject();
        try
        {
            json.put("id",id);
            json.put("paquete",paquete);
            json.put("frente",frente);
            json.put("atras",atras);
            json.put("firma",firma);
            json.put("repartidor",repartidor);
            json.put("estatus",estatus);
            json.put("parentezco", parentezco);
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
                    if(mResultCallback != null) {
                        try {
                            mResultCallback.notifySuccess("POST", response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
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

    public void rechazado(String id)
    {
        String url = "http://"+dir+":8000/api/rechazarPedido";
        JSONObject json = new JSONObject();
        try
        {
            json.put("id",id);
            json.put("estatus","no");
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
                    if(mResultCallback != null) {
                        try {
                            mResultCallback.notifySuccess("POST", response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
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
}