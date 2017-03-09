package com.haojie.badmintonscorecounter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.IOException;
import java.util.Locale;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class GameSessionActivity extends AppCompatActivity {

    // Controls
    private CourtView mCourtView;
    private TextView mTeam1ScoreLabel;
    private TextView mTeam2ScoreLabel;
    private TextView mWinningLabel;
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
    private static final String TAG = "GameSessionActivity";

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
    private TextToSpeech mTts;

    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
        }
    };


    @Override
    public void onBackPressed() {
        stopTts();

        if (mGame.getWinner()  == 0)
        {
            new AlertDialog.Builder(this)
                    .setMessage("Game not finished.\nAre you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            freeTts();
                            GameSessionActivity.this.finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
        else
        {
            GameSessionActivity.this.finish();
        }


    }


    private void stopTts()
    {
        if (mTts != null) {
            mTts.stop();
        }

    }

    private void freeTts()
    {
        if (mTts != null)
        {
            mTts.stop();
            mTts.shutdown();
            mTts = null;
        }
    }


    @Override
    protected void onDestroy() {

        freeTts();
        Database database = new Database();
        try {
            database.deserialize(this);
            database.addGame(mGame);

            database.serialize(this);
        } catch (IOException e) {
            Log.d(TAG, "Cannot write to database");
        }

        super.onDestroy();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        initUI();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean isSingles = true;
        String team1RightPlayer = "";
        String team2RightPlayer = "";
        String team1LeftPlayer = "";
        String team2LeftPlayer = "";

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                isSingles = extras.getBoolean(EXTRA_GAME_TYPE);
                team1RightPlayer = extras.getString(EXTRA_TEAM1_RIGHT_PLAYER_NAME);
                team2RightPlayer = extras.getString(EXTRA_TEAM2_RIGHT_PLAYER_NAME);

                if (!isSingles) {
                    team1LeftPlayer = extras.getString(EXTRA_TEAM1_LEFT_PLAYER_NAME);
                    team2LeftPlayer = extras.getString(EXTRA_TEAM2_LEFT_PLAYER_NAME);
                }
            }
        } else {
            isSingles = (boolean) savedInstanceState.getSerializable(EXTRA_GAME_TYPE);
            team1RightPlayer = (String) savedInstanceState.getSerializable(EXTRA_TEAM1_RIGHT_PLAYER_NAME);
            team2RightPlayer = (String) savedInstanceState.getSerializable(EXTRA_TEAM2_RIGHT_PLAYER_NAME);

            if (!isSingles) {
                team1LeftPlayer = (String) savedInstanceState.getSerializable(EXTRA_TEAM1_LEFT_PLAYER_NAME);
                team2LeftPlayer = (String) savedInstanceState.getSerializable(EXTRA_TEAM2_LEFT_PLAYER_NAME);
            }
        }

        Database database = new Database();
        database.deserialize(this);

        mGame = new Game(isSingles ? Game.GameType.Singles : Game.GameType.Doubles, 1);
        mGame.setPlayer(Game.PlayerPosition.Team1Right, database.getPlayerWithName(team1RightPlayer));
        mGame.setPlayer(Game.PlayerPosition.Team2Right, database.getPlayerWithName(team2RightPlayer));
        if (!isSingles) {
            mGame.setPlayer(Game.PlayerPosition.Team1Left, database.getPlayerWithName(team1LeftPlayer));
            mGame.setPlayer(Game.PlayerPosition.Team2Left, database.getPlayerWithName(team2LeftPlayer));
        }

        initUI();
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }


    private void initUI() {
        setContentView(R.layout.activity_game_session);

        mVisible = true;
        mContentView = findViewById(R.id.fullscreen_content);


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        mCourtView = (CourtView) findViewById(R.id.court_view);
        mCourtView.registerListener(new CourtView.CourtViewTouchListener() {
            @Override
            public void onTeam1Score() {
                mGame.onTeam1Score();
                refreshScores(false);
            }

            @Override
            public void onTeam2Score() {
                mGame.onTeam2Score();
                refreshScores(false);
            }
        });

        mTeam1ScoreLabel = (TextView) findViewById(R.id.team1_score);
        mTeam2ScoreLabel = (TextView) findViewById(R.id.team2_score);
        mWinningLabel = (TextView) findViewById(R.id.winning_text_view);
        mWinningLabel.setVisibility(View.INVISIBLE);

        mUndoButton = (Button) findViewById(R.id.undo_button);
        mUndoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGame.undo();
                refreshScores(true);
            }
        });

        refreshScores(false);
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

    private void refreshScores(boolean isUndo) {
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

        mCourtView.setServicePosition(GamePresenter.PlayerPositionToPosition(mGame.getCurrentServer()));

        if (mGame.getWinner() != 0) {
            mWinningLabel.setText("Team " + mGame.getWinner() + " wins");
            mWinningLabel.setVisibility(View.VISIBLE);

        } else {
            mWinningLabel.setVisibility(View.INVISIBLE);
        }

        mCourtView.invalidate();

        final boolean isUndoCopy = isUndo;
        if (mTts == null) {
            mTts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status == TextToSpeech.SUCCESS) {
                        if (mTts.isLanguageAvailable(Locale.US) >= 0 &&
                            mTts.setLanguage(Locale.US) >= 0)
                        {
                            ConvertTextToSpeech(isUndoCopy);
                        }
                    }
                }
            });
        }
        else
        {
            ConvertTextToSpeech(isUndoCopy);
        }

    }

    private void ConvertTextToSpeech(boolean isUndo) {
        mTts.stop();
        mTts.speak(GamePresenter.getAnnouncementText(mGame, !isUndo), TextToSpeech.QUEUE_FLUSH, null);
    }


}
