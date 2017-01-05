package com.haojie.badmintonscorecounter;

import android.content.Context;
import android.graphics.BitmapFactory;
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

    public ManagePlayerArrayAdapter(Context context, List<Player> players) {
        super(context, 0, players);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if(row == null) {
            row = LayoutInflater.from(getContext()).inflate(R.layout.manage_player_list_item, parent, false);
        }
        final Player player = getItem(position);
        ImageView playerImage = (ImageView)row.findViewById(R.id.player_image);

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
