package com.andrewchelladurai.simplebible;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andrewchelladurai.simplebible.AllBooks.Book;

import java.util.List;

public class AdapterBooksList
        extends RecyclerView.Adapter<AdapterBooksList.ViewHolder> {

    private final List<Book>                            mValues;
    private final FragmentBooksList.InteractionListener mListener;

    public AdapterBooksList(List<Book> items, FragmentBooksList.InteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.fragment_book_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
//        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).toString());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onBooksListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder
            extends RecyclerView.ViewHolder {

        public final View     mView;
        public final TextView mContentView;
        public       Book     mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.fragment_book_list_item_content);
            //TODO : Find a way to make this take effect without an activity recreate
//            mContentView.setTypeface(Utilities.getInstance().getPreferredStyle(view.getContext
// ()));
//            mContentView.setTextSize(Utilities.getInstance().getPreferredSize(view.getContext()));
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
