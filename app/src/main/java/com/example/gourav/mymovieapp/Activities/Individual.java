

package com.example.gourav.mymovieapp.Activities;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.gourav.mymovieapp.Fragments.IndiFrag;
import com.example.gourav.mymovieapp.Misc.GlobalVar;
import com.example.gourav.mymovieapp.R;

public class Individual extends AppCompatActivity {

    Toolbar tooly;
    private static final int TO_FAV_ITEMID = 10001;//assigning a random int

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalVar.indiactivity = true;

        setContentView(R.layout.activity_individual);


        if (savedInstanceState == null) {

            Bundle arguments = new Bundle();
            arguments.putInt("ID", getIntent().getIntExtra("ID", 0));

            IndiFrag fragment = new IndiFrag();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.indiFragCont, fragment)
                    .commit();
        }
        tooly = (Toolbar) findViewById(R.id.toolbarIndi);


    }

    @Override
    protected void onDestroy() {
        GlobalVar.indiactivity = false;
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            case TO_FAV_ITEMID:
                startActivity(new Intent(this, Favorites.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(0, TO_FAV_ITEMID, 0, "Favourites");
        return super.onCreateOptionsMenu(menu);
    }
}

