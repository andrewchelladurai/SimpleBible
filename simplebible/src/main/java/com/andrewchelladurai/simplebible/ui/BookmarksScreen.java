package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.EntityBookmark;
import com.andrewchelladurai.simplebible.data.EntityVerse;
import com.andrewchelladurai.simplebible.model.BookmarksViewModel;
import com.andrewchelladurai.simplebible.ui.adapter.BookmarksAdapter;
import com.andrewchelladurai.simplebible.ui.ops.BookmarksScreenOps;
import com.andrewchelladurai.simplebible.ui.ops.SimpleBibleOps;

public class BookmarksScreen
    extends Fragment
    implements BookmarksScreenOps {

  private static final String TAG = "BookmarksScreen";

  private SimpleBibleOps ops;

  private BookmarksViewModel model;

  private BookmarksAdapter adapter;

  private View rootView;

  @Override
  public void onAttach(@NonNull final Context context) {
    Log.d(TAG, "onAttach:");
    super.onAttach(context);

    if (context instanceof SimpleBibleOps) {
      ops = (SimpleBibleOps) context;
    } else {
      throw new ClassCastException(TAG + " onAttach: [Context] must implement [SimpleBibleOps]");
    }

    model = ViewModelProvider.AndroidViewModelFactory
                .getInstance(requireActivity().getApplication())
                .create(BookmarksViewModel.class);
    adapter = new BookmarksAdapter(this,
                                   getString(R.string.item_bookmark_template_verse),
                                   getString(R.string.item_bookmark_template_note),
                                   getString(R.string.item_bookmark_template_note_empty));
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    Log.d(TAG, "onCreateView:");
    rootView = inflater.inflate(R.layout.bookmarks_screen, container, false);

    ((RecyclerView) rootView.findViewById(R.id.scr_bookmarks_list)).setAdapter(adapter);

    updateContent();

    return rootView;
  }

  private void updateContent() {
    Log.d(TAG, "updateContent:");
    model.getBookmarks().observe(getViewLifecycleOwner(), bookmarks -> {
      if (bookmarks == null || bookmarks.isEmpty()) {
        showHelpInfo();
        return;
      }
      model.cacheBookmarks(bookmarks);
      showBookmarks();
    });
  }

  private void showHelpInfo() {
    rootView.findViewById(R.id.scr_bookmarks_contain_help).setVisibility(View.VISIBLE);
    rootView.findViewById(R.id.scr_bookmarks_list).setVisibility(View.GONE);
  }

  private void showBookmarks() {
    rootView.findViewById(R.id.scr_bookmarks_list).setVisibility(View.VISIBLE);
    rootView.findViewById(R.id.scr_bookmarks_contain_help).setVisibility(View.GONE);
    adapter.notifyDataSetChanged();
  }

  @Override
  public int getBookmarkListSize() {
    return model.getBookmarkListSize();
  }

  @NonNull
  @Override
  public EntityBookmark getBookmarkAtPosition(final int position) {
    return model.getBookmarkAtPosition(position);
  }

  @Override
  public void handleActionEdit(@NonNull final EntityBookmark bookmark) {
    Log.d(TAG, "handleActionEdit: bookmark [" + bookmark + "]");
  }

  @Override
  public void handleActionDelete(@NonNull final EntityBookmark bookmark) {
    Log.d(TAG, "handleActionDelete: bookmark [" + bookmark + "]");
  }

  @Override
  public void handleActionShare(@NonNull final EntityBookmark bookmark) {
    Log.d(TAG, "handleActionShare: bookmark [" + bookmark + "]");
  }

  @Override
  public void handleActionSelect(@NonNull final EntityBookmark bookmark) {
    Log.d(TAG, "handleActionSelect: bookmark = [" + bookmark + "]");
  }

  @NonNull
  @Override
  public EntityVerse getFirstVerseOfBookmark(@NonNull final EntityBookmark bookmark) {
    return model.getFirstVerseOfBookmark(bookmark);
  }

}
