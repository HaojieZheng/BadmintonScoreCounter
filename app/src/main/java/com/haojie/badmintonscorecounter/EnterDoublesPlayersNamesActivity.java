package com.haojie.badmintonscorecounter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

import static com.haojie.badmintonscorecounter.R.drawable.ic_action_name;

public class EnterDoublesPlayersNamesActivity extends AppCompatActivity implements SelectPlayerNameDialogFragment.SelectPlayerNameClickHandler, ViewUpdatePhotoDialogFragment.OnFragmentInteractionListener{

    private final ImageButton[] mTakePhotoButtons = new ImageButton[4];
    private final Bitmap[] playerPictures = new Bitmap[4];


    private EditText mEditTeam1Player1Name;
    private EditText mEditTeam1Player2Name;
    private EditText mEditTeam2Player1Name;
    private EditText mEditTeam2Player2Name;

    private int mPlayerSelectionShown = 0;



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

        mTakePhotoButtons[0] = (ImageButton)findViewById(R.id.take_photo1);
        mTakePhotoButtons[1] = (ImageButton)findViewById(R.id.take_photo2);
        mTakePhotoButtons[2] = (ImageButton)findViewById(R.id.take_photo3);
        mTakePhotoButtons[3] = (ImageButton)findViewById(R.id.take_photo4);

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

