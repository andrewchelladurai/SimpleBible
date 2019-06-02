package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
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
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.entities.Bookmark;
import com.andrewchelladurai.simplebible.data.entities.Verse;
import com.andrewchelladurai.simplebible.model.BookmarkScreenModel;
import com.andrewchelladurai.simplebible.ui.adapter.BookmarkedVerseListAdapter;
import com.andrewchelladurai.simplebible.ui.ops.BookmarkScreenOps;
import com.andrewchelladurai.simplebible.ui.ops.SimpleBibleScreenOps;
import com.andrewchelladurai.simplebible.utils.BookmarkUtils.CreateBookmarkLoader;
import com.andrewchelladurai.simplebible.utils.BookmarkUtils.DeleteBookmarkLoader;
import com.andrewchelladurai.simplebible.utils.BookmarkUtils.UpdateBookmarkLoader;
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
  private StringBuilder verseListText;

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

    ((RecyclerView) view.findViewById(R.id.bookmark_scr_list))
        .setAdapter(verseListAdapter);

    if (contentTemplate == null) {
      contentTemplate = getString(R.string.item_bookmark_verse_content_template);
    }

    activityOps.hideNavigationComponent();

    if (savedState == null) {
      final Bundle args = getArguments();
      if (args == null) {
        activityOps.showErrorScreen(getString(R.string.bookmark_scr_err_no_args_passed), true);
      } else if (!args.containsKey(ARG_ARRAY_VERSES)) {
        activityOps.showErrorScreen(getString(R.string.bookmark_scr_err_no_verses_passed), true);
      } else {
        final Parcelable[] array = args.getParcelableArray(ARG_ARRAY_VERSES);
        if (array == null || array.length == 0) {
          activityOps.showErrorScreen(getString(R.string.bookmark_scr_err_no_verses_passed), true);
        } else {
          final List<Parcelable> argVerseList = Arrays.asList(array);
          final String reference = model.createBookmarkReference(argVerseList);
          model.doesBookmarkExist(reference)
               .observe(this, rowCount -> {
                 final boolean exists = rowCount != null && rowCount > 0;
                 Log.d(TAG, "onCreateView: reference[" + reference + "], exists[" + exists + "]");

                 if (exists) {
                   model.getBookmark(reference).observe(this, bookmark -> {
                     if (bookmark != null) {
                       noteField.setText(bookmark.getNote());
                     } else {
                       Log.e(TAG, "toggleActionButtons: bookmark with reference["
                                  + model.getCachedReference() + "] not found");
                     }
                   });
                 }

                 toggleActionButtons(exists);
                 verseListAdapter.refreshList(argVerseList);
               });
        }
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

  private void toggleActionButtons(final boolean bookmarkExists) {
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
    LoaderManager.getInstance(this)
                 .initLoader(DeleteBookmarkLoader.ID, null, new DeleteBookmarkListener())
                 .forceLoad();
  }

  private void handleClickActionSave() {
    final String buttonText = saveButton.getText().toString();
    if (buttonText.equalsIgnoreCase(getString(R.string.bookmark_scr_action_save))) {
      LoaderManager.getInstance(this)
                   .initLoader(CreateBookmarkLoader.ID, null, new CreateBookmarkListener())
                   .forceLoad();
    } else if (buttonText.equalsIgnoreCase(getString(R.string.bookmark_scr_action_update))) {
      LoaderManager.getInstance(this)
                   .initLoader(UpdateBookmarkLoader.ID, null, new UpdateBookmarkListener())
                   .forceLoad();
    }
  }

  private void handleClickActionEdit() {
    saveButton.setText(getString(R.string.bookmark_scr_action_update));
    toggleActionButtons(false);
  }

  private void handleClickActionShare() {
    activityOps.shareText(String.format(
        getString(R.string.bookmark_scr_action_share_template), verseListText, getNote()));
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
      final Spanned htmlText = HtmlCompat.fromHtml(String.format(contentTemplate,
                                                                 book.getName(),
                                                                 verse.getChapterNumber(),
                                                                 verse.getVerseNumber(),
                                                                 verse.getText()),
                                                   HtmlCompat.FROM_HTML_MODE_LEGACY);
      textView.setText(htmlText);

      if (verseListText == null) {
        verseListText = new StringBuilder();
      }

      verseListText.append(htmlText)
                   .append("\n");
    });
  }

  private class CreateBookmarkListener
      implements LoaderManager.LoaderCallbacks<Boolean> {

    private static final String TAG = "CreateBookmarkLoaderLis";

    @NonNull
    @Override
    public Loader<Boolean> onCreateLoader(final int id, @Nullable final Bundle args) {
      Log.d(TAG, "onCreateLoader:");
      return new CreateBookmarkLoader(requireContext(),
                                      new Bookmark(model.getCachedReference(), getNote()));
    }

    @Override
    public void onLoadFinished(@NonNull final Loader<Boolean> loader, final Boolean data) {
      Log.d(TAG, "onLoadFinished:");
      final String reference = model.getCachedReference();
      model.doesBookmarkExist(reference)
           .observe(BookmarkScreen.this, rowCount -> {
             final boolean exists = rowCount != null && rowCount > 0;
             if (exists) {
               model.getBookmark(model.getCachedReference())
                    .observe(BookmarkScreen.this, bookmark -> {
                      if (bookmark != null) {
                        noteField.setText(bookmark.getNote());
                      } else {
                        Log.e(TAG, "toggleActionButtons: bookmark with reference["
                                   + reference + "] not found");
                      }
                    });
             }

             toggleActionButtons(exists);
             activityOps.showErrorMessage(
                 exists ? getString(R.string.bookmark_scr_action_save_success) :
                 getString(R.string.bookmark_scr_action_save_failure));
           });
    }

    @Override
    public void onLoaderReset(@NonNull final Loader<Boolean> loader) {
      Log.d(TAG, "onLoaderReset: ");
    }

  }

  private class UpdateBookmarkListener
      implements LoaderManager.LoaderCallbacks<Boolean> {

    private static final String TAG = "UpdateBookmarkListener";

    @NonNull
    @Override
    public Loader<Boolean> onCreateLoader(final int id, @Nullable final Bundle args) {
      Log.d(TAG, "onCreateLoader:");
      return new UpdateBookmarkLoader(requireContext(),
                                      new Bookmark(model.getCachedReference(), getNote()));
    }

    @Override
    public void onLoadFinished(@NonNull final Loader<Boolean> loader, final Boolean data) {
      Log.d(TAG, "onLoadFinished:");
      final String reference = model.getCachedReference();
      model.doesBookmarkExist(reference)
           .observe(BookmarkScreen.this, rowCount -> {
             final boolean exists = rowCount != null && rowCount > 0;
             if (exists) {
               model.getBookmark(model.getCachedReference())
                    .observe(BookmarkScreen.this, bookmark -> {
                      if (bookmark != null) {
                        noteField.setText(bookmark.getNote());
                      } else {
                        Log.e(TAG, "toggleActionButtons: bookmark with reference["
                                   + reference + "] not found");
                      }
                    });
             }

             toggleActionButtons(exists);
             activityOps.showErrorMessage(
                 exists ? getString(R.string.bookmark_scr_action_update_success) :
                 getString(R.string.bookmark_scr_action_update_failure));
           });
    }

    @Override
    public void onLoaderReset(@NonNull final Loader<Boolean> loader) {

    }

  }

  private class DeleteBookmarkListener
      implements LoaderManager.LoaderCallbacks<Boolean> {

    private static final String TAG = "DeleteBookmarkListener";

    @NonNull
    @Override
    public Loader<Boolean> onCreateLoader(final int id, @Nullable final Bundle args) {
      Log.d(TAG, "onCreateLoader:");
      return new DeleteBookmarkLoader(requireContext(),
                                      new Bookmark(model.getCachedReference(), getNote()));
    }

    @Override
    public void onLoadFinished(@NonNull final Loader<Boolean> loader, final Boolean data) {
      Log.d(TAG, "onLoadFinished:");
      final String reference = model.getCachedReference();
      model.doesBookmarkExist(reference)
           .observe(BookmarkScreen.this, rowCount -> {
             final boolean exists = rowCount != null && rowCount > 0;
             toggleActionButtons(exists);
             activityOps.showErrorMessage(
                 exists ? getString(R.string.bookmark_scr_action_delete_failure) :
                 getString(R.string.bookmark_scr_action_delete_success));
             if (!exists) {
               NavHostFragment.findNavController(BookmarkScreen.this)
                              .navigate(R.id.action_bookmarkScreen_pop);
             }
           });
    }

    @Override
    public void onLoaderReset(@NonNull final Loader<Boolean> loader) {

    }

  }

}
