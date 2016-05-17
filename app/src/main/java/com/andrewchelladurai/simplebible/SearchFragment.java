package com.andrewchelladurai.simplebible;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andrewchelladurai.simplebible.SearchResult.Verse;

public class SearchFragment
        extends Fragment {

    private static final String TAG = "SearchFragment";

    public SearchFragment() {
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_list, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fragment_search_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new SearchViewAdapter(SearchResult.getITEMS(), this));

        return view;
    }

    public void searchResultLongClicked(final Verse pItem) {
        Log.i(TAG, "searchResultLongClicked: " + pItem.toString());
    }
}
