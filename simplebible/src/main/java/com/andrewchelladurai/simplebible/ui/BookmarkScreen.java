package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.entities.Verse;
import com.andrewchelladurai.simplebible.model.BookmarkScreenModel;
import com.andrewchelladurai.simplebible.ui.adapter.BookmarkedVerseListAdapter;
import com.andrewchelladurai.simplebible.ui.ops.BookmarkScreenOps;
import com.andrewchelladurai.simplebible.ui.ops.SimpleBibleScreenOps;
import com.andrewchelladurai.simplebible.utils.BookmarkUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;
import java.util.List;

public class BookmarkScreen
    extends Fragment
    implements BookmarkScreenOps {

  public static final String ARG_ARRAY_VERSES = "ARG_ARRAY_VERSES";
  private static final String TAG = "BookmarkScreen";

  private static String contentTemplate;

  private BookmarkedVerseListAdapter verseListAdapter;
  private BookmarkScreenModel model;
  private SimpleBibleScreenOps activityOps;

  private TextInputEditText noteField;
  private MaterialButton editButton;
  private MaterialButton saveButton;
  private MaterialButton deleteButton;
  private MaterialButton shareButton;

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    if (!(context instanceof SimpleBibleScreenOps)) {
      throw new RuntimeException(
          context.toString() + " must implement SimpleBibleScreenOps");
    }
    activityOps = (SimpleBibleScreenOps) context;
    verseListAdapter = new BookmarkedVerseListAdapter(this);
    model = ViewModelProviders.of(this).get(BookmarkScreenModel.class);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedState) {
    final View view = inflater.inflate(R.layout.bookmark_screen, container, false);

    editButton = view.findViewById(R.id.bookmark_scr_action_edit);
    editButton.setOnClickListener(v -> handleClickActionEdit());

    saveButton = view.findViewById(R.id.bookmark_scr_action_save);
    saveButton.setOnClickListener(v -> handleClickActionSave());

    deleteButton = view.findViewById(R.id.bookmark_scr_action_delete);
    deleteButton.setOnClickListener(v -> handleClickActionDelete());

    shareButton = view.findViewById(R.id.bookmark_scr_action_share);
    shareButton.setOnClickListener(v -> handleClickActionShare());

    noteField = view.findViewById(R.id.bookmark_scr_note);

    RecyclerView verseList = view.findViewById(R.id.bookmark_scr_list);
    verseList.setAdapter(verseListAdapter);

    activityOps.hideNavigationComponent();

    final Bundle args = getArguments();
    if (args == null) {
      final String message = getString(R.string.bookmark_scr_err_no_args_passed);
      activityOps.showErrorScreen(message, true);
    } else if (!args.containsKey(ARG_ARRAY_VERSES)) {
      final String message = getString(R.string.bookmark_scr_err_no_verses_passed);
      activityOps.showErrorScreen(message, true);
    } else {
      final Parcelable[] array = args.getParcelableArray(ARG_ARRAY_VERSES);
      if (array == null || array.length == 0) {
        final String message = getString(R.string.bookmark_scr_err_empty_verse_array_passed);
        activityOps.showErrorScreen(message, true);
      } else {
        // now we have the array of verses passed to this screen
        updateContent(Arrays.asList(array));
      }
    }

    return view;
  }

  @Override
  public void onDetach() {
    super.onDetach();
    activityOps.showNavigationComponent();
    activityOps = null;
    verseListAdapter = null;
  }

  private void updateContent(final List<Parcelable> list) {
    Log.d(TAG, "updateContent:");
    if (contentTemplate == null) {
      contentTemplate = getString(R.string.item_bookmark_verse_content_template);
    }

    final String reference = BookmarkUtils.createBookmarkReference(list);
    model.bookmarkExists(reference).observe(this, rowCount -> {
      if (rowCount != null && rowCount > 0) {
        Log.d(TAG, "onCreateView: reference found, getting record");
        model.getBookmark(reference)
             .observe(this, bookmark -> noteField.setText(bookmark.getNote()));
        toggleActionButtons(true);
      } else {
        toggleActionButtons(false);
      }
      verseListAdapter.refreshList(list);
    });
  }

  private void toggleActionButtons(final boolean bookmarkExists) {
    Log.d(TAG, "toggleActionButtons(): bookmarkExists = [" + bookmarkExists + "]");

    noteField.setEnabled(!bookmarkExists);

    saveButton.setVisibility((bookmarkExists) ? View.GONE : View.VISIBLE);

    editButton.setVisibility((bookmarkExists) ? View.VISIBLE : View.GONE);
    deleteButton.setVisibility((bookmarkExists) ? View.VISIBLE : View.GONE);
    shareButton.setVisibility((bookmarkExists) ? View.VISIBLE : View.GONE);
  }

  private String getNote() {
    final Editable editable = noteField.getText();
    if (editable == null) {
      Log.e(TAG, "getNote: null Editable found, returning blank");
      return "";
    }

    return editable.toString().trim();
  }

  private void handleClickActionDelete() {
    // TODO: 26/5/19 implement this
  }

  private void handleClickActionSave() {
    model.saveBookmark(model.getCachedReference(), getNote()).observe(this, rowCount -> {
      final boolean bookmarkExists = rowCount != null && rowCount > 0;
      if (bookmarkExists) {
        activityOps.showErrorMessage(getString(R.string.bookmark_scr_new_record_created));
      }
    });
  }

  private void handleClickActionEdit() {
    // TODO: 1/6/19 implement this
  }

  private void handleClickActionShare() {
    // TODO: 26/5/19 implement this
  }

  @Override
  public void refreshList(@NonNull final List<?> newList) {
    if (newList.isEmpty()) {
      Log.d(TAG, "refreshList: empty list passed, returning");
      return;
    }

    model.updateCache(newList);
  }

  @NonNull
  @Override
  public List<?> getCachedList() {
    return model.getCachedList();
  }

  @NonNull
  @Override
  public Object getCachedItemAt(final int position) {
    return model.getCachedItemAt(position);
  }

  @Override
  public int getCachedListSize() {
    return model.getCachedListSize();
  }

  @Override
  public void showContent(@NonNull final TextView textView, @NonNull final Verse verse) {
    model.getBook(verse.getBookNumber()).observe(this, book -> {
      if (book == null) {
        Log.e(TAG, "showContent: no book found for this verse [" + verse + "]");
        return;
      }
      textView.setText(HtmlCompat.fromHtml(String.format(contentTemplate,
                                                         book.getName(),
                                                         verse.getChapterNumber(),
                                                         verse.getVerseNumber(),
                                                         verse.getText()),
                                           HtmlCompat.FROM_HTML_MODE_LEGACY));
    });
  }

}
