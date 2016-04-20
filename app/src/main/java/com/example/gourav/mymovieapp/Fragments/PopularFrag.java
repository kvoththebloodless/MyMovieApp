package com.example.gourav.mymovieapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.gourav.mymovieapp.Activities.Popular;
import com.example.gourav.mymovieapp.AdapterClasses.MyPopularAdapter;
import com.example.gourav.mymovieapp.Interfaces.Communication;
import com.example.gourav.mymovieapp.Misc.GlobalVar;
import com.example.gourav.mymovieapp.Misc.Movies;
import com.example.gourav.mymovieapp.Misc.VolleySingleton;
import com.example.gourav.mymovieapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Gourav on 3/4/2016.
 */
public class PopularFrag extends Fragment implements AdapterView.OnItemSelectedListener {
    RecyclerView Recycler;
    Communication op;
    android.support.v7.widget.AppCompatSpinner spinner;
    int Spinner_Position;
    boolean JUST_INITIALIZED;
    String TYPE_POPULAR = "popular", TYPE_TOP_RATED = "top_rated";

    String URLS[] = {"http://api.themoviedb.org/3/movie/popular?api_key="+GlobalVar.API_KEY, "http://api.themoviedb.org/3/movie/top_rated?api_key="+GlobalVar.API_KEY};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.popularfrag, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            Spinner_Position = savedInstanceState.getInt("spinner_choice");
            Log.i("oncreate spinner_pos", savedInstanceState.getInt("spinner_choice") + "");
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Recycler = (RecyclerView) view.findViewById(R.id.recycler);
        Recycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        Recycler.setAdapter(new MyPopularAdapter(getContext(), op));
    }

    public void callVolley(int id, final String type) {
        Recycler.setAdapter(null);
        Movies.reset();
        try {

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    URLS[id], (String) null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("App", response.toString());
                            try {

                                JSONArray jarr = response.getJSONArray("results");
                                for (int i = 0; i < jarr.length(); i++) {
                                    JSONObject json = jarr.getJSONObject(i);
                                    Movies temp1 = new Movies(json.getString("original_title"), json.getInt("id") + "",
                                            json.getString("overview"), json.getString("backdrop_path"), json.getString("vote_average"),
                                            json.getString("release_date"), json.getString("poster_path"), type);
                                    Movies.addShow(temp1);
                                }

                                //Recycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
                                Recycler.setAdapter(new MyPopularAdapter(getContext(), op));//unwillingly passing activity's instance here


                            } catch (Exception e) {

                                Log.e("json problem", "" + e);
                            }


                        }


                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Volley error", error.toString());

                }
            });
            VolleySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjReq);
        } catch (Exception e) {
            Log.e("call volley exception", e + "");

        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putInt("spinner_choice", Spinner_Position);


        super.onSaveInstanceState(outState);

        Log.i("onSaveInstance", "item " + spinner.getSelectedItem().toString());

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (!GlobalVar.m2pane)
            menu.clear();
        inflater.inflate(R.menu.menu_popular, menu);


        spinner = (android.support.v7.widget.AppCompatSpinner) (menu.findItem(R.id.spinner).getActionView());
        ArrayAdapter<CharSequence> SpinnerAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.Spinner_choice, android.R.layout.simple_list_item_1);
        SpinnerAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinner.setOnItemSelectedListener(this);
        spinner.setAdapter(SpinnerAdapter);
        Log.i("onCreateOptionsMenu ", Spinner_Position + "");
        spinner.setSelection(Spinner_Position);
        JUST_INITIALIZED = false;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (JUST_INITIALIZED)
            op.deleteDetailFragment();

        Recycler.setAdapter(null);


        switch (
                position
                ) {
            case 0:
                if (Movies.retMovieTypeCount(TYPE_POPULAR) > 0)
                    Recycler.setAdapter(new MyPopularAdapter(getContext(), op));
                else
                    callVolley(position, TYPE_POPULAR);
                break;
            case 1:
                if (Movies.retMovieTypeCount(TYPE_TOP_RATED) > 0)
                    Recycler.setAdapter(new MyPopularAdapter(getContext(), op));
                else
                    callVolley(position, TYPE_TOP_RATED);
                break;


        }

        Spinner_Position = position;
        JUST_INITIALIZED = true;
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        op = ((Popular) context);
    }
}
