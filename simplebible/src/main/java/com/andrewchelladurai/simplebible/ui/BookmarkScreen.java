package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;
import java.util.List;

import static androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY;

public class BookmarkScreen
    extends Fragment
    implements BookmarkScreenOps {

  public static final String ARG_ARRAY_VERSES = "ARG_ARRAY_VERSES";
  private static final String TAG = "BookmarkScreen";
  private static String contentTemplate;

  private BookmarkScreenListener screenListener;
  private BookmarkedVerseListAdapter verseListAdapter;
  private BookmarkScreenModel model;
  private TextInputEditText noteField;
  private SimpleBibleScreenOps activityOps;

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    if (!(context instanceof BookmarkScreenListener)) {
      throw new RuntimeException(
          context.toString() + " must implement FragmentInteractionListener");
    }
    if (!(context instanceof SimpleBibleScreenOps)) {
      throw new RuntimeException(
          context.toString() + " must implement SimpleBibleScreenOps");
    }
    activityOps = (SimpleBibleScreenOps) context;
    screenListener = (BookmarkScreenListener) context;
    verseListAdapter = new BookmarkedVerseListAdapter(this);
    model = ViewModelProviders.of(this).get(BookmarkScreenModel.class);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedState) {
    final View view = inflater.inflate(R.layout.bookmark_screen, container, false);

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
        contentTemplate = getString(R.string.item_bookmark_verse_content_template);
        verseListAdapter.refreshList(Arrays.asList(array));
      }
    }

    view.findViewById(R.id.bookmark_scr_action_edit)
        .setOnClickListener(v -> handleClickActionEdit());
    view.findViewById(R.id.bookmark_scr_action_save)
        .setOnClickListener(v -> handleClickActionSave());
    view.findViewById(R.id.bookmark_scr_action_delete)
        .setOnClickListener(v -> handleClickActionDelete());
    view.findViewById(R.id.bookmark_scr_action_share)
        .setOnClickListener(v -> handleClickActionShare());

    noteField = view.findViewById(R.id.bookmark_scr_note);

    RecyclerView verseList = view.findViewById(R.id.bookmark_scr_list);
    verseList.setAdapter(verseListAdapter);

    return view;
  }

  @Override
  public void onDetach() {
    super.onDetach();
    activityOps = null;
    screenListener = null;
    verseListAdapter = null;
  }

  private void handleClickActionDelete() {
    // TODO: 26/5/19 implement this
  }

  private void handleClickActionSave() {
    // TODO: 26/5/19 implement this
  }

  private void handleClickActionEdit() {
    // TODO: 26/5/19 implement this
  }

  private void handleClickActionShare() {
    // TODO: 26/5/19 implement this
  }

  @Override
  public void refreshList(@NonNull final List<?> newList) {
    model.updateCacheList(newList);
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
                                                         verse.getText()), FROM_HTML_MODE_LEGACY));
    });
  }

  interface BookmarkScreenListener {

  }

}
