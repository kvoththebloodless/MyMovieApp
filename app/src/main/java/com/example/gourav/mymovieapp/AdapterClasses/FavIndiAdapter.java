package com.example.gourav.mymovieapp.AdapterClasses;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Movie;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gourav.mymovieapp.ContentProviderPackage.MovieContract;
import com.example.gourav.mymovieapp.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Gourav on 2/27/2016.
 */
public class FavIndiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    int index;
    final static int HEADERS = 0, VIDEOS = 1, REVIEWS = 2;
    int VIDSIZE;
    int REVSIZE;
    Cursor general, video, review;
    LayoutInflater linf;

    public FavIndiAdapter(Context c, int index, Cursor cursor1, Cursor cursor2, Cursor cursor3) {
        context = c;
        this.index = index;
        general = cursor1;
        video = cursor2;
        review = cursor3;
        if (video == null)
            VIDSIZE = 0;
        else
            VIDSIZE = video.getCount();

        if (review == null)
            REVSIZE = 0;
        else
            REVSIZE = review.getCount();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        linf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        switch (viewType) {
            case HEADERS:
                return new HeaderViewHolder(linf.inflate(R.layout.plainlistitem, null));
            case VIDEOS:
                return new VideoViewHolder(linf.inflate(R.layout.listitem_video, null));
            case REVIEWS:
                return new ReviewViewHolder(linf.inflate(R.layout.listitem_reviews, null));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            if (general != null && general.moveToFirst()) {
                switch (position) {
                    case 0:
                        ((HeaderViewHolder) holder).text.setText(Html.fromHtml("<b>Title</b>: <small>" + general.getString(general.getColumnIndex(MovieContract.MovieData.MOVIE_NAME)) + " </small>"));
                        break;
                    case 1:
                        ((HeaderViewHolder) holder).text.setText(Html.fromHtml("<b>Release Date</b>: <small>" + general.getString(general.getColumnIndex(MovieContract.MovieData.RELEASE_DATE)) + " </small>"));
                        break;
                    case 2:
                        ((HeaderViewHolder) holder).text.setText(Html.fromHtml("<b>Rating</b>: <small>" + general.getString(general.getColumnIndex(MovieContract.MovieData.RATING)) + "</small>"));
                        break;
                    case 3:
                        ((HeaderViewHolder) holder).text.setText(Html.fromHtml("<b>Synopsis</b>: <small>" + general.getString(general.getColumnIndex(MovieContract.MovieData.PLOT)) + "</small>"));
                        break;
                    case 4:
                        ((HeaderViewHolder) holder).text.setText(Html.fromHtml("<H4>TRAILERS:</H4> "));
                        break;
                    default:
                        ((HeaderViewHolder) holder).text.setText(Html.fromHtml("<H4>REVIEWS:</H4> "));
                        break;

                }
            }
        } else if (holder instanceof VideoViewHolder) {
            if (video != null && video.moveToPosition(position - 5)) {
                VideoViewHolder holder1 = (VideoViewHolder) holder;
                holder1.Name.setText(video.getString(video.getColumnIndex(MovieContract.MovieVideos.VNAME)));
                Picasso.with(context).load("http://img.youtube.com/vi/" + video.getString(video.getColumnIndex(MovieContract.MovieVideos.KEY)) + "/default.jpg").into(holder1.thumb);
            }
        } else {
            if (review != null && review.moveToPosition(position - (6 + VIDSIZE))) {
                ReviewViewHolder holder1 = (ReviewViewHolder) holder;
                holder1.Content.setText(review.getString(review.getColumnIndex(MovieContract.MovieReviews.REVIEWS)));
                holder1.Author.setText(review.getString(review.getColumnIndex(MovieContract.MovieReviews.AUTHOR)));
            }
        }
    }

    @Override
    public int getItemCount() {
        return 6 + VIDSIZE + REVSIZE;
    }

    @Override
    public int getItemViewType(int position) {
        int viewtype = -1;
        if (position < 5)
            viewtype = HEADERS;
        else if (position >= 5 && position < (5 + VIDSIZE))
            viewtype = VIDEOS;
        else if (position == (5 + VIDSIZE))
            viewtype = HEADERS;
        else
            viewtype = REVIEWS;
        return viewtype;
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView text;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.headertext);

        }
    }

    class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView Name;
        ImageView thumb;

        public VideoViewHolder(View itemView) {

            super(itemView);
            itemView.setOnClickListener(this);
            Name = (TextView) itemView.findViewById(R.id.vidname);
            thumb = (ImageView) itemView.findViewById(R.id.thumbnail);
        }

        @Override
        public void onClick(View v) {
            video.moveToPosition(getLayoutPosition() - 5);
            Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + video.getString(video.getColumnIndex(MovieContract.MovieVideos.KEY))));
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(in);
        }
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {

        TextView Author, Content;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            Author = (TextView) itemView.findViewById(R.id.author);
            Content = (TextView) itemView.findViewById(R.id.content);
        }

    }

}
