package com.haojie.badmintonscorecounter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class EnterSinglesPlayersNames extends AppCompatActivity {

    ImageButton mSwapButton;
    EditText mEditPlayer1Name;
    EditText mEditPlayer2Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_singles_players_names);

        mSwapButton = (ImageButton)findViewById(R.id.swap_button);
        mEditPlayer1Name = (EditText)findViewById(R.id.editPlayer1Name);
        mEditPlayer2Name = (EditText)findViewById(R.id.editPlayer2Name);

        mSwapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = mEditPlayer1Name.getText().toString();
                mEditPlayer1Name.setText(mEditPlayer2Name.getText().toString(), TextView.BufferType.EDITABLE);
                mEditPlayer2Name.setText(temp, TextView.BufferType.EDITABLE);
            }
        });
    }
}
