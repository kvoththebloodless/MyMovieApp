package com.example.gourav.mymovieapp.Misc;


import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;


public class Movies {

    private static ArrayList<Movies> MovieList = new ArrayList<Movies>();
    private ArrayList<Reviews> ReviewList = new ArrayList<>();
    private ArrayList<Videos> VideoList = new ArrayList<>();
    private String Name = "", Id = "", Plot = "", Backdrop = "", Rating = "", ReleaseDate = "", Poster = "";
    private static HashMap<String,Integer> MovieTypeCount=new HashMap<>();


    public static void addShow(Movies t) {

        MovieList.add(t);

    }

    public Movies(String Name, String Id, String Plot, String Backdrop, String Rating, String ReleaseDate, String Poster, String type) {

        this.Name = Name;
        this.Id = Id;
        this.Plot = Plot;
        this.Backdrop = Backdrop;
        this.Rating = Rating;
        this.ReleaseDate = ReleaseDate;
        this.Poster = Poster;
        Integer temp=MovieTypeCount.get(type);
        if(temp==null)
            temp=0;

        MovieTypeCount.put(type,++temp);
    }
    public static int retMovieTypeCount(String type)
            {Integer temp=MovieTypeCount.get(type);
                if(temp==null)
                    return 0;
                else
                    return MovieList.size();

            }
    public static void reset() {
        MovieList.clear();
        MovieTypeCount.clear();
    }

    public static String getName(int position) {
        Log.i("name", MovieList.get(position).Name);
        return MovieList.get(position).Name;
    }

    public static String getBackDrop(int position) {
        return MovieList.get(position).Backdrop;
    }

    public static String getPoster(int position) {
        Log.i("poster", MovieList.get(position).Name);return MovieList.get(position).Poster;
    }

    public static String getId(int position) {
        return MovieList.get(position).Id;
    }

    public static String getRating(int position) {
        return MovieList.get(position).Rating;
    }

    public static String getReleaseDate(int position) {
        return MovieList.get(position).ReleaseDate;
    }

    public static String getPlot(int position) {

        return MovieList.get(position).Plot;


    }

    public static int retShowCount() {
        return MovieList.size();
    }

    public void appendVideos(String id, String key, String name) {
        VideoList.add(new Videos(id, key, name));
    }
    public void appendReviews(String Author,String Content)
        {
            ReviewList.add(new Reviews(Author,Content));
        }
    public static Movies retMovieObj(int position)

                {
                 return MovieList.get(position);
                }
    public ArrayList getVidList()
        {
            return VideoList;
        }
    public ArrayList getReviewList()
        {
            return ReviewList;
        }


    public class Reviews {
        private String AUTHOR;
        private String CONTENT;

        public Reviews(String Author, String Content) {
            AUTHOR = Author;
            CONTENT = Content;
        }

        public String getAuthor() {
            return AUTHOR;
        }

        public String getContent() {
            return CONTENT;
        }

    }


   public class Videos {

        private String ID;
        private String KEY;
        private String NAME;

        public Videos(String id, String key, String name) {
            ID = id;
            KEY=key;
            NAME=name;


        }

        public String getId() {
            return ID;
        }

        public String getKey() {
            return KEY;
        }

        public String getName() {
            return NAME;
        }


    }
}







