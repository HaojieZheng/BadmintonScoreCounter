package com.haojie.badmintonscorecounter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class EnterDoublesPlayersNames extends AppCompatActivity implements SelectPlayerNameDialogFragment.SelectPlayerNameClickHandler{

    ImageButton mSwapTeam1Button;
    ImageButton mSwapTeam2Button;
    ImageButton mSwapTeamsButton;
    ImageButton mAddress1;
    ImageButton mAddress2;
    ImageButton mAddress3;
    ImageButton mAddress4;
    EditText mEditTeam1Player1Name;
    EditText mEditTeam1Player2Name;
    EditText mEditTeam2Player1Name;
    EditText mEditTeam2Player2Name;
    Button mStartGameButton;

    int mPlayerSelectionShown = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_doubles_players_names);

        mSwapTeam1Button = (ImageButton)findViewById(R.id.swapteam1_button);
        mSwapTeam2Button = (ImageButton)findViewById(R.id.swapteam2_button);
        mSwapTeamsButton = (ImageButton)findViewById(R.id.swapteams_button);

        mAddress1 = (ImageButton)findViewById(R.id.addressbook1);
        mAddress2 = (ImageButton)findViewById(R.id.addressbook2);
        mAddress3 = (ImageButton)findViewById(R.id.addressbook3);
        mAddress4 = (ImageButton)findViewById(R.id.addressbook4);

        mEditTeam1Player1Name = (EditText)findViewById(R.id.editTeam1Player1Name);
        mEditTeam1Player2Name = (EditText)findViewById(R.id.editTeam1Player2Name);
        mEditTeam2Player1Name = (EditText)findViewById(R.id.editTeam2Player1Name);
        mEditTeam2Player2Name = (EditText)findViewById(R.id.editTeam2Player2Name);

        mStartGameButton = (Button)findViewById(R.id.button_start);

        mSwapTeam1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swapTeam1();
            }
        });

        mSwapTeam2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swapTeam2();
            }
        });

        mSwapTeamsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swapTeams();
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

        mAddress3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayerSelectionShown = 3;
                startSelectPlayerFromListActivity();
            }
        });

        mAddress4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayerSelectionShown = 4;
                startSelectPlayerFromListActivity();
            }
        });

    }


    private void swapTeam1()
    {
        String temp = mEditTeam1Player1Name.getText().toString();
        mEditTeam1Player1Name.setText(mEditTeam1Player2Name.getText().toString(), TextView.BufferType.EDITABLE);
        mEditTeam1Player2Name.setText(temp, TextView.BufferType.EDITABLE);
    }

    private void swapTeam2()
    {
        String temp = mEditTeam2Player1Name.getText().toString();
        mEditTeam2Player1Name.setText(mEditTeam2Player2Name.getText().toString(), TextView.BufferType.EDITABLE);
        mEditTeam2Player2Name.setText(temp, TextView.BufferType.EDITABLE);
    }

    private void swapTeams()
    {
        String temp1 = mEditTeam1Player1Name.getText().toString();
        String temp2 = mEditTeam1Player2Name.getText().toString();
        mEditTeam1Player1Name.setText(mEditTeam2Player1Name.getText().toString(), TextView.BufferType.EDITABLE);
        mEditTeam1Player2Name.setText(mEditTeam2Player2Name.getText().toString(), TextView.BufferType.EDITABLE);

        mEditTeam2Player1Name.setText(temp1, TextView.BufferType.EDITABLE);
        mEditTeam2Player2Name.setText(temp2, TextView.BufferType.EDITABLE);


    }


    private void startSelectPlayerFromListActivity()
    {
        SelectPlayerNameDialogFragment dialogFrag = new SelectPlayerNameDialogFragment();
        dialogFrag.show(getFragmentManager(), "Select double player names");
    }


    private void startGameSessionActivity()
    {
        Intent intent = new Intent(this, GameSessionActivity.class);
        intent.putExtra(GameSessionActivity.EXTRA_GAME_TYPE, false);
        intent.putExtra(GameSessionActivity.EXTRA_TEAM1_RIGHT_PLAYER_NAME, mEditTeam1Player1Name.getText().toString());
        intent.putExtra(GameSessionActivity.EXTRA_TEAM1_LEFT_PLAYER_NAME, mEditTeam1Player2Name.getText().toString());
        intent.putExtra(GameSessionActivity.EXTRA_TEAM2_RIGHT_PLAYER_NAME, mEditTeam2Player1Name.getText().toString());
        intent.putExtra(GameSessionActivity.EXTRA_TEAM2_LEFT_PLAYER_NAME, mEditTeam2Player2Name.getText().toString());

        startActivity(intent);
        finish();
    }

    @Override
    public void onNameSelected(SelectPlayerNameDialogFragment dialogFragment, String playerName) {
        if (mPlayerSelectionShown == 1)
        {
            mEditTeam1Player1Name.setText(playerName, TextView.BufferType.EDITABLE);
        }
        else if (mPlayerSelectionShown == 2)
        {
            mEditTeam1Player2Name.setText(playerName, TextView.BufferType.EDITABLE);
        }
        if (mPlayerSelectionShown == 3)
        {
            mEditTeam2Player1Name.setText(playerName, TextView.BufferType.EDITABLE);
        }
        else if (mPlayerSelectionShown == 4)
        {
            mEditTeam2Player2Name.setText(playerName, TextView.BufferType.EDITABLE);
        }
    }

}
