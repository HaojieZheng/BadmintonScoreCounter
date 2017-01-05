package com.haojie.badmintonscorecounter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class ManagePlayersActivity extends AppCompatActivity {

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

        SelectPlayerArrayAdapter adapter = new SelectPlayerArrayAdapter(getApplicationContext(), database.getPlayersWithoutDefault());

        mPlayerList.setAdapter(adapter);
    }


}
