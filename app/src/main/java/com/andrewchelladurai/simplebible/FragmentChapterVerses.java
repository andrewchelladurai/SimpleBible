package com.andrewchelladurai.simplebible;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ListViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class FragmentChapterVerses
        extends Fragment
        implements AdapterView.OnItemLongClickListener {

    private static final String ARG_VERSES_LIST = "VERSES_LIST";
    private static FragmentChapterVerses staticInstance;
    private static ArrayAdapter<String>  verseListAdapter;
    private ArrayList<String> versesList = new ArrayList<>(0);

    private InteractionListener mListener;

    public FragmentChapterVerses() {
        // Required empty public constructor
    }

    public static FragmentChapterVerses getInstance() {
        if (staticInstance == null) {
            staticInstance = new FragmentChapterVerses();
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
        View view = inflater.inflate(R.layout.fragment_verses, container, false);
        ArrayList<String> verseList = new ArrayList<>(1);
        verseListAdapter = new ArrayAdapter<String>(getContext(),
                                                    android.R.layout.simple_list_item_1,
                                                    verseList);
        ListViewCompat listViewCompat =
                (ListViewCompat) view.findViewById(R.id.fragment_v2_verses_list);
        listViewCompat.setAdapter(verseListAdapter);
        listViewCompat.setOnItemLongClickListener(this);
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

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        String verse = ((TextView) view).getText()
                       + " -- The Holy Bible (New International Version)";
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, verse);
        startActivity(intent);
        return true;
    }

    public interface InteractionListener {

        void handleVersesFragmentInteraction(View view);
    }
}
