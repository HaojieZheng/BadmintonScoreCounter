package com.haojie.badmintonscorecounter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
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
            displayText += "<b>Top Players:</b><br>";
            for (Pair<Player, Integer> pair : top3Players) {
                displayText += "\t" + pair.first.getName() + " : " + pair.second + " wins<br>";
            }
        }

        if (displayText.isEmpty())
            displayText = "No statistics to display";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            mInfoTextView.setText(android.text.Html.fromHtml(displayText, Html.FROM_HTML_MODE_COMPACT));
        }
        else
        {
            mInfoTextView.setText(android.text.Html.fromHtml(displayText));
        }
    }

    GameStatisticsPresenter mGameStatisticsPresenter;
}
