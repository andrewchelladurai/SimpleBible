package com.andrewchelladurai.simplebible.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.entities.Bookmark;
import com.andrewchelladurai.simplebible.ui.ops.BookmarkListScreenOps;
import com.andrewchelladurai.simplebible.ui.ops.SbRvAdapterOps;
import com.andrewchelladurai.simplebible.ui.ops.SbRvHolderOps;
import com.andrewchelladurai.simplebible.utils.BookmarkUtils;
import java.util.ArrayList;
import java.util.List;

public class BookmarkListScreenAdapter
    extends RecyclerView.Adapter
    implements SbRvAdapterOps {

  private static final String TAG = "BookmarkListScreenAdapt";
  private final List<Bookmark> list = new ArrayList<>();

  private BookmarkListScreenOps ops;

  public BookmarkListScreenAdapter(final BookmarkListScreenOps ops) {
    this.ops = ops;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent,
                                                    final int viewType) {
    return new BookmarkViewHolder(LayoutInflater.from(parent.getContext())
                                                .inflate(R.layout.item_bookmark, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
    ((BookmarkViewHolder) holder).updateContent(list.get(position));
  }

  @Override
  public int getItemCount() {
    return list.size();
  }

  @Override
  public void refreshList(@NonNull final List<?> newList) {
    list.clear();
    for (final Object object : newList) {
      list.add((Bookmark) object);
    }
    Log.d(TAG, "refreshList: updated [" + getItemCount() + "] bookmark records");
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public List<?> getList() {
    return list;
  }

  private class BookmarkViewHolder
      extends RecyclerView.ViewHolder
      implements SbRvHolderOps {

    private View rootView;

    BookmarkViewHolder(final View rootView) {
      super(rootView);
      this.rootView = rootView;
    }

    @Override
    public void updateContent(final Object object) {
      final Bookmark bookmark = (Bookmark) object;

      final int count = bookmark.getReferences().split(BookmarkUtils.SEPARATOR).length;
      final TextView contentTv = rootView.findViewById(R.id.item_bookmark_content);
      final String formattedText = String.format(String.valueOf(contentTv.getText()), count);
      contentTv.setText(HtmlCompat.fromHtml(formattedText, HtmlCompat.FROM_HTML_MODE_COMPACT));

      final String bookmarkNote = (bookmark.getNote().isEmpty())
                                  ? ops.getStringValue(R.string.item_bookmark_msg_empty_note)
                                  : bookmark.getNote();
      final TextView noteTv = rootView.findViewById(R.id.item_bookmark_note);
      noteTv.setText(bookmarkNote);

      rootView.findViewById(R.id.item_bookmark_action_view)
              .setOnClickListener(v -> ops.handleBookmarkActionEdit(bookmark));
      rootView.findViewById(R.id.item_bookmark_action_delete)
              .setOnClickListener(v -> ops.handleBookmarkActionDelete(bookmark));
      rootView.findViewById(R.id.item_bookmark_action_share)
              .setOnClickListener(v -> ops.handleBookmarkActionShare(bookmark));
    }

  }

}
