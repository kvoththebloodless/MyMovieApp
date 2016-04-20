package com.example.gourav.mymovieapp.AdapterClasses;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gourav.mymovieapp.Misc.Movies;
import com.example.gourav.mymovieapp.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Gourav on 2/27/2016.
 */
public class IndividualAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    int index;
    final static int HEADERS=0,VIDEOS=1,REVIEWS=2;
    int VIDSIZE;
    int REVSIZE;


    LayoutInflater linf;
    public IndividualAdapter(Context c,
                             int index) {
        context = c;
        this.index = index;
        VIDSIZE=Movies.retMovieObj(index).getVidList().size();
        REVSIZE=Movies.retMovieObj(index).getReviewList().size();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        linf=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        switch (viewType)
            {
                case HEADERS:return new HeaderViewHolder(linf.inflate(R.layout.plainlistitem,null));
                case VIDEOS:return new VideoViewHolder(linf.inflate(R.layout.listitem_video,null));
                case REVIEWS:return new ReviewViewHolder(linf.inflate(R.layout.listitem_reviews,null));
     default:return null;
            }
            }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,
                                 int position) {
if(holder instanceof HeaderViewHolder)
        {
            switch (position)
                {

                    case 0:((HeaderViewHolder) holder).text.setText(Html.fromHtml("<b>Title</b>: <small>"+Movies.getName(index)+" </small>"));break;
                    case 1:((HeaderViewHolder) holder).text.setText(Html.fromHtml("<b>Release Date</b>: <small>"+Movies.getReleaseDate(index)+" </small>"));break;
                    case 2:((HeaderViewHolder) holder).text.setText(Html.fromHtml("<b>Rating</b>: <small>"+Movies.getRating(index)+"</small>"));break;
                    case 3:((HeaderViewHolder) holder).text.setText(Html.fromHtml("<b>Synopsis</b>: <small>"+Movies.getPlot(index)+"</small>"));break;
                    case 4:((HeaderViewHolder) holder).text.setText(Html.fromHtml("<H4>TRAILERS:</H4> "));break;
                    default:((HeaderViewHolder) holder).text.setText(Html.fromHtml("<H4>REVIEWS:</H4> ")); break;

                }

        }
        else if(holder instanceof VideoViewHolder)
                {
                    VideoViewHolder holder1=(VideoViewHolder)holder;
                    Movies.Videos vid=(Movies.Videos)Movies.retMovieObj(index).getVidList().
                            get(position-5);
                    holder1.Name.setText(vid.getName());
                    Picasso.with(context).load("http://img.youtube.com/vi/" + vid.getKey() + "/default.jpg").into(holder1.thumb);

                }
        else
            {ReviewViewHolder holder1=(ReviewViewHolder)holder;
                Movies.Reviews review=(Movies.Reviews)Movies.retMovieObj(index).getReviewList().get(position-(6+VIDSIZE));
                holder1.Content.setText(review.getContent());
                holder1.Author.setText(review.getAuthor());
            }
    }

    @Override
    public int getItemCount() {
        return 6+VIDSIZE+REVSIZE;
    }

    @Override
    public int getItemViewType(int position) {
     int viewtype=-1;
if(position<5)
    viewtype=HEADERS;
        else if(position>=5 && position<(5+VIDSIZE))
    viewtype=VIDEOS;
        else if(position==(5+VIDSIZE))
    viewtype=HEADERS;
        else
    viewtype=REVIEWS;
        return viewtype;
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
TextView text;
        public HeaderViewHolder(View itemView) {
            super(itemView);
            text=(TextView)itemView.findViewById(R.id.headertext);
        }
    }
    class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView Name;
        ImageView thumb;
        public VideoViewHolder(View itemView) {

            super(itemView);
            itemView.setOnClickListener(this);
           // Toast.makeText(context,"count",Toast.LENGTH_SHORT).show();
            Name=(TextView)itemView.findViewById(R.id.vidname);
            thumb=(ImageView)itemView.findViewById(R.id.thumbnail);
        }

        @Override
        public void onClick(View v) {

            Intent in=new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+((Movies.Videos)Movies.retMovieObj(index).getVidList().get(getLayoutPosition()-5)).getKey()));
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
            context.startActivity(in);
        }
    }
    class ReviewViewHolder extends RecyclerView.ViewHolder {

            TextView Author,Content;
            public ReviewViewHolder(View itemView) {
                super(itemView);
                Author=(TextView)itemView.findViewById(R.id.author);
                Content=(TextView)itemView.findViewById(R.id.content);
            }

        }


}
