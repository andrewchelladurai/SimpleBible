package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.entities.Bookmark;
import com.andrewchelladurai.simplebible.model.BookmarkListScreenModel;
import com.andrewchelladurai.simplebible.ui.adapter.BookmarkListScreenAdapter;
import com.andrewchelladurai.simplebible.ui.ops.BookmarkListScreenOps;
import com.andrewchelladurai.simplebible.ui.ops.SimpleBibleScreenOps;

public class BookmarkListScreen
    extends Fragment
    implements BookmarkListScreenOps {

  private static final String TAG = "BookmarkListScreen";

  private View rootView;
  private SimpleBibleScreenOps activityOps;
  private BookmarkListScreenModel model;
  private BookmarkListScreenAdapter adapter;

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    if (!(context instanceof SimpleBibleScreenOps)) {
      throw new RuntimeException(context.toString() + " must implement SimpleBibleScreenOps");
    }

    model = ViewModelProviders.of(this).get(BookmarkListScreenModel.class);
    adapter = new BookmarkListScreenAdapter(this);
    activityOps = (SimpleBibleScreenOps) context;
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedState) {
    rootView = inflater.inflate(R.layout.bookmarklist_screen, container, false);

    final TextView textView = rootView.findViewById(R.id.bookmark_list_scr_help_text);
    final ImageView imageView = rootView.findViewById(R.id.bookmark_list_scr_help_image);

    RecyclerView recyclerView = rootView.findViewById(R.id.bookmark_list_scr_list);
    recyclerView.setAdapter(adapter);

    model.getAllRecords().observe(this, bookmarks -> {
      if (bookmarks == null || bookmarks.isEmpty()) {
        textView.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        textView.setText(HtmlCompat.fromHtml(
            getString(R.string.bookmark_list_scr_help_text), HtmlCompat.FROM_HTML_MODE_LEGACY));
        return;
      }

      textView.setVisibility(View.GONE);
      imageView.setVisibility(View.GONE);
      recyclerView.setVisibility(View.VISIBLE);
      adapter.refreshList(bookmarks);

    });

    return rootView;
  }

  @Override
  public void onDetach() {
    super.onDetach();
    activityOps = null;
  }

  @NonNull
  @Override
  public String getStringValue(final int stringResId) {
    return getString(stringResId);
  }

  @Override
  public void handleBookmarkActionEdit(@NonNull final Bookmark bookmark) {
    Log.d(TAG, "handleBookmarkActionEdit: bookmark [" + bookmark + "]");
  }

  @Override
  public void handleBookmarkActionDelete(@NonNull final Bookmark bookmark) {
    Log.d(TAG, "handleBookmarkActionDelete: bookmark [" + bookmark + "]");
  }

  @Override
  public void handleBookmarkActionShare(@NonNull final Bookmark bookmark) {
    Log.d(TAG, "handleBookmarkActionShare: bookmark [" + bookmark + "]");
  }

}
