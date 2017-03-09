package com.haojie.badmintonscorecounter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

import static com.haojie.badmintonscorecounter.R.drawable.ic_action_name;

public class EnterDoublesPlayersNamesActivity extends AppCompatActivity implements SelectPlayerNameDialogFragment.SelectPlayerNameClickHandler, ViewUpdatePhotoDialogFragment.OnFragmentInteractionListener{

    private ImageButton mTakePhotoButton1;
    private ImageButton mTakePhotoButton2;
    private ImageButton mTakePhotoButton3;
    private ImageButton mTakePhotoButton4;

    private EditText mEditTeam1Player1Name;
    private EditText mEditTeam1Player2Name;
    private EditText mEditTeam2Player1Name;
    private EditText mEditTeam2Player2Name;

    private int mPlayerSelectionShown = 0;
    private static final int REQUEST_IMAGE_CAPTURE_1 = 1;
    private static final int REQUEST_IMAGE_CAPTURE_2 = 2;
    private static final int REQUEST_IMAGE_CAPTURE_3 = 3;
    private static final int REQUEST_IMAGE_CAPTURE_4 = 4;

    private Bitmap player1Picture = null;
    private Bitmap player2Picture = null;
    private Bitmap player3Picture = null;
    private Bitmap player4Picture = null;

