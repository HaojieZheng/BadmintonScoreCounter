package com.haojie.badmintonscorecounter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;

public class EnterSinglesPlayersNames extends AppCompatActivity implements SelectPlayerNameDialogFragment.SelectPlayerNameClickHandler{

    ImageButton mSwapButton;
    ImageButton mAddress1;
    ImageButton mAddress2;
    EditText mEditPlayer1Name;
    EditText mEditPlayer2Name;
    Button mStartGameButton;
    int mPlayerSelectionShown = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_singles_players_names);

        mSwapButton = (ImageButton)findViewById(R.id.swap_button);
        mEditPlayer1Name = (EditText)findViewById(R.id.editPlayer1Name);
        mEditPlayer2Name = (EditText)findViewById(R.id.editPlayer2Name);
        mStartGameButton = (Button)findViewById(R.id.button_start);
        mAddress1 = (ImageButton)findViewById(R.id.addressbook1);
        mAddress2 = (ImageButton)findViewById(R.id.addressbook2);


        mSwapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = mEditPlayer1Name.getText().toString();
                mEditPlayer1Name.setText(mEditPlayer2Name.getText().toString(), TextView.BufferType.EDITABLE);
                mEditPlayer2Name.setText(temp, TextView.BufferType.EDITABLE);
            }
        });


        mStartGameButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                startGameSessionActivity();
            }
        });

        mAddress1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayerSelectionShown = 1;
                startSelectPlayerFromListActivity();
            }
        });

        mAddress2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayerSelectionShown = 2;
                startSelectPlayerFromListActivity();
            }
        });

    }


    private void startGameSessionActivity()
    {

        // save the player names first
        Database database = new Database();
        try {
            database.Deserialize(EnterSinglesPlayersNames.this);
            String player1Name = mEditPlayer1Name.getText().toString();
            Player player1 = database.getPlayerWithName(player1Name);
            if (player1 == null || player1Name.startsWith("Player"))
            {
                player1 = new Player(player1Name);
                database.addPlayer(player1);
            }

            String player2Name = mEditPlayer2Name.getText().toString();
            Player player2 = database.getPlayerWithName(player2Name);
            if (player2 == null || player2Name.startsWith("Player"))
            {
                player2 = new Player(player2Name);
                database.addPlayer(player2);
            }
            database.Serialize(EnterSinglesPlayersNames.this);

        }
        catch (IOException e)
        {

        }

        Intent intent = new Intent(this, GameSessionActivity.class);
        intent.putExtra(GameSessionActivity.EXTRA_GAME_TYPE, true);
        intent.putExtra(GameSessionActivity.EXTRA_TEAM1_RIGHT_PLAYER_NAME, mEditPlayer1Name.getText().toString());
        intent.putExtra(GameSessionActivity.EXTRA_TEAM2_RIGHT_PLAYER_NAME, mEditPlayer2Name.getText().toString());

        startActivity(intent);
        finish();
    }

    private void startSelectPlayerFromListActivity()
    {
        SelectPlayerNameDialogFragment dialogFrag = new SelectPlayerNameDialogFragment();
        dialogFrag.show(getFragmentManager(), "Select single player name");
    }

    @Override
    public void onNameSelected(SelectPlayerNameDialogFragment dialogFragment, String playerName) {
        if (mPlayerSelectionShown == 1)
        {
            mEditPlayer1Name.setText(playerName, TextView.BufferType.EDITABLE);
        }
        else if (mPlayerSelectionShown == 2)
        {
            mEditPlayer2Name.setText(playerName, TextView.BufferType.EDITABLE);
        }
    }
}
