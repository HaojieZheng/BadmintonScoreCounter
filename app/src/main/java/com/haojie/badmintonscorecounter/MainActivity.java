package com.haojie.badmintonscorecounter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button mViewPlayersButton;
    private Button mViewGameStatisticsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startSinglesButton = (Button)findViewById(R.id.button_singles);
        Button startDoublesButton = (Button)findViewById(R.id.button_doubles);
        mViewPlayersButton = (Button)findViewById(R.id.button_view_players);
        mViewGameStatisticsButton = (Button)findViewById(R.id.button_view_game_statistics);

        refreshButtonStates();


        startSinglesButton.setOnClickListener(new View.OnClickListener()
        {
           @Override
            public void onClick(View v)
           {
               Intent intent = new Intent(MainActivity.this, EnterSinglesPlayersNamesActivity.class);
               startActivity(intent);
           }
        });

        startDoublesButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, EnterDoublesPlayersNamesActivity.class);
                startActivity(intent);
            }
        }
        );

        mViewPlayersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewPlayersActivity.class);
                startActivity(intent);
            }
        });

        mViewGameStatisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewGameStatisticsActivity.class);
                startActivity(intent);
            }
        });
    }

    void refreshButtonStates()
    {
        Database database = new Database();
        database.deserialize(MainActivity.this);
        boolean enableViewPlayers = database.getPlayersWithoutDefault().size() != 0;
        mViewPlayersButton.setEnabled(enableViewPlayers);
        mViewPlayersButton.setClickable(enableViewPlayers);

        boolean enableViewStatistics = database.getGames().size() != 0 && enableViewPlayers;
        mViewGameStatisticsButton.setEnabled(enableViewStatistics);
        mViewGameStatisticsButton.setClickable(enableViewStatistics);
    }



    @Override
    protected void onResume()
    {
        super.onResume();
        refreshButtonStates();
    }


}