    private ViewUpdatePhotoDialogFragment mDialogFragment;
    private String mTempPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_doubles_players_names);

        ImageButton swapTeam1Button = (ImageButton) findViewById(R.id.swap_team1_button);
        ImageButton swapTeam2Button = (ImageButton) findViewById(R.id.swap_team2_button);
        ImageButton swapTeamsButton = (ImageButton) findViewById(R.id.swap_teams_button);

        ImageButton address1 = (ImageButton) findViewById(R.id.address_book1);
        ImageButton address2 = (ImageButton) findViewById(R.id.address_book2);
        ImageButton address3 = (ImageButton) findViewById(R.id.address_book3);
        ImageButton address4 = (ImageButton) findViewById(R.id.address_book4);

        Database database = new Database();
        database.deserialize(EnterDoublesPlayersNamesActivity.this);
        if (database.getPlayersWithoutDefault().size() == 0)
        {
            BitmapUtils.setImageButtonEnabled(this, false, address1, ic_action_name);
            BitmapUtils.setImageButtonEnabled(this, false, address2, ic_action_name);
            BitmapUtils.setImageButtonEnabled(this, false, address3, ic_action_name);
            BitmapUtils.setImageButtonEnabled(this, false, address4, ic_action_name);
        }

        mTakePhotoButton1 = (ImageButton)findViewById(R.id.take_photo1);
        mTakePhotoButton2 = (ImageButton)findViewById(R.id.take_photo2);
        mTakePhotoButton3 = (ImageButton)findViewById(R.id.take_photo3);
        mTakePhotoButton4 = (ImageButton)findViewById(R.id.take_photo4);

        mEditTeam1Player1Name = (EditText)findViewById(R.id.editTeam1Player1Name);
        mEditTeam1Player2Name = (EditText)findViewById(R.id.editTeam1Player2Name);
        mEditTeam2Player1Name = (EditText)findViewById(R.id.editTeam2Player1Name);
        mEditTeam2Player2Name = (EditText)findViewById(R.id.editTeam2Player2Name);

        Button startGameButton = (Button) findViewById(R.id.button_start);

        swapTeam1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swapTeam1();
            }
        });

        swapTeam2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swapTeam2();
            }
        });

        swapTeamsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swapTeams();
            }
        });


        startGameButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                startGameSessionActivity();
            }
        });

        address1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayerSelectionShown = 1;
                startSelectPlayerFromListActivity();
            }
        });

        address2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayerSelectionShown = 2;
                startSelectPlayerFromListActivity();
            }
        });

        address3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayerSelectionShown = 3;
                startSelectPlayerFromListActivity();
            }
        });

        address4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayerSelectionShown = 4;
                startSelectPlayerFromListActivity();
            }
        });

        mTakePhotoButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPhotoButton(player1Picture, REQUEST_IMAGE_CAPTURE_1);
            }
        });

        mTakePhotoButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPhotoButton(player2Picture, REQUEST_IMAGE_CAPTURE_2);
            }
        });

        mTakePhotoButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPhotoButton(player3Picture, REQUEST_IMAGE_CAPTURE_3);
            }
        });

        mTakePhotoButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPhotoButton(player4Picture, REQUEST_IMAGE_CAPTURE_4);
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


    private void onClickPhotoButton(Bitmap bitmap, int code)
    {
        if (bitmap != null)
        {
            mTempPath = Database.writeBitmapToDisk(bitmap);
            mDialogFragment = new ViewUpdatePhotoDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putString(ViewUpdatePhotoDialogFragment.ARG_PHOTO_PATH, mTempPath);
            bundle.putString(ViewUpdatePhotoDialogFragment.ARG_PLAYER_NAME, Integer.toString(code));
            mDialogFragment.setArguments(bundle);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(mDialogFragment, "Manage player image");
            ft.commit();
        }
        else
        {

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, code);
            }
        }

    }


    private void startGameSessionActivity()
    {
    // save the player names first
        Database database = new Database();
        try {
            database.deserialize(EnterDoublesPlayersNamesActivity.this);

            String player1Name = mEditTeam1Player1Name.getText().toString();
            database.updateOrAddPlayer(player1Name, player1Picture);

            String player2Name = mEditTeam1Player2Name.getText().toString();
            database.updateOrAddPlayer(player2Name, player2Picture);

            String player3Name = mEditTeam1Player2Name.getText().toString();
            database.updateOrAddPlayer(player3Name, player3Picture);

            String player4Name = mEditTeam1Player2Name.getText().toString();
            database.updateOrAddPlayer(player4Name, player4Picture);

            database.serialize(EnterDoublesPlayersNamesActivity.this);

        }
        catch (IOException ignored)
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
        else if (requestCode == REQUEST_IMAGE_CAPTURE_3 && resultCode == RESULT_OK) {
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
        else
            mDialogFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onNameSelected(SelectPlayerNameDialogFragment dialogFragment, String playerName) {

        Database database = new Database();
        database.deserialize(this);
        Player player = database.getPlayerWithName(playerName);

        if (mPlayerSelectionShown == 1) {
            mEditTeam1Player1Name.setText(playerName, TextView.BufferType.EDITABLE);
            if (player.getImagePath() != null) {
                player1Picture = BitmapFactory.decodeFile(player.getImagePath());
                mTakePhotoButton1.setImageBitmap(BitmapUtils.resizePhotoToButtonSize(player1Picture));
            }
        } else if (mPlayerSelectionShown == 2) {
            mEditTeam1Player2Name.setText(playerName, TextView.BufferType.EDITABLE);
            if (player.getImagePath() != null) {
                player2Picture = BitmapFactory.decodeFile(player.getImagePath());
                mTakePhotoButton2.setImageBitmap(BitmapUtils.resizePhotoToButtonSize(player2Picture));
            }
        } else if (mPlayerSelectionShown == 3) {
            mEditTeam2Player1Name.setText(playerName, TextView.BufferType.EDITABLE);
            if (player.getImagePath() != null) {
                player3Picture = BitmapFactory.decodeFile(player.getImagePath());
                mTakePhotoButton3.setImageBitmap(BitmapUtils.resizePhotoToButtonSize(player3Picture));
            }
        } else if (mPlayerSelectionShown == 4) {
            mEditTeam2Player2Name.setText(playerName, TextView.BufferType.EDITABLE);
            if (player.getImagePath() != null) {
                player4Picture = BitmapFactory.decodeFile(player.getImagePath());
                mTakePhotoButton4.setImageBitmap(BitmapUtils.resizePhotoToButtonSize(player4Picture));
            }
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored") // can't do anything if the temp file cannot be deleted
    @Override
    public void onDismiss(String code) {
        // refresh the photo
        int playerNumber = Integer.parseInt(code);

        if (playerNumber == 1) {
            player1Picture = BitmapFactory.decodeFile(mTempPath);
            mTakePhotoButton1.setImageBitmap(BitmapUtils.resizePhotoToButtonSize(player1Picture));
        }
        else if (playerNumber == 2)
        {
            player2Picture = BitmapFactory.decodeFile(mTempPath);
            mTakePhotoButton2.setImageBitmap(BitmapUtils.resizePhotoToButtonSize(player2Picture));
        }
        else if (playerNumber == 3) {
            player3Picture = BitmapFactory.decodeFile(mTempPath);
            mTakePhotoButton3.setImageBitmap(BitmapUtils.resizePhotoToButtonSize(player3Picture));
        }
        else if (playerNumber == 4)
        {
            player4Picture = BitmapFactory.decodeFile(mTempPath);
            mTakePhotoButton4.setImageBitmap(BitmapUtils.resizePhotoToButtonSize(player4Picture));
        }

        new File(mTempPath).delete();
    }

}
