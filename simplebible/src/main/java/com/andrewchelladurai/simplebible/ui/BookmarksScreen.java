package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.EntityBook;
import com.andrewchelladurai.simplebible.data.EntityBookmark;
import com.andrewchelladurai.simplebible.model.BookmarksViewModel;
import com.andrewchelladurai.simplebible.ui.adapter.BookmarksAdapter;
import com.andrewchelladurai.simplebible.ui.ops.BookmarksScreenOps;
import com.andrewchelladurai.simplebible.ui.ops.SimpleBibleOps;
import com.andrewchelladurai.simplebible.utils.Utils;

public class BookmarksScreen
    extends Fragment
    implements BookmarksScreenOps {

  private static final String TAG = "BookmarksScreen";

  private SimpleBibleOps ops;

  private BookmarksViewModel model;

  private BookmarksAdapter adapter;

  private View rootView;

  @NonNull
  private String verseTemplateSingle = "";

  @NonNull
  private String verseTemplateMultiple = "";

  @NonNull
  private String noteTemplate = "";

  @NonNull
  private String noteEmptyTemplate = "";

  @Override
  public void onAttach(@NonNull final Context context) {
    Log.d(TAG, "onAttach:");
    super.onAttach(context);

    if (context instanceof SimpleBibleOps) {
      //noinspection unused
      ops = (SimpleBibleOps) context;
    } else {
      throw new ClassCastException(TAG + " onAttach: [Context] must implement [SimpleBibleOps]");
    }

    model = ViewModelProvider.AndroidViewModelFactory
                .getInstance(requireActivity().getApplication())
                .create(BookmarksViewModel.class);
    adapter = new BookmarksAdapter(this);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    Log.d(TAG, "onCreateView:");
    ops.showNavigationView();
    rootView = inflater.inflate(R.layout.bookmarks_screen, container, false);

    verseTemplateSingle =
        getResources().getQuantityString(R.plurals.item_bookmark_template_verse, 1);
    verseTemplateMultiple =
        getResources().getQuantityString(R.plurals.item_bookmark_template_verse, 2);
    noteTemplate = getString(R.string.item_bookmark_template_note);
    noteEmptyTemplate = getString(R.string.item_bookmark_template_note_empty);

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

    ((TextView) rootView.findViewById(R.id.scr_bookmarks_help))
        .setText(HtmlCompat.fromHtml(getString(R.string.scr_bookmarks_help),
                                     HtmlCompat.FROM_HTML_MODE_COMPACT));

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
  public void handleActionSelect(@NonNull final EntityBookmark bookmark) {
    Log.d(TAG, "handleActionSelect: bookmark = [" + bookmark + "]");

    final Bundle bundle = new Bundle();
    bundle.putString(BookmarkScreen.ARG_STR_REFERENCE, bookmark.getReference());

    NavHostFragment.findNavController(this)
                   .navigate(R.id.nav_from_scr_bookmark_list_to_scr_bookmark, bundle);
  }

  @Override
  public void getFirstVerseOfBookmark(@NonNull final EntityBookmark bookmark,
                                      @NonNull final TextView verseView,
                                      @NonNull final TextView noteView) {
    model.getFirstVerseOfBookmark(bookmark).observe(getViewLifecycleOwner(), verse -> {
      final String bookmarkReference = bookmark.getReference();
      final String bookmarkNote = bookmark.getNote();
      final int verseCount = Utils.getInstance()
                                  .splitBookmarkReference(bookmarkReference).length;

      if (verse == null) {
        Log.e(TAG, "getFirstVerseOfBookmark: ",
              new IllegalArgumentException("null bookmark found for reference["
                                           + bookmarkReference + "]"));
        return;
      }

      final EntityBook book = Utils.getInstance().getCachedBook(verse.getBook());
      if (book == null) {
        Log.e(TAG, "getFirstVerseOfBookmark: ",
              new IllegalArgumentException("null book for verse reference["
                                           + verse.getReference() + "]"));
        return;
      }

      final int htmlMode = HtmlCompat.FROM_HTML_MODE_COMPACT;
      final Spanned verseContent =
          HtmlCompat.fromHtml(String.format((verseCount == 1)
                                            ? verseTemplateSingle
                                            : verseTemplateMultiple,
                                            verseCount,
                                            book.getName(),
                                            verse.getChapter(),
                                            verse.getVerse(),
                                            verse.getText()), htmlMode);

      final Spanned noteContent =
          (bookmarkNote.isEmpty())
          ? HtmlCompat.fromHtml(noteEmptyTemplate, htmlMode)
          : HtmlCompat.fromHtml(String.format(noteTemplate, bookmarkNote), htmlMode);

      noteView.setText(noteContent);
      verseView.setText(verseContent);

    });
  }

}
