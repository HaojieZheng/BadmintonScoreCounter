package com.haojie.badmintonscorecounter;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class ManagePlayersActivity extends AppCompatActivity implements ViewUpdatePhotoDialogFragment.OnFragmentInteractionListener{

    ListView mPlayerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_players);

        mPlayerList = (ListView)findViewById(R.id.player_list);

        populateList();
    }

    private void populateList()
    {
        Database database = new Database();
        database.deserialize(getApplicationContext());

        ManagePlayerArrayAdapter adapter = new ManagePlayerArrayAdapter(getApplicationContext(), database.getPlayersWithoutDefault(), this);

        mPlayerList.setAdapter(adapter);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
