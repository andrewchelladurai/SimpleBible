package com.andrewchelladurai.simplebible;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ListViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class V2SearchFragment
        extends Fragment
        implements View.OnClickListener {

    private static final String TAG = "V2SearchFragment";
    private static V2SearchFragment    staticInstance;
    private        InteractionListener mListener;
    private EditText       editText;
    private Button         button;
    private TextView       resultsLabel;
    private ListViewCompat resultsList;

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
        editText = (EditText) view.findViewById(R.id.fragment_v2_search_edittext);
        button = (Button) view.findViewById(R.id.fragment_v2_search_button);
        resultsLabel = (TextView) view.findViewById(R.id.fragment_v2_search_results_label);
        resultsList = (ListViewCompat) view.findViewById(R.id.fragment_v2_search_results_listView);

        button.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        ArrayList<String> results =
                DatabaseUtility.getInstance(getContext())
                               .searchForText(editText.getText().toString());
        Toast.makeText(getContext(),
                       editText.getText() + " : " + results.size() + " Results",
                       Toast.LENGTH_SHORT).show();
    }

    public interface InteractionListener {

        void onSearchFragmentInteraction(View view);
    }
}
