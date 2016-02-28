package com.andrewchelladurai.simplebible;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ListViewCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class FragmentSearch
        extends Fragment
        implements View.OnClickListener,
                   TextWatcher, AdapterView.OnItemLongClickListener {

    private static final String TAG = "FragmentSearch";
    private static FragmentSearch       staticInstance;
    private        InteractionListener  mListener;
    private        EditText             editText;
    private        Button               button;
    private        TextView             resultsLabel;
    private        ArrayAdapter<String> listAdapter;

    public FragmentSearch() {
        // Required empty public constructor
    }

    public static FragmentSearch getInstance() {
        if (staticInstance == null) {
            staticInstance = new FragmentSearch();
            Bundle args = new Bundle();
            staticInstance.setArguments(args);
        }
        return staticInstance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        editText = (EditText) view.findViewById(R.id.fragment_v2_search_edittext);
        button = (Button) view.findViewById(R.id.fragment_v2_search_button);
        resultsLabel = (TextView) view.findViewById(R.id.fragment_v2_search_results_label);
        ListViewCompat resultsList = (ListViewCompat) view.findViewById(
                R.id.fragment_v2_search_results_listView);
        listAdapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_list_item_1, new ArrayList<String>(1));
        resultsList.setAdapter(listAdapter);

        button.setOnClickListener(this);
        editText.addTextChangedListener(this);
        resultsList.setOnItemLongClickListener(this);
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
        String textToSearch = editText.getText().toString();

        if (textToSearch.length() < 2) {
            resultsLabel.setText(getString(R.string.fragment_v2_search_results_label_2chars));
            editText.requestFocus();
            return;
        }

        String title = button.getText().toString();

        if (title.equalsIgnoreCase(getString( // is button showing : Click to reset
                                              R.string.fragment_v2_search_button_label_reset))) {
            button.setText(getString(R.string.fragment_v2_search_button_label_default));
            listAdapter.clear();
            editText.setText("");
            resultsLabel.setText("");
        } else if (title.equalsIgnoreCase(getString(
                R.string.fragment_v2_search_button_label_default))) {
            ArrayList<String> results =
                    DatabaseUtility.getInstance(getContext())
                                   .searchForText(editText.getText().toString());
            if (results.size() > 0) {
                listAdapter.clear();
                listAdapter.addAll(results);
                String str = (results.size() + 1) + " "
                             + getString(R.string.fragment_v2_search_button_label_results_found);
                resultsLabel.setText(str);
                button.setText(getString(R.string.fragment_v2_search_button_label_reset));
            } else {
                resultsLabel.setText(
                        getString(R.string.fragment_v2_search_button_label_no_results_found));
                button.setText(getString(R.string.fragment_v2_search_button_label_reset));
                listAdapter.clear();
            }
        }
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        button.setText(getString(R.string.fragment_v2_search_button_label_default));
    }

    @Override
    public void afterTextChanged(Editable editable) {
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

        void onSearchFragmentInteraction(View view);
    }
}
