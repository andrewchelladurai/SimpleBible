package com.andrewchelladurai.simplebible;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InteractionListener} interface
 * to handle interaction events.
 * Use the {@link V2HomeFragment#getInstance} factory method to
 * create an instance of this fragment.
 */
public class V2HomeFragment
        extends Fragment {

    private static final String TAG                  = "V2HomeFragment";
    private static final String ARG_VERSE_OF_THE_DAY = "ARG_VERSE_OF_THE_DAY";
    private static V2HomeFragment      staticInstance;
    private        String              verseOfTheDay;
    private        InteractionListener mListener;

    public V2HomeFragment() {
        // Required empty public constructor
    }

    public static V2HomeFragment getInstance(String verseID) {
        if (staticInstance == null) {
            staticInstance = new V2HomeFragment();
            Bundle args = new Bundle();
            args.putString(ARG_VERSE_OF_THE_DAY, verseID);
            staticInstance.setArguments(args);
        }
        return staticInstance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            verseOfTheDay = getArguments().getString(ARG_VERSE_OF_THE_DAY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_v2_home, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InteractionListener) {
            mListener = (InteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                                       + " must implement InteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface InteractionListener {

        void onHomeFragmentInteraction(View view);
    }
}
