package com.example.gourav.mymovieapp.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.gourav.mymovieapp.Activities.Favorites;
import com.example.gourav.mymovieapp.AdapterClasses.MyFavouritesAdapter;
import com.example.gourav.mymovieapp.ContentProviderPackage.MovieContract;
import com.example.gourav.mymovieapp.Interfaces.Communication;
import com.example.gourav.mymovieapp.Misc.GlobalVar;
import com.example.gourav.mymovieapp.R;

/**
 * Created by Gourav on 3/10/2016.
 */
public class FavouriteFrag extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {
    RecyclerView Recycler;
    Communication op;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.popularfrag, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Recycler = (RecyclerView) view.findViewById(R.id.recycler);
        Recycler.setLayoutManager(new GridLayoutManager(getContext(), 2));

    }

    @Override
    public void onAttach(Context context) {
        ((Favorites) context).getSupportLoaderManager().initLoader(0, null, this);
        op = (Favorites) context;
        super.onAttach(context);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projections = {MovieContract.MovieData.POSTER_URL, MovieContract.MovieData.MOVIEID};
        return new CursorLoader(
                getContext(),
                MovieContract.MovieData.CONTENT_URI,
                projections,
                null,
                null,
                null
        );


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Recycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        Recycler.setAdapter(new MyFavouritesAdapter(getContext(), data, op));
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Recycler.setAdapter(null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (!GlobalVar.fav2pane)
            menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }
}

