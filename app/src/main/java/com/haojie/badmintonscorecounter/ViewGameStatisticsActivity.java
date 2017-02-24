package com.haojie.badmintonscorecounter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewGameStatisticsActivity extends AppCompatActivity {

    TextView mInfoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_statistics);

        Database database = new Database();
        database.deserialize(this);
        mGameStatisticsPresenter = new GameStatisticsPresenter(database);

        mInfoTextView = (TextView)findViewById(R.id.info_text_view);

        mGameStatisticsPresenter.calculate();

        loadInfoText();
    }

    private void loadInfoText()
    {
        ArrayList<Pair<Player, Integer>> top3Players = mGameStatisticsPresenter.getTopNPlayers(3);

        String displayText = "";
        if (!top3Players.isEmpty()) {
            displayText += "Top Players:\n";
            for (Pair<Player, Integer> pair : top3Players) {
                displayText += pair.first.getName() + " : " + pair.second + " wins\n";
            }
        }
        mInfoTextView.setText(displayText);
    }

    GameStatisticsPresenter mGameStatisticsPresenter;
}
