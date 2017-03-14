package com.haojie.badmintonscorecounter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ViewGameStatisticsActivity extends AppCompatActivity {



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
        List<GameStatisticsPresenter.PlayerWinEntry> top3Players = mGameStatisticsPresenter.getTopNPlayers(3);

        String displayText = "";
        if (!top3Players.isEmpty()) {
            displayText += "<b>Top Players:</b><br>";
            for (GameStatisticsPresenter.PlayerWinEntry pair : top3Players) {
                displayText += "\t" + pair.getPlayer().getName() + " : " + pair.getWins() + " wins<br>";
            }
        }

        if (displayText.isEmpty())
            displayText = "No statistics to display";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            mInfoTextView.setText(android.text.Html.fromHtml(displayText, Html.FROM_HTML_MODE_COMPACT));
        }
        else
        {
            //noinspection deprecation
            mInfoTextView.setText(android.text.Html.fromHtml(displayText));
        }
    }



    private TextView mInfoTextView;
    private GameStatisticsPresenter mGameStatisticsPresenter;
}
