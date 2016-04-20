package com.example.gourav.mymovieapp.Fragments;

import android.app.ProgressDialog;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.gourav.mymovieapp.Activities.Favorites;
import com.example.gourav.mymovieapp.AdapterClasses.IndividualAdapter;
import com.example.gourav.mymovieapp.ContentProviderPackage.MovieContract;
import com.example.gourav.mymovieapp.Misc.GlobalVar;
import com.example.gourav.mymovieapp.Misc.Movies;
import com.example.gourav.mymovieapp.Misc.VolleySingleton;
import com.example.gourav.mymovieapp.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Gourav on 3/4/2016.
 */
public class IndiFrag extends Fragment {

    ImageView Backdrop;
    RecyclerView IndiRecycler;
    int position;
    MyQueryHandler mhandler;
    String URL_VIDEO;
    String URL_REVIEW;
    String vidkey;
    ProgressDialog PD;
    int favcounter = 0;
    android.support.v7.widget.ShareActionProvider shareVid;
    SwitchCompat fav;
    Intent shareIntent;
    Toolbar tooly;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = getArguments().getInt("ID");

        setHasOptionsMenu(true);

        Log.i("onCreate()", "yes");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.indifrag, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        tooly = (Toolbar) view.findViewById(R.id.toolbarIndi);

        IndiRecycler = (RecyclerView) view.findViewById(R.id.indirecycler);
        IndiRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        IndiRecycler.setAdapter(new IndividualAdapter(getContext(), position));
        Backdrop = (ImageView) view.findViewById(R.id.mainbackdrop);

        URL_VIDEO = "http://api.themoviedb.org/3/movie/" + Movies.getId(position) + "/videos?api_key="+GlobalVar.API_KEY;
        URL_REVIEW = "http://api.themoviedb.org/3/movie/" + Movies.getId(position) + "/reviews?api_key="+GlobalVar.API_KEY;

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.i("onActivityCreated", "yes");
        super.onActivityCreated(savedInstanceState);

        tooly.setTitle(Movies.getName(position));

