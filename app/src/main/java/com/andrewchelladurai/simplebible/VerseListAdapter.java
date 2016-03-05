package com.andrewchelladurai.simplebible;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com
 * on 05-Mar-2016 @ 2:34 AM
 */
public class VerseListAdapter
        extends ArrayAdapter<String> {

    public VerseListAdapter(Context context, int resource, ArrayList<String> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setTypeface(Utilities.getInstance().getPreferredStyle(getContext()));
        textView.setTextSize(Utilities.getInstance().getPreferredSize(getContext()));
        return view;
    }
}
