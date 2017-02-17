package com.haojie.badmintonscorecounter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
    public static final String ARG_PHOTO_PATH = "photoPath";
    public static final String ARG_PlAYER_NAME = "playerName";

    private String mPhotoPath;
    private String mPlayerName;



    private ImageView mPlayerImageView;

    private OnFragmentInteractionListener mListener;

    public ViewUpdatePhotoDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mPlayerImageView = (ImageView)(getView().findViewById(R.id.player_image_view));

        mPlayerImageView.setImageBitmap(BitmapFactory.decodeFile(mPhotoPath));

        ImageButton cameraButton = (ImageButton)(getView().findViewById(R.id.button_camera));
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                getActivity().startActivityForResult(takePictureIntent, 1001);
                }
            });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        mListener.onDismiss(mPlayerName);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageBitmap = BitmapUtils.resizeAndCropPhoto(imageBitmap);
            Database.writeBitmapToDisk(imageBitmap, mPhotoPath);
            mPlayerImageView.setImageBitmap(imageBitmap);
        }
    }


    public static ViewUpdatePhotoDialogFragment newInstance(String photoPath, String playerName) {
        ViewUpdatePhotoDialogFragment fragment = new ViewUpdatePhotoDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PHOTO_PATH, photoPath);
        args.putString(ARG_PlAYER_NAME, playerName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPhotoPath = getArguments().getString(ARG_PHOTO_PATH);
            mPlayerName = getArguments().getString(ARG_PlAYER_NAME);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_update_photo, container, false);
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
        void onDismiss(String playerName);
    }
}
