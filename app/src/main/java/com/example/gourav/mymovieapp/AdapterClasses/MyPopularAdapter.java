package com.example.gourav.mymovieapp.AdapterClasses;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.example.gourav.mymovieapp.Interfaces.Communication;
import com.example.gourav.mymovieapp.Misc.Movies;
import com.example.gourav.mymovieapp.R;
import com.squareup.picasso.Picasso;


/**
 * Created by Gourav on 11/12/2015.
 */
public class MyPopularAdapter extends RecyclerView.Adapter<MyPopularAdapter.PopularHolder> {
    Context c;

    LayoutInflater linf;

    Communication op;

    public MyPopularAdapter(Context c, Communication op) {
        this.c = c;
        linf = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.op = op;
    }

    @Override
    public PopularHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new PopularHolder(linf.inflate(R.layout.listitem_common, null));
    }

    @Override
    public void onBindViewHolder(PopularHolder holder, int position) {

        Picasso.with(c)
                .load("https://image.tmdb.org/t/p/w154" + Movies.getPoster(position))
                .fit()
                .into(holder.Image);

    }

    @Override
    public int getItemCount() {
        return Movies.retShowCount();
    }

    class PopularHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView Image;

        public PopularHolder(View itemView) {
            super(itemView);

            Image = (ImageView) itemView.findViewById(R.id.image1);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            op.onMasterItemClicked(getLayoutPosition());

        }
    }

}
