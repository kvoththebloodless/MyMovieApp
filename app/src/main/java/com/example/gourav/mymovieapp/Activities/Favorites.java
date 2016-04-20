package com.example.gourav.mymovieapp.Activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.gourav.mymovieapp.Fragments.FavdetFrag;
import com.example.gourav.mymovieapp.Fragments.FavouriteFrag;
import com.example.gourav.mymovieapp.Interfaces.Communication;
import com.example.gourav.mymovieapp.Misc.GlobalVar;
import com.example.gourav.mymovieapp.R;

public class Favorites extends AppCompatActivity implements Communication {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular);


        if (findViewById(R.id.indiFragCont) != null)

            GlobalVar.fav2pane = true;

        FragmentManager fm = getSupportFragmentManager();
        FavouriteFrag frag = (FavouriteFrag) fm.findFragmentById(R.id.popularFragCont);
        if (frag == null) {
            frag = new FavouriteFrag();

            fm.beginTransaction()
                    .add(R.id.popularFragCont, frag, "popularfrag")
                    .commit();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.tooly);
        toolbar.setTitle("Favourites");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    @Override
    public void onMasterItemClicked(int id) {
        if (GlobalVar.fav2pane) {


            Bundle arguments = new Bundle();
            arguments.putInt("MovieId", id);
            FavdetFrag fragment = new FavdetFrag();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.indiFragCont, fragment, "favdetfrag")
                    .commit();

        } else {

            Intent detailIntent = new Intent(this, Favourite_detail.class);
            detailIntent.putExtra("MovieId", id);
            startActivity(detailIntent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void deleteDetailFragment() {

        Fragment remFrag = getSupportFragmentManager().findFragmentByTag("favdetfrag");
        if (remFrag != null)
            getSupportFragmentManager().beginTransaction()
                    .remove(remFrag)
                    .commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GlobalVar.fav2pane = false;

    }
}
