package com.example.gourav.mymovieapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.gourav.mymovieapp.Fragments.IndiFrag;
import com.example.gourav.mymovieapp.Fragments.PopularFrag;
import com.example.gourav.mymovieapp.Interfaces.Communication;
import com.example.gourav.mymovieapp.Misc.GlobalVar;
import com.example.gourav.mymovieapp.R;


public class Popular extends AppCompatActivity implements Communication {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_popular);


            if (findViewById(R.id.indiFragCont) != null)

                GlobalVar.m2pane = true;


            FragmentManager fm = getSupportFragmentManager();
            PopularFrag frag = (PopularFrag) fm.findFragmentByTag("popularfrag");
            if (frag == null) {
                frag = new PopularFrag();
                fm.beginTransaction()
                        .add(R.id.popularFragCont, frag, "popularfrag")
                        .commit();
            }

            Toolbar toolbar = (Toolbar) findViewById(R.id.tooly);
            toolbar.setTitle("Movies");
            setSupportActionBar(toolbar);

        } catch (Exception e) {
            Log.e("error_popular", e + "");
        }

    }

    @Override
    public void onMasterItemClicked(int id) {
        if (GlobalVar.m2pane) {

            Bundle arguments = new Bundle();
            arguments.putInt("ID", id);
            IndiFrag fragment = new IndiFrag();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.indiFragCont, fragment)
                    .commit();

        } else {


            Intent detailIntent = new Intent(this, Individual.class);
            detailIntent.putExtra("ID", id);
            startActivity(detailIntent);
        }
    }

    @Override
    public void deleteDetailFragment() {
        if (GlobalVar.m2pane) {
            Fragment remFrag = getSupportFragmentManager().findFragmentById(R.id.indiFragCont);
            if (remFrag != null)
                getSupportFragmentManager().beginTransaction()
                        .remove(remFrag)
                        .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_tofavourites:
                startActivity(new Intent(this, Favorites.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        GlobalVar.m2pane = false;
        super.onDestroy();
    }


}
