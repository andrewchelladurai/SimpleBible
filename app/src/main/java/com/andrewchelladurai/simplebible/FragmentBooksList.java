package com.andrewchelladurai.simplebible;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentBooksList
        extends Fragment {

    public static final  String ARG_OLD_TESTAMENT_LIST = "OLD_TESTAMENT_LIST";
    public static final  String ARG_NEW_TESTAMENT_LIST = "NEW_TESTAMENT_LIST";
    private static final String TAG = "FragmentBooksList";
    private static final String ARG_COLUMN_COUNT       = "COLUMN_COUNT";
    private static FragmentBooksList staticInstanceOT;
    private static FragmentBooksList staticInstanceNT;
    private int    mColumnCount = 1;
    private String booksList    = ARG_OLD_TESTAMENT_LIST;
    private InteractionListener mListener;

    public FragmentBooksList() {
    }

    public static FragmentBooksList getInstance(String booksListType, int columnCount) {
        if (booksListType == null) {
            booksListType = ARG_OLD_TESTAMENT_LIST;
        }
        Bundle args = new Bundle();
        if (columnCount <= 0) {
            columnCount = 1;
        }

        args.putInt(ARG_COLUMN_COUNT, columnCount);

        if (booksListType.equalsIgnoreCase(ARG_NEW_TESTAMENT_LIST)) {
            if (staticInstanceNT == null) {
                staticInstanceNT = new FragmentBooksList();
                staticInstanceNT.setArguments(args);
                staticInstanceNT.mColumnCount = columnCount;
                staticInstanceNT.booksList = ARG_NEW_TESTAMENT_LIST;
            }
            return staticInstanceNT;
        } else {
            if (staticInstanceOT == null) {
                staticInstanceOT = new FragmentBooksList();
                staticInstanceOT.setArguments(args);
                staticInstanceOT.mColumnCount = columnCount;
                staticInstanceOT.booksList = ARG_OLD_TESTAMENT_LIST;
            }
            return staticInstanceOT;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_list_layout, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            Log.d(TAG, "onCreateView: mColumnCount = " + mColumnCount);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            if (this.booksList.equalsIgnoreCase(ARG_NEW_TESTAMENT_LIST)) {
                recyclerView.setAdapter(
                        new AdapterBooksList(AllBooks.getNTBooksList(), mListener));
            } else {
                recyclerView.setAdapter(
                        new AdapterBooksList(AllBooks.getOTBooksList(), mListener));
            }
        }
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

        void onBooksListFragmentInteraction(AllBooks.Book item);
    }
}
