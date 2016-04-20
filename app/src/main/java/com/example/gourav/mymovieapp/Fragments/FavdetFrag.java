package com.example.gourav.mymovieapp.Fragments;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.gourav.mymovieapp.AdapterClasses.FavIndiAdapter;
import com.example.gourav.mymovieapp.ContentProviderPackage.MovieContract;
import com.example.gourav.mymovieapp.Interfaces.Communication;
import com.example.gourav.mymovieapp.Misc.GlobalVar;
import com.example.gourav.mymovieapp.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Gourav on 3/10/2016.
 */
public class FavdetFrag extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {
    RecyclerView IndiRecycler;
    int ID;
    ImageView Backdrop;
    Cursor general, videos, reviews;
    Toolbar tooly;
    android.support.v7.widget.ShareActionProvider shareVid;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ID = getArguments().getInt("MovieId");

        getActivity().getSupportLoaderManager().initLoader(1, null, this);
        getActivity().getSupportLoaderManager().initLoader(2, null, this);
        getActivity().getSupportLoaderManager().initLoader(3, null, this);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.indifrag, container, false);
    }

    @Override
    public void onPause() {
        if (!GlobalVar.fav2pane && !GlobalVar.favdetactivity) {
            ((Communication) (getActivity())).deleteDetailFragment();
            getActivity().getSupportLoaderManager().destroyLoader(1);
            getActivity().getSupportLoaderManager().destroyLoader(2);
            getActivity().getSupportLoaderManager().destroyLoader(3);
        }
        super.onPause();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        IndiRecycler = (RecyclerView) view.findViewById(R.id.indirecycler);
        Backdrop = (ImageView) view.findViewById(R.id.mainbackdrop);
        tooly = (Toolbar) view.findViewById(R.id.toolbarIndi);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//            if (GlobalVar.m2pane)
//                tooly.setVisibility(View.GONE);
////            else
        IndiRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        IndiRecycler.setAdapter(new FavIndiAdapter(getContext(), ID, general, videos, reviews));
        if (!GlobalVar.fav2pane) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(tooly);
            ((AppCompatActivity) getActivity()).setSupportActionBar(tooly);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (!GlobalVar.fav2pane && !GlobalVar.favdetactivity)
            return;
        inflater.inflate(R.menu.menu_favdetail, menu);
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem menuItem = menu.findItem(R.id.share);
        shareVid = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");

        Cursor c = getContext().getContentResolver().query(MovieContract.MovieVideos.CONTENT_URI, null, MovieContract.MovieVideos.MOVIE_ID + "=" + ID, null, null);
        if (c != null && c.moveToFirst()) {
            String key = c.getString(c.getColumnIndex(MovieContract.MovieVideos.KEY));
            shareIntent.putExtra(Intent.EXTRA_TEXT, "https://www.youtube.com/watch?v=" + key);
            shareVid.setShareIntent(shareIntent);
        } else {
            shareVid.setShareIntent(null);

        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case 1:
                return new CursorLoader(
                        getContext(),
                        MovieContract.MovieData.CONTENT_URI,
                        null,
                        MovieContract.MovieData.MOVIEID + "=" + ID,
                        null,
                        null
                );
            case 2:
                return new CursorLoader(getContext(), MovieContract.MovieVideos.CONTENT_URI
                        , null, MovieContract.MovieVideos.MOVIE_ID + "=" + ID,
                        null,
                        null);

            case 3:
                return new CursorLoader(getContext(), MovieContract.MovieReviews.CONTENT_URI, null
                        , MovieContract.MovieReviews.MOVIE_ID + "=" + ID, null, null);
            default:
                return null;
        }

    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        switch (loader.getId()) {
            case 1:

                data.moveToFirst();

                Picasso.with(getContext()).load("https://image.tmdb.org/t/p/w780" + data.getString(data.getColumnIndex(MovieContract.MovieData.BACKDROP))).fit().into(Backdrop);
                general = data;
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(general.getString(general.getColumnIndex(MovieContract.MovieData.MOVIE_NAME)));
                break;
            case 2:
                videos = data;

                break;
            case 3:
                reviews = data;


                break;

        }

        IndiRecycler.setAdapter(new FavIndiAdapter(getContext(), ID, general, videos, reviews));

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        IndiRecycler.setAdapter(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.remove:

                ContentResolver cr = getContext().getContentResolver();
                int deleted = cr.delete(MovieContract.MovieData.CONTENT_URI, MovieContract.MovieData.MOVIEID + "=" + ID, null);
                deleted += cr.delete(MovieContract.MovieVideos.CONTENT_URI, MovieContract.MovieData.MOVIEID + "=" + ID, null) +
                        cr.delete(MovieContract.MovieReviews.CONTENT_URI, MovieContract.MovieData.MOVIEID + "=" + ID, null);

                Toast.makeText(getContext(), "No of rows deleted from the 3 tables= " + deleted, Toast.LENGTH_SHORT).show();
                getActivity().getSupportLoaderManager().destroyLoader(1);
                getActivity().getSupportLoaderManager().destroyLoader(2);
                getActivity().getSupportLoaderManager().destroyLoader(3);
                if (GlobalVar.fav2pane)
                    ((Communication) getActivity()).deleteDetailFragment();
                else
                    getActivity().finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
