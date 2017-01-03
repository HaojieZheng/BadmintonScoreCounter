package com.haojie.badmintonscorecounter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;

public class EnterDoublesPlayersNamesActivity extends AppCompatActivity implements SelectPlayerNameDialogFragment.SelectPlayerNameClickHandler{

    ImageButton mSwapTeam1Button;
    ImageButton mSwapTeam2Button;
    ImageButton mSwapTeamsButton;
    ImageButton mAddress1;
    ImageButton mAddress2;
    ImageButton mAddress3;
    ImageButton mAddress4;
    ImageButton mTakePhotoButton1;
    ImageButton mTakePhotoButton2;
    ImageButton mTakePhotoButton3;
    ImageButton mTakePhotoButton4;

    EditText mEditTeam1Player1Name;
    EditText mEditTeam1Player2Name;
    EditText mEditTeam2Player1Name;
    EditText mEditTeam2Player2Name;
    Button mStartGameButton;

    int mPlayerSelectionShown = 0;
    static final int REQUEST_IMAGE_CAPTURE_1 = 1;
    static final int REQUEST_IMAGE_CAPTURE_2 = 2;
    static final int REQUEST_IMAGE_CAPTURE_3 = 3;
    static final int REQUEST_IMAGE_CAPTURE_4 = 4;

    Bitmap player1Picture = null;
    Bitmap player2Picture = null;
    Bitmap player3Picture = null;
    Bitmap player4Picture = null;


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

        mTakePhotoButton1 = (ImageButton)findViewById(R.id.takephoto1);
        mTakePhotoButton2 = (ImageButton)findViewById(R.id.takephoto2);
        mTakePhotoButton3 = (ImageButton)findViewById(R.id.takephoto3);
        mTakePhotoButton4 = (ImageButton)findViewById(R.id.takephoto4);

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

        mTakePhotoButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE_1);
                }
            }
        });

        mTakePhotoButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE_2);
                }
            }
        });

        mTakePhotoButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE_3);
                }
            }
        });

        mTakePhotoButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE_4);
                }
            }
        });

    }


    private void refreshButtonImage()
    {
        if (player1Picture != null)
        {
            mTakePhotoButton1.setImageBitmap(BitmapUtils.resizePhotoToButtonSize(player1Picture));
        }
        else
        {
            mTakePhotoButton1.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_take_photo, null));
        }

        if (player2Picture != null)
        {
            mTakePhotoButton2.setImageBitmap(BitmapUtils.resizePhotoToButtonSize(player2Picture));
        }
        else
        {
            mTakePhotoButton2.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_take_photo, null));
        }

        if (player3Picture != null)
        {
            mTakePhotoButton3.setImageBitmap(BitmapUtils.resizePhotoToButtonSize(player3Picture));
        }
        else
        {
            mTakePhotoButton3.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_take_photo, null));
        }

        if (player4Picture != null)
        {
            mTakePhotoButton4.setImageBitmap(BitmapUtils.resizePhotoToButtonSize(player4Picture));
        }
        else
        {
            mTakePhotoButton4.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_take_photo, null));
        }
    }

    private void swapTeam1()
    {
        String temp = mEditTeam1Player1Name.getText().toString();
        mEditTeam1Player1Name.setText(mEditTeam1Player2Name.getText().toString(), TextView.BufferType.EDITABLE);
        mEditTeam1Player2Name.setText(temp, TextView.BufferType.EDITABLE);

        Bitmap tempPic = player1Picture;
        player1Picture = player2Picture;
        player2Picture = tempPic;

        refreshButtonImage();
    }

    private void swapTeam2()
    {
        String temp = mEditTeam2Player1Name.getText().toString();
        mEditTeam2Player1Name.setText(mEditTeam2Player2Name.getText().toString(), TextView.BufferType.EDITABLE);
        mEditTeam2Player2Name.setText(temp, TextView.BufferType.EDITABLE);

        Bitmap tempPic = player3Picture;
        player3Picture = player4Picture;
        player4Picture = tempPic;

        refreshButtonImage();
    }

    private void swapTeams()
    {
        String temp1 = mEditTeam1Player1Name.getText().toString();
        String temp2 = mEditTeam1Player2Name.getText().toString();
        Bitmap temp1Image = player1Picture;
        Bitmap temp2Image = player2Picture;

        mEditTeam1Player1Name.setText(mEditTeam2Player1Name.getText().toString(), TextView.BufferType.EDITABLE);
        mEditTeam1Player2Name.setText(mEditTeam2Player2Name.getText().toString(), TextView.BufferType.EDITABLE);
        player1Picture = player3Picture;
        player2Picture = player4Picture;

        mEditTeam2Player1Name.setText(temp1, TextView.BufferType.EDITABLE);
        mEditTeam2Player2Name.setText(temp2, TextView.BufferType.EDITABLE);
        player3Picture = temp1Image;
        player4Picture = temp2Image;

        refreshButtonImage();
    }


    private void startSelectPlayerFromListActivity()
    {
        SelectPlayerNameDialogFragment dialogFrag = new SelectPlayerNameDialogFragment();
        dialogFrag.show(getFragmentManager(), "Select double player names");
    }


    private static void updatePlayerInDatabase(Database database, String name, Bitmap picture)
    {
        Player player = database.getPlayerWithName(name);
        if (player == null)
        {
            player = new Player(name);

            if (picture != null)
            {
                String fileName = Database.writeBitmapToDisk(picture);
                player.setImagePath(fileName);
            }
            database.addPlayer(player);
        }
        else
        {
            // update the image for the player in the database
            if (picture != null)
            {
                String fileName = Database.writeBitmapToDisk(picture);
                player.setImagePath(fileName);
            }
            else
                player.setImagePath(null);
        }
    }


    private void startGameSessionActivity()
    {
    // save the player names first
        Database database = new Database();
        try {
            database.deserialize(EnterDoublesPlayersNamesActivity.this);

            String player1Name = mEditTeam1Player1Name.getText().toString();
            updatePlayerInDatabase(database, player1Name, player1Picture);

            String player2Name = mEditTeam1Player2Name.getText().toString();
            updatePlayerInDatabase(database, player2Name, player2Picture);

            String player3Name = mEditTeam1Player2Name.getText().toString();
            updatePlayerInDatabase(database, player3Name, player3Picture);

            String player4Name = mEditTeam1Player2Name.getText().toString();
            updatePlayerInDatabase(database, player4Name, player4Picture);

            database.serialize(EnterDoublesPlayersNamesActivity.this);

        }
        catch (IOException e)
        {

        }


        Intent intent = new Intent(this, GameSessionActivity.class);
        intent.putExtra(GameSessionActivity.EXTRA_GAME_TYPE, false);
        intent.putExtra(GameSessionActivity.EXTRA_TEAM1_RIGHT_PLAYER_NAME, mEditTeam1Player1Name.getText().toString());
        intent.putExtra(GameSessionActivity.EXTRA_TEAM1_LEFT_PLAYER_NAME, mEditTeam1Player2Name.getText().toString());
        intent.putExtra(GameSessionActivity.EXTRA_TEAM2_RIGHT_PLAYER_NAME, mEditTeam2Player1Name.getText().toString());
        intent.putExtra(GameSessionActivity.EXTRA_TEAM2_LEFT_PLAYER_NAME, mEditTeam2Player2Name.getText().toString());

        startActivity(intent);
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE_1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            player1Picture = BitmapUtils.resizeAndCropPhoto(imageBitmap);
            mTakePhotoButton1.setImageBitmap(BitmapUtils.resizePhotoToButtonSize(player1Picture));
        }
        else if (requestCode == REQUEST_IMAGE_CAPTURE_2 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            player2Picture = BitmapUtils.resizeAndCropPhoto(imageBitmap);
            mTakePhotoButton2.setImageBitmap(BitmapUtils.resizePhotoToButtonSize(player2Picture));
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE_3 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            player3Picture = BitmapUtils.resizeAndCropPhoto(imageBitmap);
            mTakePhotoButton3.setImageBitmap(BitmapUtils.resizePhotoToButtonSize(player3Picture));
        }
        else if (requestCode == REQUEST_IMAGE_CAPTURE_4 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            player4Picture = BitmapUtils.resizeAndCropPhoto(imageBitmap);
            mTakePhotoButton4.setImageBitmap(BitmapUtils.resizePhotoToButtonSize(player4Picture));
        }
    }

    @Override
    public void onNameSelected(SelectPlayerNameDialogFragment dialogFragment, String playerName) {

        Database database = new Database();
        database.deserialize(this);
        Player player = database.getPlayerWithName(playerName);

        if (mPlayerSelectionShown == 1)
        {
            mEditTeam1Player1Name.setText(playerName, TextView.BufferType.EDITABLE);
            if (player.getImagePath() != null)
            {
                player1Picture = BitmapFactory.decodeFile(player.getImagePath());
                mTakePhotoButton1.setImageBitmap(BitmapUtils.resizePhotoToButtonSize(player1Picture));
            }
        }
        else if (mPlayerSelectionShown == 2)
        {
            mEditTeam1Player2Name.setText(playerName, TextView.BufferType.EDITABLE);
            if (player.getImagePath() != null)
            {
                player2Picture = BitmapFactory.decodeFile(player.getImagePath());
                mTakePhotoButton2.setImageBitmap(BitmapUtils.resizePhotoToButtonSize(player2Picture));
            }
        }
        else if (mPlayerSelectionShown == 3)
        {
            mEditTeam2Player1Name.setText(playerName, TextView.BufferType.EDITABLE);
            if (player.getImagePath() != null)
            {
                player3Picture = BitmapFactory.decodeFile(player.getImagePath());
                mTakePhotoButton3.setImageBitmap(BitmapUtils.resizePhotoToButtonSize(player3Picture));
            }
        }
        else if (mPlayerSelectionShown == 4)
        {
            mEditTeam2Player2Name.setText(playerName, TextView.BufferType.EDITABLE);
            if (player.getImagePath() != null)
            {
                player4Picture = BitmapFactory.decodeFile(player.getImagePath());
                mTakePhotoButton4.setImageBitmap(BitmapUtils.resizePhotoToButtonSize(player4Picture));
            }
        }


    }

}
