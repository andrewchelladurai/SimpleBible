/*
 *
 * This file 'ChapterViewAdapter.java' is part of SimpleBible :
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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Andrew Chelladurai - TheUnknownAndrew[at]GMail[dot]com
 * on 30-Jun-2016 @ 1:49 AM
 */
public class AdapterChapterList
        extends RecyclerView.Adapter<AdapterChapterList.ChapterView> {

    private final ActivityChapterList     mActivity;
    private final List<ChapterList.Entry> mValues;

    public AdapterChapterList(ActivityChapterList activity, List<ChapterList.Entry> items) {
        mActivity = activity;
        mValues = items;
    }

    @Override
    public ChapterView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.entry_chapter, parent, false);
        return new ChapterView(view);
    }

    @Override
    public void onBindViewHolder(final ChapterView holder, final int position) {
        final ChapterList.Entry currentItem = mValues.get(position);
        holder.mItem = currentItem;
        holder.mContent.setText(currentItem.getContent());

        holder.mView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mActivity.isDualPane()) {
                    Bundle args = new Bundle();
                    args.putString(FragmentChapterVerses.ARG_ITEM_ID,
                                   currentItem.getContent());
                    FragmentChapterVerses fragment = new FragmentChapterVerses();
                    fragment.setArguments(args);
                    mActivity.getSupportFragmentManager().beginTransaction()
                             .replace(R.id.chapter_container, fragment)
                             .commit();
                } else {
                    Bundle args = new Bundle();
                    args.putString(ActivityChapterVerses.CURRENT_CHAPTER_NUMBER,
                                   holder.getAdapterPosition() + "");
                    args.putParcelable(ActivityChapterVerses.CURRENT_BOOK, mActivity.getBook());
                    args.putString(FragmentChapterVerses.ARG_ITEM_ID, holder.mItem.getContent());

                    Intent intent = new Intent(v.getContext(), ActivityChapterVerses.class);
                    intent.putExtras(args);
                    v.getContext().startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ChapterView
            extends RecyclerView.ViewHolder {

        public final  View              mView;
        private final TextView          mContent;
        public        ChapterList.Entry mItem;

        public ChapterView(View view) {
            super(view);
            mView = view;
            mContent = (TextView) view.findViewById(R.id.chapter_entry_content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContent.getText() + "'";
        }

        public String getContent() {
            return mContent.getText().toString();
        }
    }
}
