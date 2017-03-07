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

public class EnterSinglesPlayersNamesActivity extends AppCompatActivity implements SelectPlayerNameDialogFragment.SelectPlayerNameClickHandler, ViewUpdatePhotoDialogFragment.OnFragmentInteractionListener{

    private ImageButton mTakePhotoButton1;
    private ImageButton mTakePhotoButton2;
    private EditText mEditPlayer1Name;
    private EditText mEditPlayer2Name;
    private int mPlayerSelectionShown = 0;
    private static final int REQUEST_IMAGE_CAPTURE_1 = 1;
    private static final int REQUEST_IMAGE_CAPTURE_2 = 2;
    private Bitmap player1Picture = null;
    private Bitmap player2Picture = null;
    private String mTempPath;
    private ViewUpdatePhotoDialogFragment mDialogFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_singles_players_names);

        ImageButton swapButton = (ImageButton) findViewById(R.id.swap_button);
        mTakePhotoButton1 = (ImageButton)findViewById(R.id.takephoto1);
        mTakePhotoButton2 = (ImageButton)findViewById(R.id.takephoto2);
        mEditPlayer1Name = (EditText)findViewById(R.id.editPlayer1Name);
        mEditPlayer2Name = (EditText)findViewById(R.id.editPlayer2Name);
        Button startGameButton = (Button) findViewById(R.id.button_start);
        ImageButton address1 = (ImageButton) findViewById(R.id.addressbook1);
        ImageButton address2 = (ImageButton) findViewById(R.id.addressbook2);

        Database database = new Database();
        database.deserialize(EnterSinglesPlayersNamesActivity.this);
        if (database.getPlayersWithoutDefault().size() == 0)
        {
            BitmapUtils.setImageButtonEnabled(this, false, address1, ic_action_name);
            BitmapUtils.setImageButtonEnabled(this, false, address2, ic_action_name);
        }


        swapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swap();
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

    }

    private void onClickPhotoButton(Bitmap bitmap, int code)
    {
        if (bitmap != null)
        {
            mTempPath = Database.writeBitmapToDisk(bitmap);
            mDialogFragment = new ViewUpdatePhotoDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putString(ViewUpdatePhotoDialogFragment.ARG_PHOTO_PATH, mTempPath);
            bundle.putString(ViewUpdatePhotoDialogFragment.ARG_PlAYER_NAME, Integer.toString(code));
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


    private void swap()
    {
        String temp = mEditPlayer1Name.getText().toString();
        mEditPlayer1Name.setText(mEditPlayer2Name.getText().toString(), TextView.BufferType.EDITABLE);
        mEditPlayer2Name.setText(temp, TextView.BufferType.EDITABLE);

        Bitmap tempPic = player1Picture;
        player1Picture = player2Picture;
        player2Picture = tempPic;

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
    }

    @Override
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
        else
            mDialogFragment.onActivityResult(requestCode, resultCode, data);
    }





    private void startGameSessionActivity()
    {

        // save the player names first
        Database database = new Database();
        try {
            database.deserialize(EnterSinglesPlayersNamesActivity.this);
            String player1Name = mEditPlayer1Name.getText().toString();
            database.updateOrAddPlayer(player1Name, player1Picture);

            String player2Name = mEditPlayer2Name.getText().toString();
            database.updateOrAddPlayer(player2Name, player2Picture);

            database.serialize(EnterSinglesPlayersNamesActivity.this);

        }
        catch (IOException ignored)
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

        Database database = new Database();
        database.deserialize(EnterSinglesPlayersNamesActivity.this);
        Player player = database.getPlayerWithName(playerName);

        if (mPlayerSelectionShown == 1)
        {
            mEditPlayer1Name.setText(playerName, TextView.BufferType.EDITABLE);
            if (player.getImagePath() != null)
            {
                player1Picture = BitmapFactory.decodeFile(player.getImagePath());
                mTakePhotoButton1.setImageBitmap(BitmapUtils.resizePhotoToButtonSize(player1Picture));
            }
        }
        else if (mPlayerSelectionShown == 2)
        {
            mEditPlayer2Name.setText(playerName, TextView.BufferType.EDITABLE);
            if (player.getImagePath() != null)
            {
                player2Picture = BitmapFactory.decodeFile(player.getImagePath());
                mTakePhotoButton2.setImageBitmap(BitmapUtils.resizePhotoToButtonSize(player2Picture));
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

        new File(mTempPath).delete();
    }
}
