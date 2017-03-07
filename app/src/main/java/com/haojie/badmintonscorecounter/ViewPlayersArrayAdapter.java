package com.haojie.badmintonscorecounter;

import android.os.Bundle;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
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

public class ViewPlayersArrayAdapter extends ArrayAdapter<Player> {

    public ViewPlayersArrayAdapter(Context context, List<Player> players, ViewPlayersActivity activity) {
        super(context, 0, players);
        mActivity = activity;
    }

    private final ViewPlayersActivity mActivity;

    @NonNull
    @Override
    public View getView(int position, final View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if(row == null) {
            row = LayoutInflater.from(getContext()).inflate(R.layout.view_player_list_item, parent, false);
        }
        final Player player = getItem(position);
        assert player != null;
        ImageView playerImage = (ImageView)row.findViewById(R.id.player_image);

        playerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player.getImagePath() == null)
                    return;

                ViewUpdatePhotoDialogFragment fr = new ViewUpdatePhotoDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString(ViewUpdatePhotoDialogFragment.ARG_PHOTO_PATH, player.getImagePath());
                bundle.putString(ViewUpdatePhotoDialogFragment.ARG_PlAYER_NAME, player.getName());
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
                mActivity.onDataSetChanged();

            }
        });
        return row;
    }
}
