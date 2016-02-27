package com.andrewchelladurai.simplebible;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class V2SearchFragment
        extends Fragment {

    private static final String TAG = "V2SearchFragment";
    private static V2SearchFragment    staticInstance;
    private        InteractionListener mListener;

    public V2SearchFragment() {
        // Required empty public constructor
    }

    public static V2SearchFragment getInstance() {
        if (staticInstance == null) {
            staticInstance = new V2SearchFragment();
            Bundle args = new Bundle();
            staticInstance.setArguments(args);
        }
        return staticInstance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_v2_search, container, false);
        return view;
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

        void onSearchFragmentInteraction(View view);
    }
}
