package com.haojie.badmintonscorecounter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ViewGameStatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_statistics);

        Database database = new Database();
        database.deserialize(this);
        mGameStatisticsPresenter = new GameStatisticsPresenter(database);
    }

    GameStatisticsPresenter mGameStatisticsPresenter;
}
