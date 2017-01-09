package com.haojie.badmintonscorecounter;

import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewUpdatePhotoDialogFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewUpdatePhotoDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewUpdatePhotoDialogFragment extends DialogFragment {
    public static final String ARG_PLAYER_NAME = "playerName";
    private static final String ARG_PARAM2 = "param2";

    private String mPlayerName;
    private String mParam2;

    private ImageView mPlayerImageView;

    private OnFragmentInteractionListener mListener;

    public ViewUpdatePhotoDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mPlayerImageView = (ImageView)(getView().findViewById(R.id.player_image_view));

        Database database = new Database();
        database.deserialize(getContext());
        Player player = database.getPlayerWithName(mPlayerName);

        mPlayerImageView.setImageBitmap(player.getImage());
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param playerName Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewUpdatePhotoDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewUpdatePhotoDialogFragment newInstance(String playerName, String param2) {
        ViewUpdatePhotoDialogFragment fragment = new ViewUpdatePhotoDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PLAYER_NAME, playerName);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPlayerName = getArguments().getString(ARG_PLAYER_NAME);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_update_photo, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
