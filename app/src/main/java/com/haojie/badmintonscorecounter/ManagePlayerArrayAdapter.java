package com.haojie.badmintonscorecounter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

/**
 * Created by Haojie on 1/5/2017.
 */

public class ManagePlayerArrayAdapter extends ArrayAdapter<Player> {

    public ManagePlayerArrayAdapter(Context context, List<Player> players, AppCompatActivity activity) {
        super(context, 0, players);
        mActivity = activity;
    }

    AppCompatActivity mActivity;

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        View row = convertView;
        if(row == null) {
            row = LayoutInflater.from(getContext()).inflate(R.layout.manage_player_list_item, parent, false);
        }
        final Player player = getItem(position);
        ImageView playerImage = (ImageView)row.findViewById(R.id.player_image);

        playerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player.getImagePath() == null)
                    return;

                ViewUpdatePhotoDialogFragment fr = new ViewUpdatePhotoDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString(ViewUpdatePhotoDialogFragment.ARG_PLAYER_NAME, player.getName());
                fr.setArguments(bundle);

                FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
                ft.add(fr, "Manage player image");
                ft.commit();
            }
        });

        String path = player.getImagePath();
        if (path != null) {
            playerImage.setImageBitmap(BitmapUtils.resizePhotoToButtonSize(player.getImage()));
        }

        TextView playerName = (TextView)row.findViewById(R.id.player_name);
        playerName.setText(player.getName(), TextView.BufferType.EDITABLE);

        ImageButton imageButton = (ImageButton)row.findViewById(R.id.delete_button);
        imageButton.setImageBitmap(BitmapFactory.decodeResource(row.getResources(), R.drawable.ic_delete));
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Database database = new Database();
                database.deserialize(v.getContext());
                database.removePlayer(player.getName());
                try
                {
                    database.serialize(v.getContext());
                }
                catch (IOException e)
                {
                    // TODO: display error
                }

                remove(player);
                notifyDataSetChanged();

            }
        });
        return row;
    }
}
