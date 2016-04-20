package com.example.gourav.mymovieapp.ContentProviderPackage;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {
    public static final String AUTHORITY =
            "com.example.gourav.movieapp.MovieProvider";
    public static final Uri BASE_CONTENT_URI =
           Uri.parse("content://"+AUTHORITY);
      public static final String PATH_MOVIE = "shows";
      public static final String PATH_REVIEWS = "reviews";
      public static final String PATH_VIDEOS = "videos";

    public static class MovieData implements BaseColumns {
        public static final Uri CONTENT_URI =
                              BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();
                     public static final String CONTENT_TYPE =
                             ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_MOVIE;
           public static final String CONTENT_ITEM_TYPE =
                             ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_MOVIE;

        public static final
                String _ID="_id",
                TABLE_NAME="Movie",
               MOVIE_NAME = "Name",
                MOVIEID = "Movieid", PLOT = "Plot",
                BACKDROP = "Backdrop",
                RATING = "Rating",
                RELEASE_DATE = "Release_date",
                POSTER_URL= "Poster",

                DATABASE_NAME="Movie";
        public static Uri uriWithId(long id)
            {return ContentUris.withAppendedId(CONTENT_URI,id);

            }
    }
    public static class MovieReviews implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEWS).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_REVIEWS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_REVIEWS;
        public static final
        String _ID="_id",
                TABLE_NAME="Movie_reviews",
                MOVIE_ID = "Movieid",
                REVIEWS="Reviews",
                AUTHOR="Author";

        public static Uri uriWithId(long id)
        {return ContentUris.withAppendedId(CONTENT_URI,id);

        }
    }
    public static class MovieVideos implements BaseColumns {
         public static final Uri CONTENT_URI =
                 BASE_CONTENT_URI.buildUpon().appendPath(PATH_VIDEOS).build();
         public static final String CONTENT_TYPE =
                 ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_VIDEOS;
         public static final String CONTENT_ITEM_TYPE =
                 ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_VIDEOS;
        public static final
        String _ID="_id",
                TABLE_NAME="Movie_videos",
                MOVIE_ID = "Movieid",
                KEY="Keys",
                VNAME="Vname",
                VID="Vid";
        public static Uri uriWithId(long id)
        {return ContentUris.withAppendedId(CONTENT_URI,id);

        }
    }
}