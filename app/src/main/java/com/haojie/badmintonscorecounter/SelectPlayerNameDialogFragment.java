package com.haojie.badmintonscorecounter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by Haojie on 12/30/2016.
 */

public class SelectPlayerNameDialogFragment extends DialogFragment {

    public interface SelectPlayerNameClickHandler
    {
        void onNameSelected(SelectPlayerNameDialogFragment dialogFragment, String playerName);
    }

    private static final String PLAYER_NAME_KEY = "player_name";
    private ListView mListView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View v = getActivity().getLayoutInflater().inflate(R.layout.activity_select_player_from_list, null);
        mListView = (ListView)v.findViewById(R.id.list_view);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                Player value = (Player)adapter.getItemAtPosition(position);

                SelectPlayerNameClickHandler handler = (SelectPlayerNameClickHandler)getActivity();
                handler.onNameSelected(SelectPlayerNameDialogFragment.this, value.getName());
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
        Database database = new Database();
        database.deserialize(v.getContext());

        SelectPlayerArrayAdapter adapter = new SelectPlayerArrayAdapter(v.getContext(), database.getPlayersWithoutDefault());

        mListView.setAdapter(adapter);
    }


}
