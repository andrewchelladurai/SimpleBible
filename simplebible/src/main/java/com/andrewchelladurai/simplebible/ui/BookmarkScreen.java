package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.EntityBookmark;
import com.andrewchelladurai.simplebible.data.EntityVerse;
import com.andrewchelladurai.simplebible.model.BookmarkViewModel;
import com.andrewchelladurai.simplebible.ui.adapter.BookmarkAdapter;
import com.andrewchelladurai.simplebible.ui.ops.BookmarkScreenOps;
import com.andrewchelladurai.simplebible.ui.ops.SimpleBibleOps;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.Random;

public class BookmarkScreen
    extends Fragment
    implements BookmarkScreenOps {

  private static final String TAG = "BookmarkScreen";

  static final String ARG_STR_REFERENCE = "ARG_STR_REFERENCE";

  private BookmarkViewModel model;

  private SimpleBibleOps ops;

  private BookmarkAdapter adapter;

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
                .create(BookmarkViewModel.class);

    adapter = new BookmarkAdapter(this,
                                  getString(R.string.scr_bookmark_detail_verse_item_template));
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedState) {
    ops.hideKeyboard();
    ops.hideNavigationView();

    rootView = inflater.inflate(R.layout.bookmark_detail_screen, container, false);

    ((BottomAppBar) rootView.findViewById(R.id.scr_bookmark_details_app_bar))
        .setOnMenuItemClickListener(item -> {
          switch (item.getItemId()) {
            case R.id.menu_action_save_scr_bookmark_detail:
              handleActionSave();
              return true;
            case R.id.menu_action_delete_scr_bookmark_detail:
              handleActionDelete();
              return true;
            case R.id.menu_action_edit_scr_bookmark_detail:
              handleActionEdit();
              return true;
            case R.id.menu_action_share_scr_bookmark_detail:
              handleActionShare();
              return true;
            default:
              Log.e(TAG, "onCreateView: unknown Menu item [" + item.getTitle() + "] captured");
              return false;
          }
        });

    ((RecyclerView) rootView.findViewById(R.id.scr_bookmark_details_list)).setAdapter(adapter);

    final Bundle arguments = getArguments();

    if (savedState == null) {
      if (arguments == null) {
        // TODO: 16/4/20 extract error message
        ops.showErrorScreen("No Arguments passed when loading screen", true, false);
        return rootView;
      }

      if (!arguments.containsKey(ARG_STR_REFERENCE)) {
        // TODO: 16/4/20 extract error message
        ops.showErrorScreen("No Bookmark Reference Passed when loading screen", true, false);
        return rootView;
      }

      Log.d(TAG, "onCreateView: first run");
      updateContent(arguments.getString(ARG_STR_REFERENCE, ""));
    } else {
      Log.d(TAG, "onCreateView: NOT the first run");
      final EntityBookmark bookmark = model.getCachedBookmark();

      if (bookmark != null) {
        updateContent(bookmark.getReference());
      } else {
        if (arguments != null) {
          updateContent(arguments.getString(ARG_STR_REFERENCE, ""));
        } else {
          // TODO: 16/4/20 extract error message
          ops.showErrorScreen("No Bookmark Reference Passed when loading screen", true, false);
          return rootView;
        }
      }
    }

    return rootView;
  }

  private void handleActionSave() {
    Log.d(TAG, "handleActionSave:");
    final String noteText = getNoteFieldText();

    final EntityBookmark bookmark =
        new EntityBookmark(model.getCachedBookmarkReference(), noteText);

    final int taskId = new Random().nextInt();

    final AsyncTaskLoader<Boolean> saveTask = new AsyncTaskLoader<Boolean>(requireContext()) {

      @Nullable
      @Override
      public Boolean loadInBackground() {
        return model.saveBookmark(bookmark);
      }
    };

    final LoaderManager.LoaderCallbacks<Boolean> saveTaskListener =
        new LoaderManager.LoaderCallbacks<Boolean>() {

          @NonNull
          @Override
          public Loader<Boolean> onCreateLoader(final int id, @Nullable final Bundle args) {
            return saveTask;
          }

          @Override
          public void onLoadFinished(@NonNull final Loader<Boolean> loader,
                                     final Boolean saved) {
            if (saved) {
              model.setCachedBookmark(bookmark);
              model.setCachedBookmarkReference(bookmark.getReference());

              updateContent(model.getCachedBookmarkReference());
              ops.showMessage(
                  getString(R.string.scr_bookmark_detail_msg_saved),
                  R.id.scr_bookmark_details_app_bar);
            }
          }

          @Override
          public void onLoaderReset(@NonNull final Loader<Boolean> loader) {
          }
        };

    LoaderManager.getInstance(requireActivity())
                 .initLoader(taskId, Bundle.EMPTY, saveTaskListener)
                 .forceLoad();
  }

  private void refreshContent() {
    Log.d(TAG, "refreshContent:");

    final EntityBookmark bookmark = model.getCachedBookmark();
    final TextInputEditText noteField = rootView.findViewById(R.id.scr_bookmark_details_note);
    final Chip title = rootView.findViewById(R.id.scr_bookmark_details_title);

    final int verseCount = model.getCachedVerseListSize();
    final String titleCount = getResources().getQuantityString(
        R.plurals.scr_bookmark_detail_title_template_verse_count, verseCount,
        verseCount);

    if (bookmark == null) {
      showActionGroupNew();
      title.setText(getString(R.string.scr_bookmark_detail_title_template,
                              getString(R.string.scr_bookmark_detail_title_template_bookmark_new),
                              titleCount));

      noteField.setHint(R.string.scr_bookmark_detail_note_new);
    } else {
      final String note = bookmark.getNote();
      final boolean emptyNote = note.isEmpty();

      showActionGroupExisting();
      noteField.setEnabled(false);

      final String titleState =
          getString(emptyNote ? R.string.scr_bookmark_detail_title_template_bookmark_empty
                              : R.string.scr_bookmark_detail_title_template_bookmark_saved);

      title.setText(getString(R.string.scr_bookmark_detail_title_template, titleState, titleCount));

      noteField.setHint(emptyNote ? getString(R.string.scr_bookmark_detail_note_empty) : note);
      noteField.setText(emptyNote ? getString(R.string.scr_bookmark_detail_note_empty) : note);
    }

    // grab focus so that the text-layout hint and text-field hint does not overlay each other
    noteField.requestFocus();

    adapter.notifyDataSetChanged();

  }

  private void showActionGroupNew() {
    final Menu menu = ((BottomAppBar) rootView.findViewById(R.id.scr_bookmark_details_app_bar))
                          .getMenu();
    menu.setGroupVisible(R.id.menu_group_new_scr_bookmark_detail, true);
    menu.setGroupVisible(R.id.menu_group_existing_scr_bookmark_detail, false);
  }

  private void showActionGroupExisting() {
    final Menu menu = ((BottomAppBar) rootView.findViewById(R.id.scr_bookmark_details_app_bar))
                          .getMenu();
    menu.setGroupVisible(R.id.menu_group_existing_scr_bookmark_detail, true);
    menu.setGroupVisible(R.id.menu_group_new_scr_bookmark_detail, false);
  }

  @NonNull
  private String getNoteFieldText() {
    final Editable noteField =
        ((TextInputEditText) rootView.findViewById(R.id.scr_bookmark_details_note)).getText();
    return (noteField == null) ? "" : noteField.toString();
  }

  private void handleActionDelete() {
    Log.d(TAG, "handleActionDelete:");
  }

  private void updateContent(@NonNull final String reference) {
    final EntityBookmark cachedBookmark = model.getCachedBookmark();

    if (cachedBookmark != null
        && model.getCachedBookmarkReference().equalsIgnoreCase(reference)) {
      Log.d(TAG, "updateContent: reference is already cached");
      refreshContent();
      return;
    }

    // validate the passed bookmark reference
    if (!model.validateBookmarkReference(reference)) {
      // TODO: 16/4/20 extract error message
      ops.showErrorScreen("Invalid Bookmark Reference [" + reference + "] passed", true, false);
      return;
    }

    // get the references of each verses present in the bookmark reference
    final String[] verseReferenceList = model.getVersesForBookmarkReference(reference);
    if (verseReferenceList.length < 1) {
      // TODO: 16/4/20 extract error message
      ops.showErrorScreen("No Verse references found in bookmark reference ["
                          + reference + "]", true, true);
      return;
    }

    // use the verse references to get the individual verses
    final LiveData<List<EntityVerse>> verses = model.getVerses(verseReferenceList);
    if (verses == null) {
      // TODO: 16/4/20 extract error message
      ops.showErrorScreen("No Verses found for bookmark reference ["
                          + reference + "]", true, false);
      return;
    }

    final LifecycleOwner lifeOwner = getViewLifecycleOwner();

    // if we have a valid live data object, observe it
    verses.observe(lifeOwner, verseList -> {
      if (verseList.isEmpty()) {
        // TODO: 16/4/20 extract error message
        ops.showErrorScreen("No Verses found for bookmark reference ["
                            + reference + "]", true, false);
        return;
      }

      // get the bookmark from the database using the bookmark reference
      model.getBookmarkForReference(reference).observe(lifeOwner, bookmark -> {
        model.setCachedBookmark(bookmark);
        model.setCachedVerses(verseList);
        model.setCachedBookmarkReference(reference);
        refreshContent();
      });
    });

  }

  private void handleActionShare() {
    Log.d(TAG, "handleActionShare:");
  }

  @Nullable
  @Override
  public EntityVerse getVerseAtPosition(@IntRange(from = 0) final int position) {
    return model.getVerseAtPosition(position);
  }

  @IntRange(from = 0)
  @Override
  public int getCachedVerseListSize() {
    return model.getCachedVerseListSize();
  }

  private void handleActionEdit() {
    Log.d(TAG, "handleActionEdit:");

    final TextInputEditText noteField = rootView.findViewById(R.id.scr_bookmark_details_note);
    final Chip title = rootView.findViewById(R.id.scr_bookmark_details_title);
    final int verseCount = model.getCachedVerseListSize();
    final String titleCount = getResources().getQuantityString(
        R.plurals.scr_bookmark_detail_title_template_verse_count, verseCount,
        verseCount);
    final String note = model.getCachedBookmark().getNote();
    final boolean emptyNote = note.isEmpty();

    title.setText(getString(R.string.scr_bookmark_detail_title_template,
                            getString(R.string.scr_bookmark_detail_title_template_bookmark_new),
                            titleCount));

    noteField.setHint(emptyNote ? getString(R.string.scr_bookmark_detail_note_empty) : note);
    noteField.setText(emptyNote ? getString(R.string.scr_bookmark_detail_note_empty) : note);

    showActionGroupNew();
    noteField.setEnabled(true);

    // grab focus so that the text-layout hint and text-field hint does not overlay each other
    noteField.requestFocus();
  }

}
