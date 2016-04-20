package com.example.gourav.mymovieapp.Misc;

/**
 * Created by Gourav on 2/5/2016.
 */

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public class VolleySingleton {

    private static VolleySingleton instance;
    private RequestQueue requestQueue;


    private VolleySingleton(Context context) {
        requestQueue = Volley.newRequestQueue(context);

    }


    public static VolleySingleton getInstance(Context context) {
        if (instance == null) {
            instance = new VolleySingleton(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }


    public <T> void addToRequestQueue(Request<T> req) {

        getRequestQueue().add(req);
    }

}