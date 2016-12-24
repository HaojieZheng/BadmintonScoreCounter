package com.haojie.badmintonscorecounter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button mStartSinglesButton;
    private Button mStartDoublesButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStartSinglesButton = (Button)findViewById(R.id.button_singles);
        mStartDoublesButton = (Button)findViewById(R.id.button_doubles);

        mStartSinglesButton.setOnClickListener(new View.OnClickListener()
        {
           @Override
            public void onClick(View v)
           {
               /*Intent intent = new Intent(MainActivity.this, EnterSinglePlayersNameActivity.class);
               startActivity(intent);*/
           }
        });
    }
}
