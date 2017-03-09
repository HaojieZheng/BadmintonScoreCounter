package com.haojie.badmintonscorecounter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Haojie on 1/2/2017.
 * Adapter to show players on a ListView and is selectable
 */

public class SelectPlayerArrayAdapter extends ArrayAdapter<Player> {
    public SelectPlayerArrayAdapter(Context context, List<Player> players) {
        super(context, 0, players);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if(row == null) {
            row = LayoutInflater.from(getContext()).inflate(R.layout.player_name_list_item, parent, false);
        }
        Player player = getItem(position);
        assert player != null;

        ImageView playerImage = (ImageView)row.findViewById(R.id.player_image);

        String path = player.getImagePath();
        if (path != null) {
            playerImage.setImageBitmap(BitmapUtils.resizePhotoToButtonSize(player.getImage()));
        }

        TextView playerName = (TextView)row.findViewById(R.id.player_name);
        playerName.setText(player.getName(), TextView.BufferType.EDITABLE);

        return row;
    }


}
