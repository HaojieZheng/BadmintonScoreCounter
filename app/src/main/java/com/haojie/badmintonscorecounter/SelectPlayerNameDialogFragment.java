package com.haojie.badmintonscorecounter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Haojie on 12/30/2016.
 */

public class SelectPlayerNameDialogFragment extends DialogFragment {

    public interface SelectPlayerNameClickHandler
    {
        void onNameSelected(SelectPlayerNameDialogFragment dialogFragment, String playerName);
    }

    private static final String PLAYER_NAME_KEY = "player_name";
    ListView mListView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View v = getActivity().getLayoutInflater().inflate(R.layout.activity_select_player_from_list, null);
        mListView = (ListView)v.findViewById(R.id.list_view);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                HashMap<String, String> value = (HashMap<String, String>)adapter.getItemAtPosition(position);
                String playerName = value.get(PLAYER_NAME_KEY);
                SelectPlayerNameClickHandler handler = (SelectPlayerNameClickHandler)getActivity();
                handler.onNameSelected(SelectPlayerNameDialogFragment.this, playerName);
                dismiss();
            }
        });

        populateList(v);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(getString(R.string.SelectPlayerTitle)).setView(v);

        return builder.create();
    }


    private void populateList(View v)
    {
        List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
        Database database = new Database();
        database.Deserialize(v.getContext());

        for (Player p : database.getPlayers())
        {
            HashMap<String, String> row = new HashMap<String, String>();
            row.put("player_name", p.getName());
            fillMaps.add(row);
        }

        String[] from = new String[]{"player_name"};
        int[] to = new int[]{R.id.player_name};

        SimpleAdapter adapter = new SimpleAdapter(v.getContext(), fillMaps, R.layout.player_name_list_item, from, to);
        mListView.setAdapter(adapter);
    }


}
