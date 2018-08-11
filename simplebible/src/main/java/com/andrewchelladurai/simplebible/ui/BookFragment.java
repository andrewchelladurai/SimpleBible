package com.andrewchelladurai.simplebible.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.ui.dummy.DummyContent;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class BookFragment
    extends Fragment {

    private static final String TAG = "BookFragment";

    private BookFragment() {
    }

    public static BookFragment newInstance(int columnCount) {
        BookFragment fragment = new BookFragment();
        Bundle args = new Bundle();
//        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

/*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }
*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setAdapter(new BookRecyclerViewAdapter(DummyContent.ITEMS, this));
        }
        return view;
    }

    public void onListFragmentInteraction(final DummyContent.DummyItem item) {
        Log.d(TAG, "onListFragmentInteraction() called with: item = [" + item + "]");
    }
}