        if (!GlobalVar.m2pane) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(tooly);
            ((AppCompatActivity) getActivity()).setSupportActionBar(tooly);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        }


        PD = new ProgressDialog(getContext());
        PD.setTitle("just a sec...");
        PD.setMessage("Downloading some trailer links and reviews...");

        if (Movies.retMovieObj(position).getVidList().size() == 0) {
            Log.i("vid", "yes");
            PD.show();

            callVideoFetch();

        }
        if (Movies.retMovieObj(position).getReviewList().size() == 0) {
            Log.i("review", "yes");
            PD.show();
            callReviewFetch();

        }


        mhandler = new MyQueryHandler(getContext().getContentResolver());


        Picasso.with(getContext()).load("https://image.tmdb.org/t/p/w780" + Movies.getBackDrop(position)).fit().into(Backdrop);

    }

    void callVideoFetch() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                URL_VIDEO, (String) null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            JSONArray jarr = response.getJSONArray("results");
                            if (jarr.length() == 0)
                                PD.dismiss();
                            for (int i = 0; i < jarr.length(); i++) {
                                JSONObject json = jarr.getJSONObject(i);
                                Log.i("vid_put", "yes");
                                Movies.retMovieObj(position).appendVideos(json.getString("id"), json.getString("key"), json.getString("name"));
                                Log.i("vid in movies", "" + Movies.retMovieObj(position).getVidList().size());
                                if (i == 0) {
                                    vidkey = json.getString("key");
                                    getActivity().invalidateOptionsMenu();
                                }


                            }
                        } catch (Exception e) {

                            Log.e("json problem", "" + e);

                        }

                        IndiRecycler.setAdapter(new IndividualAdapter(getContext(), position));


                    }


                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                PD.dismiss();
                Log.e("json error response", "" + error.toString());

            }
        });
        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjReq);
    }

    void callReviewFetch() {
        JsonObjectRequest jsonObjReq1 = new JsonObjectRequest(Request.Method.GET,
                URL_REVIEW, (String) null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            JSONArray jarr = response.getJSONArray("results");
                            for (int i = 0; i < jarr.length(); i++) {

                                JSONObject json = jarr.getJSONObject(i);
                                Movies.retMovieObj(position).appendReviews(json.getString("author"), json.getString("content"));
                            }


                            IndiRecycler.setAdapter(new IndividualAdapter(getContext(), position));
                            PD.dismiss();

                        } catch (Exception e) {

                            Log.e("json problem", "" + e);

                        }


                    }


                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });
        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjReq1);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (!GlobalVar.m2pane && !GlobalVar.indiactivity)
            return;
        inflater.inflate(R.menu.menu_individual, menu);
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem menuItem = menu.findItem(R.id.share);

        shareVid = (android.support.v7.widget.ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        if (((ArrayList<Movies.Videos>) Movies.retMovieObj(position).getVidList()).size() > 0) {
            vidkey = ((ArrayList<Movies.Videos>) (Movies.retMovieObj(position).getVidList())).get(0).getKey();

        }
        if (vidkey != null) {
            shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");

            shareIntent.putExtra(Intent.EXTRA_TEXT, "https://www.youtube.com/watch?v=" + vidkey);
            shareVid.setShareIntent(shareIntent);

        } else {
            shareVid.setShareIntent(null);

        }


        fav = (android.support.v7.widget.SwitchCompat) (menu.findItem(R.id.fav).getActionView());
        fav.setText("Favourites");

        if (getContext().getContentResolver().query(MovieContract.MovieData.CONTENT_URI,
                new String[]{MovieContract.MovieData.MOVIEID},
                MovieContract.MovieData.MOVIEID + "=" + Movies.getId(position), null, null).moveToFirst())
            fav.setChecked(true);


        fav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PD = new ProgressDialog(getContext());

                if (isChecked) {

                    PD.setMessage("Adding Movie to Favourites. Please don't press anything!");
                    PD.show();
                    ContentValues cv = new ContentValues();
                    cv.put(MovieContract.MovieData.MOVIE_NAME, Movies.getName(position));
                    cv.put(MovieContract.MovieData.POSTER_URL, Movies.getPoster(position));
                    cv.put(MovieContract.MovieData.BACKDROP, Movies.getBackDrop(position));
                    cv.put(MovieContract.MovieData.MOVIEID, Movies.getId(position));
                    cv.put(MovieContract.MovieData.PLOT, Movies.getPlot(position));
                    cv.put(MovieContract.MovieData.RELEASE_DATE, Movies.getReleaseDate(position));
                    cv.put(MovieContract.MovieData.RATING, Movies.getRating(position));
                    mhandler.startInsert(favcounter++, null, MovieContract.MovieData.CONTENT_URI, cv);
                    for (Movies.Videos vids : (ArrayList<Movies.Videos>) Movies.retMovieObj(position).getVidList()) {
                        PD.show();
                        ContentValues cv1 = new ContentValues();
                        cv1.put(MovieContract.MovieVideos.MOVIE_ID, Movies.getId(position));
                        cv1.put(MovieContract.MovieVideos.KEY, vids.getKey());
                        cv1.put(MovieContract.MovieVideos.VID, vids.getId());
                        cv1.put(MovieContract.MovieVideos.VNAME, vids.getName());
                        mhandler.startInsert(favcounter++, null, MovieContract.MovieVideos.CONTENT_URI, cv1);
                    }
                    for (Movies.Reviews revs : (ArrayList<Movies.Reviews>) Movies.retMovieObj(position).getReviewList()) {
                        PD.show();
                        ContentValues cv2 = new ContentValues();
                        cv2.put(MovieContract.MovieVideos.MOVIE_ID, Movies.getId(position));
                        cv2.put(MovieContract.MovieReviews.REVIEWS, revs.getContent());
                        cv2.put(MovieContract.MovieReviews.AUTHOR, revs.getAuthor());
                        mhandler.startInsert(favcounter++, null, MovieContract.MovieReviews.CONTENT_URI, cv2);
                    }
                } else {
                    PD.setMessage("Deleting. Please Don't press anything!");
                    PD.show();
                    mhandler.startDelete(favcounter++, null, MovieContract.MovieData.CONTENT_URI, MovieContract.MovieData.MOVIEID + "=" + Movies.getId(position), null);
                    mhandler.startDelete(favcounter++, null, MovieContract.MovieVideos.CONTENT_URI, MovieContract.MovieData.MOVIEID + "=" + Movies.getId(position), null);
                    mhandler.startDelete(favcounter++, null, MovieContract.MovieReviews.CONTENT_URI, MovieContract.MovieData.MOVIEID + "=" + Movies.getId(position), null);
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_tofavourites) {
            startActivity(new Intent(getActivity(), Favorites.class));
            getActivity().finish();
        }
        return true;
    }

    @Override
    public void onResume() {
        if (fav != null && !getContext().getContentResolver().query(MovieContract.MovieData.CONTENT_URI,
                new String[]{MovieContract.MovieData.MOVIEID},
                MovieContract.MovieData.MOVIEID + "=" + Movies.getId(position), null, null).moveToFirst())
            fav.setChecked(false);
        super.onResume();
    }

    public class MyQueryHandler extends AsyncQueryHandler {

        public MyQueryHandler(ContentResolver cr) {
            super(cr);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {

        }


        @Override
        protected void onInsertComplete(int token, Object cookie, Uri uri) {
            if (token == favcounter - 1) {
                PD.dismiss();
                favcounter = 0;
            }
        }

        @Override
        protected void onUpdateComplete(int token, Object cookie, int result) {
        }

        @Override
        protected void onDeleteComplete(int token, Object cookie, int result) {
            if (token == favcounter - 1) {
                PD.dismiss();
                Toast.makeText(getContext(), "Deletion complete, rows affected=" + result, Toast.LENGTH_LONG).show();
                favcounter = 0;
            }
        }
    }

}
