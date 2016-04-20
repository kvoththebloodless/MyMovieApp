package com.example.gourav.mymovieapp.AdapterClasses;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gourav.mymovieapp.Activities.Favourite_detail;
import com.example.gourav.mymovieapp.Activities.Individual;
import com.example.gourav.mymovieapp.ContentProviderPackage.MovieContract;
import com.example.gourav.mymovieapp.Interfaces.Communication;
import com.example.gourav.mymovieapp.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Gourav on 2/15/2016.
 */
public class
        MyFavouritesAdapter extends RecyclerView.Adapter<MyFavouritesAdapter.MyViewHolder> {
    Context context;
    LayoutInflater linf;
    Cursor Data;
    Communication op;

    public MyFavouritesAdapter(Context c, Cursor cur, Communication op) {
        context = c;
        Data = cur;
        linf = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.op = op;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new MyViewHolder(linf.inflate(R.layout.listitem_common, null));
    }

    @Override
    public int getItemCount() {
        return Data.getCount();
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Data.moveToPosition(position);
        holder.id = Integer.parseInt(Data.getString(Data.getColumnIndex(MovieContract.MovieData.MOVIEID)));

        Picasso.with(context)
                .load("https://image.tmdb.org/t/p/w154" + Data.getString(Data.getColumnIndex(MovieContract.MovieData.POSTER_URL)))
                .fit()
                .into(holder.im);

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView im;
        int id;

        public MyViewHolder(View itemView) {

            super(itemView);
            im = (ImageView) itemView.findViewById(R.id.image1);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    op.onMasterItemClicked(id);
                }
            });
        }
    }
}
