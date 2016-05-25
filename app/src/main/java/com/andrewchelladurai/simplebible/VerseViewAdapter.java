/*
 * This file 'VerseViewAdapter.java' is part of SimpleBible :
 *
 * Copyright (c) 2016.
 *
 * This Application is available at below locations
 * Binary : https://play.google.com/store/apps/developer?id=Andrew+Chelladurai
 * Source : https://github.com/andrewchelladurai/
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * OR <http://www.gnu.org/licenses/gpl-3.0.txt>
 *
 */

package com.andrewchelladurai.simplebible;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andrewchelladurai.simplebible.ChapterContent.VerseEntry;

import java.util.List;

public class VerseViewAdapter
        extends RecyclerView.Adapter<VerseViewAdapter.Verse> {

    private final List<VerseEntry> mVerseList;
    private final ChapterActivity  mListener;

    public VerseViewAdapter(List<VerseEntry> items, ChapterActivity listener) {
        mVerseList = items;
        mListener = listener;
    }

    @Override
    public Verse onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Verse(LayoutInflater.from(parent.getContext())
                                       .inflate(R.layout.fragment_verses, parent, false));
    }

    @Override
    public void onBindViewHolder(final Verse holder, int position) {
        holder.mItem = mVerseList.get(position);

        String vText = mVerseList.get(position).getVerseText().toString();
        String vNum = mVerseList.get(position).getVerseNumber();

        String txt = mListener.getString(R.string.chapter_verse_template);
        txt = txt.replace(mListener.getString(R.string.chapter_verse_template_text), vText);
        txt = txt.replace(mListener.getString(R.string.chapter_verse_template_verse), vNum);

        holder.mContent.setText(Html.fromHtml(txt));

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override public boolean onLongClick(final View v) {
                mListener.handleLongClick(holder.mItem);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mVerseList.size();
    }

    class Verse
            extends RecyclerView.ViewHolder {

        final View              mView;
        final AppCompatTextView mContent;
        VerseEntry mItem;

        public Verse(View view) {
            super(view);
            mView = view;
            mContent = (AppCompatTextView) view.findViewById(R.id.fragment_verse_content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContent.getText() + "'";
        }
    }
}
