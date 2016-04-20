package com.example.gourav.mymovieapp.ContentProviderPackage;


import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int database_version = 1;

    public String CREATE_QUERY1 = "CREATE TABLE "+MovieContract.MovieData.TABLE_NAME+"("+MovieContract.MovieData._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+MovieContract.MovieData.MOVIE_NAME+" TEXT, "+MovieContract.MovieData.POSTER_URL+" TEXT, "+MovieContract.MovieData.BACKDROP+" TEXT, "+MovieContract.MovieData.MOVIEID+" TEXT, "+MovieContract.MovieData.PLOT+" TEXT, "+MovieContract.MovieData.RELEASE_DATE+" TEXT, "+MovieContract.MovieData.RATING+" TEXT);";
    public String CREATE_QUERY2 = "CREATE TABLE "+MovieContract.MovieReviews.TABLE_NAME+"("+MovieContract.MovieReviews._ID+" INTEGER PRIMARY KEY AUTOINCREMENT , "+MovieContract.MovieReviews.MOVIE_ID+" TEXT, "+MovieContract.MovieReviews.REVIEWS+" TEXT, "+MovieContract.MovieReviews.AUTHOR+" TEXT);";
    public String CREATE_QUERY3 = "CREATE TABLE "+MovieContract.MovieVideos.TABLE_NAME+"("+MovieContract.MovieVideos._ID+" INTEGER PRIMARY KEY AUTOINCREMENT , "+MovieContract.MovieVideos.MOVIE_ID+" TEXT, "+MovieContract.MovieVideos.KEY+" TEXT, "+ MovieContract.MovieVideos.VID+" TEXT, "+MovieContract.MovieVideos.VNAME+" TEXT);";
    public DatabaseHelper(Context context) {
        super(context, MovieContract.MovieData.DATABASE_NAME, null, database_version);
        Log.d("Database operations", "Database created");

    }

    @Override
    public void onCreate(SQLiteDatabase sdb) {

        sdb.execSQL(CREATE_QUERY1);
        sdb.execSQL(CREATE_QUERY2);
        sdb.execSQL(CREATE_QUERY3);

        Log.d("Database operations", "Tables created");

    }
    public Cursor returnGenMovieInfo(String[] projection, String selection, String[] selectionArgs, String sortOrder)
        {
            SQLiteDatabase db=getWritableDatabase();
           return db.query(MovieContract.MovieData.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
        }
    public  Cursor returnReviewsForMovies(String[] projection, String selection, String[] selectionArgs, String sortOrder)
        {
            SQLiteDatabase db=getWritableDatabase();
            return db.query(MovieContract.MovieReviews.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
        }
    public  Cursor returnVideosForMovies(String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        SQLiteDatabase db=getWritableDatabase();
        return db.query(MovieContract.MovieVideos.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub

    }




}