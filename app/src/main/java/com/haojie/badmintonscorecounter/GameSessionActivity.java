package com.haojie.badmintonscorecounter;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class GameSessionActivity extends AppCompatActivity {

    // Controls
    private CourtView mCourtView;
    private View mControlsView;
    private TextView mTeam1ScoreLabel;
    private TextView mTeam2ScoreLabel;
    private View mContentView;
    private Button mUndoButton;

    private Game mGame;


    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    // static final variables
    public final static String EXTRA_GAME_TYPE = "com.Haojie.BadmintonScoreCounter.GameType";
    public final static String EXTRA_TEAM1_RIGHT_PLAYER_NAME = "com.Haojie.BadmintonScoreCounter.Team1RightPlayerName";
    public final static String EXTRA_TEAM2_RIGHT_PLAYER_NAME = "com.Haojie.BadmintonScoreCounter.Team2RightPlayerName";
    public final static String EXTRA_TEAM1_LEFT_PLAYER_NAME = "com.Haojie.BadmintonScoreCounter.Team1LeftPlayerName";
    public final static String EXTRA_TEAM2_LEFT_PLAYER_NAME = "com.Haojie.BadmintonScoreCounter.Team2LeftPlayerName";

    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            //mControlsView.setVisibility(View.VISIBLE);
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Database database = new Database();
        try {
            database.deserialize(this);
            database.addGame(mGame);

            database.serialize(this);
        }
        catch (IOException e)
        {

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Boolean isSingles = true;
        String team1RightPlayer = "";
        String team2RightPlayer = "";
        String team1LeftPlayer = "";
        String team2LeftPlayer = "";

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras != null)
            {
                isSingles = extras.getBoolean(EXTRA_GAME_TYPE);
                team1RightPlayer = extras.getString(EXTRA_TEAM1_RIGHT_PLAYER_NAME);
                team2RightPlayer = extras.getString(EXTRA_TEAM2_RIGHT_PLAYER_NAME);

                if (!isSingles)
                {
                    team1LeftPlayer = extras.getString(EXTRA_TEAM1_LEFT_PLAYER_NAME);
                    team2LeftPlayer = extras.getString(EXTRA_TEAM2_LEFT_PLAYER_NAME);
                }
            }
        }
        else {
            isSingles = (Boolean) savedInstanceState.getSerializable(EXTRA_GAME_TYPE);
            team1RightPlayer = (String)savedInstanceState.getSerializable(EXTRA_TEAM1_RIGHT_PLAYER_NAME);
            team2RightPlayer = (String)savedInstanceState.getSerializable(EXTRA_TEAM2_RIGHT_PLAYER_NAME);

            if (!isSingles)
            {
                team1LeftPlayer = (String)savedInstanceState.getSerializable(EXTRA_TEAM1_LEFT_PLAYER_NAME);
                team2LeftPlayer = (String)savedInstanceState.getSerializable(EXTRA_TEAM2_LEFT_PLAYER_NAME);
            }
        }

        Database database = new Database();
        database.deserialize(this);

        mGame = new Game(isSingles ? Game.GameType.Singles : Game.GameType.Doubles, 1);
        mGame.setPlayer(Game.PlayerPosition.Team1Right, database.getPlayerWithName(team1RightPlayer));
        mGame.setPlayer(Game.PlayerPosition.Team2Right, database.getPlayerWithName(team2RightPlayer));
        if (!isSingles)
        {
            mGame.setPlayer(Game.PlayerPosition.Team1Left, database.getPlayerWithName(team1LeftPlayer));
            mGame.setPlayer(Game.PlayerPosition.Team2Left, database.getPlayerWithName(team2LeftPlayer));
        }

        setContentView(R.layout.activity_game_session);

        mVisible = true;
        //mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        //findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);

        mCourtView = (CourtView)findViewById(R.id.court_view);
        mCourtView.registerListener(new CourtView.CourtViewTouchListener()
        {
            @Override
            public void onTeam1Score() {
                mGame.onTeam1Score();
                refreshScores();
            }

            @Override
            public void onTeam2Score()
            {
                mGame.onTeam2Score();
                refreshScores();
            }
        });

        mTeam1ScoreLabel = (TextView)findViewById(R.id.team1_score);
        mTeam2ScoreLabel = (TextView)findViewById(R.id.team2_score);

        mUndoButton = (Button)findViewById(R.id.undo_button);
        mUndoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGame.undo();
                refreshScores();
            }
        });

        refreshScores();
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    private void refreshScores()
    {
        mTeam1ScoreLabel.setText(Integer.toString(mGame.getTeam1Score()), TextView.BufferType.EDITABLE);
        mTeam2ScoreLabel.setText(Integer.toString(mGame.getTeam2Score()), TextView.BufferType.EDITABLE);
        if (mGame.getTeam1Score() > mGame.getTeam2Score())
            mTeam1ScoreLabel.setTypeface(null, Typeface.BOLD);
        else
            mTeam1ScoreLabel.setTypeface(null, Typeface.NORMAL);

        if (mGame.getTeam1Score() < mGame.getTeam2Score())
            mTeam2ScoreLabel.setTypeface(null, Typeface.BOLD);
        else
            mTeam2ScoreLabel.setTypeface(null, Typeface.NORMAL);

        mUndoButton.setEnabled(mGame.isUndoable());

        Player playerTopLeft = mGame.getPlayer(Game.PlayerPosition.Team1Right);
        Player playerTopRight = mGame.getPlayer(Game.PlayerPosition.Team1Left);
        Player playerBottomLeft = mGame.getPlayer(Game.PlayerPosition.Team2Left);
        Player playerBottomRight = mGame.getPlayer(Game.PlayerPosition.Team2Right);

        mCourtView.setTopLeftName(playerTopLeft != null ? playerTopLeft.getName() : "");
        mCourtView.setTopRightName(playerTopRight != null ? playerTopRight.getName() : "");
        mCourtView.setBottomLeftName(playerBottomLeft != null ? playerBottomLeft.getName() : "");
        mCourtView.setBottomRightName(playerBottomRight != null ? playerBottomRight.getName() : "");

        mCourtView.setTopLeftPic(playerTopLeft != null ? playerTopLeft.getImage() : null);
        mCourtView.setTopRightPic(playerTopRight != null ? playerTopRight.getImage() : null);
        mCourtView.setBottomLeftPic(playerBottomLeft != null ? playerBottomLeft.getImage() : null);
        mCourtView.setBottomRightPic(playerBottomRight != null ? playerBottomRight.getImage() : null);


        mCourtView.setServicePosition(PlayerPositionToPosition(mGame.getCurrentServer()));

        mCourtView.invalidate();

    }

    private static CourtView.Position PlayerPositionToPosition(Game.PlayerPosition p)
    {
        switch (p)
        {
            case Team1Left:
                return CourtView.Position.TopRight;
            case Team1Right:
                return CourtView.Position.TopLeft;
            case Team2Left:
                return CourtView.Position.BottomLeft;
            case Team2Right:
                return CourtView.Position.BottomRight;
        }

        return CourtView.Position.None;
    }

}
