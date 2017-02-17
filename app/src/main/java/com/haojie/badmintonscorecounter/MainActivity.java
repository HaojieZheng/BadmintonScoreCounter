package com.haojie.badmintonscorecounter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startSinglesButton = (Button)findViewById(R.id.button_singles);
        Button startDoublesButton = (Button)findViewById(R.id.button_doubles);
        Button managePlayersButton = (Button)findViewById(R.id.button_manage_players);

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

        managePlayersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ManagePlayersActivity.class);
                startActivity(intent);
            }
        });

    }
}
