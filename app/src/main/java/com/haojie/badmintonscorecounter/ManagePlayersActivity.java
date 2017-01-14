package com.haojie.badmintonscorecounter;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
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
    public void onDismiss(String name) {
        // refresh the photo
        ((ManagePlayerArrayAdapter)mPlayerList.getAdapter()).notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("Manage player image");
        fragment.onActivityResult(requestCode, resultCode, data);
    }


}
