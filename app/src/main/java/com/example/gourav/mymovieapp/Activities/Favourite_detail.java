package com.example.gourav.mymovieapp.Activities;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import com.example.gourav.mymovieapp.Fragments.FavdetFrag;
import com.example.gourav.mymovieapp.Misc.GlobalVar;
import com.example.gourav.mymovieapp.R;


public class Favourite_detail extends AppCompatActivity {
    int ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalVar.favdetactivity = true;
        ID = getIntent().getIntExtra("MovieId", 0);
        setContentView(R.layout.activity_individual);
        if (savedInstanceState == null) {

            Bundle arguments = new Bundle();


            arguments.putInt("MovieId", ID);

            FavdetFrag fragment = new FavdetFrag();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.indiFragCont, fragment)
                    .commit();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GlobalVar.favdetactivity = false;
    }
}
