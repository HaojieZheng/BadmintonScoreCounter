package com.haojie.badmintonscorecounter;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Haojie on 12/30/2016.
 */

public class SelectPlayerNameActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> test = new HashMap<String, String>();
        test.put("player_name", "Haojie");
        fillMaps.add(test);

        String[] from = new String[]{"player_name"};
        int[] to = new int[]{R.id.player_name};

        SimpleAdapter adapter = new SimpleAdapter(this, fillMaps, R.layout.player_name_list_item, from, to);
        getListView().setAdapter(adapter);
    }


}
