package com.example.gourav.mymovieapp.ContentProviderPackage;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;


public class MovieProvider extends ContentProvider {
    static final int MOVIES= 100;//item
    static final int REVIEWS= 101;//item
    static final int VIDEOS=102;//dir
    static final int MOVIES_ALL=103;//dir
    private static final UriMatcher URI_MATCHER = buildUriMatcher();
    private static DatabaseHelper dh;

   public static UriMatcher buildUriMatcher()
        {
        final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
            URI_MATCHER.addURI(MovieContract.AUTHORITY,
                    MovieContract.PATH_MOVIE,
                    MOVIES_ALL);
            URI_MATCHER.addURI(MovieContract.AUTHORITY,
                 MovieContract.PATH_VIDEOS,
                  VIDEOS);
            URI_MATCHER.addURI(MovieContract.AUTHORITY,
                    MovieContract.PATH_REVIEWS,
                  REVIEWS);
            URI_MATCHER.addURI(MovieContract.AUTHORITY,
                    MovieContract.PATH_MOVIE+"/#",
                    MOVIES);
            return URI_MATCHER;
        }
    @Override
    public boolean onCreate() {
       dh =new DatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;

        switch (URI_MATCHER.match(uri)) {

            case MOVIES_ALL:
            {
                retCursor=dh.returnGenMovieInfo(projection,selection,selectionArgs,sortOrder);

                break;
            }

            case REVIEWS: {
               // selection=MovieContract.MovieReviews._ID+"="+rowid;
                retCursor=dh.returnReviewsForMovies( projection, selection,selectionArgs, sortOrder);
                break;
            }

            case VIDEOS: {//selection=MovieContract.MovieVideos._ID+"="+rowid;
                retCursor = dh.returnVideosForMovies(projection,selection,selectionArgs,sortOrder);
                break;
            }
            case
                MOVIES:{
                int rowid=Integer.parseInt(uri.getPathSegments().get(1));
                selection=MovieContract.MovieData._ID+"="+rowid;
                retCursor=dh.returnGenMovieInfo(projection,selection,selectionArgs,sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = dh.getWritableDatabase();
        final int match = URI_MATCHER.match(uri);


        switch (match) {
            case MOVIES: {
                db.beginTransaction();
                int returncount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.MovieData.TABLE_NAME, null, value);
                        if (_id != -1)
                            returncount++;
                    }
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returncount;



            }

            case REVIEWS: {

                db.beginTransaction();
                int returncount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.MovieReviews.TABLE_NAME, null, value);
                        if (_id != -1)
                            returncount++;
                    }
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returncount;



            }
            case VIDEOS: {

                db.beginTransaction();
                int returncount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.MovieVideos.TABLE_NAME, null, value);
                        if (_id != -1)
                            returncount++;
                    }
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returncount;



            }
            default:throw new UnsupportedOperationException("Uri doesnt match "+uri);
        }
    }
    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case MOVIES:
                return MovieContract.MovieData.CONTENT_ITEM_TYPE;
            case REVIEWS:
                return MovieContract.MovieReviews.CONTENT_TYPE;
            case VIDEOS:
                return MovieContract.MovieVideos.CONTENT_TYPE;
            case MOVIES_ALL:
                return MovieContract.MovieData.CONTENT_TYPE;
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = dh.getWritableDatabase();
        final int match = URI_MATCHER.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIES_ALL: {

                long _id = db.insert(MovieContract.MovieData.TABLE_NAME, null, values);
                if ( _id >=0 )
                    returnUri = MovieContract.MovieData.uriWithId(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case REVIEWS: {

                long _id = db.insert(MovieContract.MovieReviews.TABLE_NAME, null, values);
                if ( _id >=0 )
                    returnUri = MovieContract.MovieReviews.uriWithId(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case VIDEOS: {

                long _id = db.insert(MovieContract.MovieVideos.TABLE_NAME, null, values);
                if ( _id >=0 )
                    returnUri = MovieContract.MovieVideos.uriWithId(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
    final SQLiteDatabase db=dh.getWritableDatabase();
        final int match= URI_MATCHER.match(uri);
        int rowsdeleted;
        if(null==selection) selection="1";
        switch(match)
            {
                case MOVIES:
                    int rowid=Integer.parseInt(uri.getPathSegments().get(1));
                    selection=MovieContract.MovieData._ID+"="+rowid;
                    rowsdeleted=db.delete(MovieContract.MovieData.TABLE_NAME,selection,selectionArgs);
                    break;
                case REVIEWS:
                    rowsdeleted=db.delete(MovieContract.MovieReviews.TABLE_NAME,selection,selectionArgs);
                    break;
                case VIDEOS:
                    rowsdeleted=db.delete(MovieContract.MovieVideos.TABLE_NAME,selection,selectionArgs);
                    break;
                case MOVIES_ALL:
                    rowsdeleted=db.delete(MovieContract.MovieData.TABLE_NAME,selection,selectionArgs);
                break;
                default:throw new UnsupportedOperationException("Unknown uri: "+uri);
            }
        if(rowsdeleted!=0)
            {
                getContext().getContentResolver().notifyChange(uri,null);
            }
        return rowsdeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db=dh.getWritableDatabase();
        final int match= URI_MATCHER.match(uri);
        int rowsupdated;

        switch(match)
        {
            case MOVIES: int rowid=Integer.parseInt(uri.getPathSegments().get(1));
                selection=MovieContract.MovieData._ID+"="+rowid;
                rowsupdated=db.update(MovieContract.MovieData.TABLE_NAME, values, selection, selectionArgs);
                break;
            case REVIEWS:
                rowsupdated=db.update(MovieContract.MovieReviews.TABLE_NAME, values, selection, selectionArgs);
                break;
            case VIDEOS:
                rowsupdated=db.update(MovieContract.MovieVideos.TABLE_NAME, values, selection, selectionArgs);
                break;
            case MOVIES_ALL:
                rowsupdated=db.update(MovieContract.MovieData.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:throw new UnsupportedOperationException("Unknown uri: "+uri);
        }
        if(rowsupdated!=0)
        {
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsupdated;
    }
}
