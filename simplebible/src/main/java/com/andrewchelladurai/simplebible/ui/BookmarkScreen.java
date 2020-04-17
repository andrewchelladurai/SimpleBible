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
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.EntityBookmark;
import com.andrewchelladurai.simplebible.data.EntityVerse;
import com.andrewchelladurai.simplebible.model.BookmarkViewModel;
import com.andrewchelladurai.simplebible.ui.ops.BookmarkScreenOps;
import com.andrewchelladurai.simplebible.ui.ops.SimpleBibleOps;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class BookmarkScreen
    extends Fragment
    implements BookmarkScreenOps {

  private static final String TAG = "BookmarkScreen";

  static final String ARG_STR_REFERENCE = "ARG_STR_REFERENCE";

  private BookmarkViewModel model;

  private SimpleBibleOps ops;

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

  private void updateContent(@NonNull final String reference) {
    final EntityBookmark cachedBookmark = model.getCachedBookmark();

    if (cachedBookmark != null
        && cachedBookmark.getReference().equalsIgnoreCase(reference)) {
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
        refreshContent();
      });
    });

  }

  private void refreshContent() {
    Log.d(TAG, "refreshContent:");

    final EntityBookmark bookmark = model.getCachedBookmark();
    final TextInputEditText noteField = rootView.findViewById(R.id.scr_bookmark_details_note);
    if (bookmark == null) {
      noteField.setText(R.string.scr_bookmark_details_note_new);
    } else if (bookmark.getNote().isEmpty()) {
      noteField.setText(R.string.scr_bookmark_details_note_empty);
    } else {
      noteField.setText(bookmark.getNote());
    }

    final ArrayList<EntityVerse> verses = model.getCachedVerses();
    Log.d(TAG, "refreshContent: got [" + verses.size() + "] verses");
  }

  private void handleActionSave() {
    Log.d(TAG, "handleActionSave:");
  }

  private void handleActionDelete() {
    Log.d(TAG, "handleActionDelete:");
  }

  private void handleActionEdit() {
    Log.d(TAG, "handleActionEdit:");
  }

  private void handleActionShare() {
    Log.d(TAG, "handleActionShare:");
  }

}