        for (int i = 0; i < 4; i++)
        {
            initTakePhotoButton(mTakePhotoButtons[i], i);
        }

    }


    private void initTakePhotoButton(final ImageButton button, final int id)
    {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPhotoButton(button, playerPictures[id], id + 1);
            }
        });

        registerForContextMenu(button);
    }

    private void refreshButtonImage()
    {
        for (int i = 0; i < 4; i++)
        {
            if (playerPictures[i] != null)
            {
                mTakePhotoButtons[i].setImageBitmap(BitmapUtils.resizePhotoToButtonSize(playerPictures[i]));
            }
            else
            {
                mTakePhotoButtons[i].setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_take_photo, null));
            }
        }
    }

    private void swapTeam1()
    {
        String temp = mEditTeam1Player1Name.getText().toString();
        mEditTeam1Player1Name.setText(mEditTeam1Player2Name.getText().toString(), TextView.BufferType.EDITABLE);
        mEditTeam1Player2Name.setText(temp, TextView.BufferType.EDITABLE);

        Bitmap tempPic = playerPictures[0];
        playerPictures[0] = playerPictures[1];
        playerPictures[1] = tempPic;

        refreshButtonImage();
    }

    private void swapTeam2()
    {
        String temp = mEditTeam2Player1Name.getText().toString();
        mEditTeam2Player1Name.setText(mEditTeam2Player2Name.getText().toString(), TextView.BufferType.EDITABLE);
        mEditTeam2Player2Name.setText(temp, TextView.BufferType.EDITABLE);

        Bitmap tempPic = playerPictures[2];
        playerPictures[2] = playerPictures[3];
        playerPictures[3] = tempPic;

        refreshButtonImage();
    }

    private void swapTeams()
    {
        String temp1 = mEditTeam1Player1Name.getText().toString();
        String temp2 = mEditTeam1Player2Name.getText().toString();
        Bitmap temp1Image = playerPictures[0];
        Bitmap temp2Image = playerPictures[1];

        mEditTeam1Player1Name.setText(mEditTeam2Player1Name.getText().toString(), TextView.BufferType.EDITABLE);
        mEditTeam1Player2Name.setText(mEditTeam2Player2Name.getText().toString(), TextView.BufferType.EDITABLE);
        playerPictures[0] = playerPictures[2];
        playerPictures[1] = playerPictures[3];

        mEditTeam2Player1Name.setText(temp1, TextView.BufferType.EDITABLE);
        mEditTeam2Player2Name.setText(temp2, TextView.BufferType.EDITABLE);
        playerPictures[2] = temp1Image;
        playerPictures[3] = temp2Image;

        refreshButtonImage();
    }


    private void startSelectPlayerFromListActivity()
    {
        SelectPlayerNameDialogFragment dialogFrag = new SelectPlayerNameDialogFragment();
        dialogFrag.show(getFragmentManager(), "Select double player names");
    }


    private void onClickPhotoButton(View button, Bitmap bitmap, int code)
    {
        if (bitmap != null)
        {
            mTempPath = Database.writeBitmapToDisk(bitmap);
            ViewUpdatePhotoDialogFragment dialogFragment = new ViewUpdatePhotoDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putString(ViewUpdatePhotoDialogFragment.ARG_PHOTO_PATH, mTempPath);
            bundle.putString(ViewUpdatePhotoDialogFragment.ARG_PLAYER_NAME, Integer.toString(code));
            dialogFragment.setArguments(bundle);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(dialogFragment, "Manage player image");
            ft.commit();
        }
        else
        {
            openContextMenu(button);
        }
    }

    private static final int TAKE_PHOTO = 0x0100;
    private static final int CHOOSE_FROM_GALLERY = 0x0200;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add((byte)v.getId(), TAKE_PHOTO, 0, getString(R.string.take_photo_menu_item));
        menu.add((byte)v.getId(), CHOOSE_FROM_GALLERY, 0, getString(R.string.choose_from_gallery_menu_item));
    }


    @Override
    public boolean onContextItemSelected(MenuItem item){
        if(item.getItemId() == TAKE_PHOTO){
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, TAKE_PHOTO | (byte)item.getGroupId());
            }
        }
        else if(item.getItemId()== CHOOSE_FROM_GALLERY){
            Intent intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            intent.setType("image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("scale", true);
            intent.putExtra("outputX", BitmapUtils.PROFILE_DIMENSION_X);
            intent.putExtra("outputY", BitmapUtils.PROFILE_DIMENSION_Y);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, CHOOSE_FROM_GALLERY | (byte)item.getGroupId());
        }else{
            return false;
        }
        return true;
    }


    private void startGameSessionActivity()
    {
    // save the player names first
        Database database = new Database();
        try {
            database.deserialize(EnterDoublesPlayersNamesActivity.this);

            String player1Name = mEditTeam1Player1Name.getText().toString();
            database.updateOrAddPlayer(player1Name, playerPictures[0]);

            String player2Name = mEditTeam1Player2Name.getText().toString();
            database.updateOrAddPlayer(player2Name, playerPictures[1]);

            String player3Name = mEditTeam1Player2Name.getText().toString();
            database.updateOrAddPlayer(player3Name, playerPictures[2]);

            String player4Name = mEditTeam1Player2Name.getText().toString();
            database.updateOrAddPlayer(player4Name, playerPictures[3]);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        for (int i = 0; i < 4; i++)
        {
            if (requestCode == (TAKE_PHOTO | (byte)mTakePhotoButtons[i].getId()) && resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                playerPictures[i] = BitmapUtils.resizeAndCropPhoto(imageBitmap);
                mTakePhotoButtons[i].setImageBitmap(BitmapUtils.resizePhotoToButtonSize(playerPictures[i]));
                return;
            }
            else if (requestCode == (CHOOSE_FROM_GALLERY | (byte)mTakePhotoButtons[i].getId()) && resultCode == RESULT_OK) {
                final Bundle extras = data.getExtras();
                if (extras != null) {
                    playerPictures[i] = extras.getParcelable("data");
                    mTakePhotoButtons[i].setImageBitmap(BitmapUtils.resizePhotoToButtonSize(playerPictures[i]));
                    return;
                }
            }
        }
    }

    @Override
    public void onNameSelected(String playerName) {

        Database database = new Database();
        database.deserialize(this);
        Player player = database.getPlayerWithName(playerName);

        if (mPlayerSelectionShown == 1) {
            mEditTeam1Player1Name.setText(playerName, TextView.BufferType.EDITABLE);
            if (player.getImagePath() != null) {
                playerPictures[0] = BitmapFactory.decodeFile(player.getImagePath());
                mTakePhotoButtons[0].setImageBitmap(BitmapUtils.resizePhotoToButtonSize(playerPictures[0]));
            }
        } else if (mPlayerSelectionShown == 2) {
            mEditTeam1Player2Name.setText(playerName, TextView.BufferType.EDITABLE);
            if (player.getImagePath() != null) {
                playerPictures[1] = BitmapFactory.decodeFile(player.getImagePath());
                mTakePhotoButtons[1].setImageBitmap(BitmapUtils.resizePhotoToButtonSize(playerPictures[1]));
            }
        } else if (mPlayerSelectionShown == 3) {
            mEditTeam2Player1Name.setText(playerName, TextView.BufferType.EDITABLE);
            if (player.getImagePath() != null) {
                playerPictures[2] = BitmapFactory.decodeFile(player.getImagePath());
                mTakePhotoButtons[2].setImageBitmap(BitmapUtils.resizePhotoToButtonSize(playerPictures[2]));
            }
        } else if (mPlayerSelectionShown == 4) {
            mEditTeam2Player2Name.setText(playerName, TextView.BufferType.EDITABLE);
            if (player.getImagePath() != null) {
                playerPictures[3] = BitmapFactory.decodeFile(player.getImagePath());
                mTakePhotoButtons[3].setImageBitmap(BitmapUtils.resizePhotoToButtonSize(playerPictures[3]));
            }
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored") // can't do anything if the temp file cannot be deleted
    @Override
    public void onDismiss(String code) {
        // refresh the photo
        int playerNumber = Integer.parseInt(code);

        playerPictures[playerNumber - 1] = BitmapFactory.decodeFile(mTempPath);
        mTakePhotoButtons[playerNumber - 1].setImageBitmap(BitmapUtils.resizePhotoToButtonSize(playerPictures[playerNumber - 1]));

        new File(mTempPath).delete();
    }

}
