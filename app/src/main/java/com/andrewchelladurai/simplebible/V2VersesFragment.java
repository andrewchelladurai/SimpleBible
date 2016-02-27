package com.andrewchelladurai.simplebible;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ListViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class V2VersesFragment
        extends Fragment {

    private static final String ARG_VERSES_LIST = "VERSES_LIST";
    private static V2VersesFragment     staticInstance;
    private static ArrayAdapter<String> verseListAdapter;
    private ArrayList<String> versesList = new ArrayList<>(0);
    private ListViewCompat listViewCompat;

    private InteractionListener mListener;

    public V2VersesFragment() {
        // Required empty public constructor
    }

    public static V2VersesFragment getInstance() {
        if (staticInstance == null) {
            staticInstance = new V2VersesFragment();
            Bundle args = new Bundle();
            args.putStringArrayList(ARG_VERSES_LIST, new ArrayList<String>(0));
            staticInstance.setArguments(args);
        }
        return staticInstance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            versesList = getArguments().getStringArrayList(ARG_VERSES_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_v2_verses, container, false);
        ArrayList<String> verseList = new ArrayList<>(1);
        verseListAdapter = new ArrayAdapter<String>(getContext(),
                                                    android.R.layout.simple_list_item_1,
                                                    verseList);
        listViewCompat = (ListViewCompat) view.findViewById(R.id.fragment_v2_verses_list);
        listViewCompat.setAdapter(verseListAdapter);
        return view;
    }

    public void onButtonPressed(View view) {
        if (mListener != null) {
            mListener.handleVersesFragmentInteraction(view);
        }
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

    public void refreshVersesList(int bookNumber, int chapterNumber) {
        versesList = DatabaseUtility.getInstance(getContext())
                                    .getAllVerseOfChapter(bookNumber, chapterNumber);
        verseListAdapter.clear();
        verseListAdapter.addAll(versesList);
        verseListAdapter.notifyDataSetChanged();
    }

    public interface InteractionListener {

        void handleVersesFragmentInteraction(View view);
    }
}
