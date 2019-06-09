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
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.entities.Bookmark;
import com.andrewchelladurai.simplebible.data.entities.Verse;
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

    updateContent();

    return rootView;
  }

  @Override
  public void onDetach() {
    super.onDetach();
    activityOps = null;
  }

  private void updateContent() {
    final TextView textView = rootView.findViewById(R.id.bookmark_list_scr_help_text);
    final ImageView imageView = rootView.findViewById(R.id.bookmark_list_scr_help_image);
    final RecyclerView recyclerView = rootView.findViewById(R.id.bookmark_list_scr_list);
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
  }

  @NonNull
  @Override
  public String getStringValue(final int stringResId) {
    return getString(stringResId);
  }

  @Override
  public void handleBookmarkActionEdit(@NonNull final Bookmark bookmark) {
    // get the selected verses into an array so we can pass it
    model.getVerses(bookmark.getReferences()).observe(this, verses -> {
      if (verses == null || verses.isEmpty()) {
        Log.e(TAG, "handleBookmarkActionEdit: no verses found");
        throw new IllegalArgumentException(TAG + " handleBookmarkActionEdit: no verses found");
      }
      final Verse[] verseArray = new Verse[verses.size()];
      verses.toArray(verseArray);

      // now pass the array to the BookmarkScreen
      Bundle bundle = new Bundle();
      bundle.putParcelableArray(BookmarkScreen.ARG_ARRAY_VERSES, verseArray);
      NavHostFragment.findNavController(this)
                     .navigate(R.id.action_bookmarkListScreen_to_bookmarkScreen, bundle);

    });
  }

  @Override
  public void handleBookmarkActionDelete(@NonNull final Bookmark bookmark) {
    Log.d(TAG, "handleBookmarkActionDelete: bookmark [" + bookmark + "]");
    // TODO: 9/6/19 implement this
  }

  @Override
  public void handleBookmarkActionShare(@NonNull final Bookmark bookmark) {
    Log.d(TAG, "handleBookmarkActionShare: bookmark [" + bookmark + "]");
    // TODO: 9/6/19 implement this
  }

}
