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
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.db.entities.EntityBookmark;
import com.andrewchelladurai.simplebible.db.entities.EntityVerse;
import com.andrewchelladurai.simplebible.model.Book;
import com.andrewchelladurai.simplebible.model.Bookmark;
import com.andrewchelladurai.simplebible.model.Verse;
import com.andrewchelladurai.simplebible.model.view.BookmarkViewModel;
import com.andrewchelladurai.simplebible.ui.adapter.BookmarkAdapter;
import com.andrewchelladurai.simplebible.ui.ops.BookmarkScreenOps;
import com.andrewchelladurai.simplebible.ui.ops.SimpleBibleOps;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
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

    model = ViewModelProvider.AndroidViewModelFactory.getInstance(
      requireActivity().getApplication())
                                                     .create(BookmarkViewModel.class);

    adapter = new BookmarkAdapter(this, getString(R.string.scr_bookmark_template_verse_item));
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedState) {
    ops.hideKeyboard();
    ops.hideNavigationView();

    rootView = inflater.inflate(R.layout.bookmark_screen, container, false);

    ((BottomAppBar) rootView.findViewById(R.id.scr_bookmark_app_bar)).setOnMenuItemClickListener(
      item -> {
        switch (item.getItemId()) {
          case R.id.scr_bookmark_menu_action_save:
            handleActionSave();
            return true;
          case R.id.scr_bookmark_menu_action_delete:
            handleActionDelete();
            return true;
          case R.id.scr_bookmark_menu_action_edit:
            handleActionEdit();
            return true;
          case R.id.scr_bookmark_menu_action_share:
            handleActionShare();
            return true;
          default:
            Log.e(TAG, "onCreateView: unknown Menu item [" + item.getTitle() + "] captured");
            return false;
        }
      });

    ((RecyclerView) rootView.findViewById(R.id.scr_bookmark_list)).setAdapter(adapter);

    final Bundle arguments = getArguments();

    if (savedState == null) {
      if (arguments == null) {
        ops.showErrorScreen(getString(R.string.scr_bookmark_msg_empty_args), true, false);
        return rootView;
      }

      if (!arguments.containsKey(ARG_STR_REFERENCE)) {
        ops.showErrorScreen(getString(R.string.scr_bookmark_msg_no_reference), true, false);
        return rootView;
      }

      Log.d(TAG, "onCreateView: first run");
      updateContent(arguments.getString(ARG_STR_REFERENCE, ""));
    } else {
      Log.d(TAG, "onCreateView: NOT the first run");
      final Bookmark bookmark = model.getCachedBookmark();

      if (bookmark != null) {
        updateContent(bookmark.getReference());
      } else {
        if (arguments != null) {
          updateContent(arguments.getString(ARG_STR_REFERENCE, ""));
        } else {
          ops.showErrorScreen(getString(R.string.scr_bookmark_msg_no_reference), true, false);
          return rootView;
        }
      }
    }

    return rootView;
  }

  private void handleActionSave() {
    Log.d(TAG, "handleActionSave:");

    final String note = getNoteFieldText();
    final Bookmark bookmark = model.getCachedBookmark();

    if (bookmark == null) {
      Log.e(TAG, "handleActionSave:",
            new IllegalArgumentException("null bookmark, can not delete"));
      // TODO: 27/5/20 show an error screen if this happens
      return;
    }

    final int taskId = new Random().nextInt();

    final LoaderManager.LoaderCallbacks<Boolean> taskListener =
      new LoaderManager.LoaderCallbacks<Boolean>() {

        @NonNull
        @Override
        public Loader<Boolean> onCreateLoader(final int id, @Nullable final Bundle args) {
          return new AsyncTaskLoader<Boolean>(requireContext()) {

            @Override
            public Boolean loadInBackground() {
              return model.saveBookmark(new EntityBookmark(bookmark.getReference(), note));
            }
          };
        }

        @Override
        public void onLoadFinished(@NonNull final Loader<Boolean> loader, final Boolean saved) {
          if (saved) {
            updateContent(bookmark.getReference());
            ops.showMessage(getString(R.string.scr_bookmark_msg_saved), R.id.scr_bookmark_app_bar);
          }
        }

        @Override
        public void onLoaderReset(@NonNull final Loader<Boolean> loader) {
        }
      };

    LoaderManager.getInstance(requireActivity())
                 .initLoader(taskId, Bundle.EMPTY, taskListener)
                 .forceLoad();
  }

  private void refreshContent(final boolean bookmarkExists) {
    final Bookmark bookmark = model.getCachedBookmark();

    if (bookmark == null) {
      ops.showErrorScreen("Cached Bookmark can not be null", true, true);
      Log.e(TAG, "refreshContent: cachedBookmark is null",
            new IllegalStateException("refreshContent: cachedBookmark is null"));
      return;
    }

    final TextInputEditText noteField = rootView.findViewById(R.id.scr_bookmark_note);
    final Chip title = rootView.findViewById(R.id.scr_bookmark_title);

    final int verseCount = model.getCachedVerseListSize();
    final String titleCount = getResources().getQuantityString(
      R.plurals.scr_bookmark_template_title_verse_count, verseCount, verseCount);

    if (!bookmarkExists) {
      showActionGroupNew();
      title.setText(getString(R.string.scr_bookmark_template_title,
                              getString(R.string.scr_bookmark_template_title_bookmark_new),
                              titleCount));
      noteField.setHint(R.string.scr_bookmark_note_new);
    } else {
      final String note = bookmark.getNote();
      final boolean emptyNote = note.isEmpty();

      showActionGroupExisting();
      noteField.setEnabled(false);

      final String titleState = getString(
        emptyNote ? R.string.scr_bookmark_template_title_bookmark_empty
                  : R.string.scr_bookmark_template_title_bookmark_saved);

      title.setText(getString(R.string.scr_bookmark_template_title, titleState, titleCount));

      noteField.setHint(emptyNote ? getString(R.string.scr_bookmark_note_empty) : note);
      noteField.setText(emptyNote ? getString(R.string.scr_bookmark_note_empty) : note);
    }

    // grab focus so that the text-layout hint and text-field hint does not overlay each other
    noteField.requestFocus();

    adapter.notifyDataSetChanged();

  }

  private void showActionGroupNew() {
    final Menu menu = ((BottomAppBar) rootView.findViewById(R.id.scr_bookmark_app_bar)).getMenu();
    menu.setGroupVisible(R.id.scr_bookmark_menu_group_new, true);
    menu.setGroupVisible(R.id.scr_bookmark_menu_group_existing, false);
  }

  private void showActionGroupExisting() {
    final Menu menu = ((BottomAppBar) rootView.findViewById(R.id.scr_bookmark_app_bar)).getMenu();
    menu.setGroupVisible(R.id.scr_bookmark_menu_group_existing, true);
    menu.setGroupVisible(R.id.scr_bookmark_menu_group_new, false);
  }

  @NonNull
  private String getNoteFieldText() {
    final Editable noteField = ((TextInputEditText) rootView.findViewById(
      R.id.scr_bookmark_note)).getText();
    return (noteField == null) ? "" : noteField.toString();
  }

  private void updateContent(@NonNull final String reference) {
    final Bookmark cachedBookmark = model.getCachedBookmark();

    if (cachedBookmark != null && cachedBookmark.getReference()
                                                .equalsIgnoreCase(reference)) {
      Log.d(TAG, "updateContent: reference is already cached");
      refreshContent(true);
      return;
    }

    // validate the passed bookmark reference
    Log.d(TAG, "updateContent: reference = [" + reference + "]");
    if (!model.validateBookmarkReference(reference)) {
      ops.showErrorScreen(getString(R.string.scr_bookmark_msg_invalid_reference, reference), true,
                          false);
      return;
    }
    Log.d(TAG, "updateContent: validatedBookmarkReference");

    // get the references of each verses present in the bookmark reference
    final String[] verseReferenceList = model.getVersesForBookmarkReference(reference);
    if (verseReferenceList.length < 1) {
      ops.showErrorScreen(getString(R.string.scr_bookmark_msg_no_verse_found, reference), true,
                          true);
      return;
    }
    Log.d(TAG, "updateContent: verseReferenceList >= 1");

    // use the verse references to get the individual verses
    model.getVerses(verseReferenceList)
         .observe(getViewLifecycleOwner(), list -> {
           if (list == null || list.isEmpty()) {
             ops.showErrorScreen(getString(R.string.scr_bookmark_msg_no_verse_found, reference),
                                 true, true);
             return;
           }

           final ArrayList<Verse> verseList = new ArrayList<>(list.size());
           final Book[] book = new Book[1];
           for (final EntityVerse verse : list) {
             book[0] = Book.getCachedBook(verse.getBook());
             if (book[0] == null) {
               Log.e(TAG, "updateContent: no book found for verse [" + verse + ", skipping it]");
               continue;
             }
             verseList.add(new Verse(verse, book[0]));
           }
           Log.d(TAG, "updateContent: got [" + verseList.size() + "] verses from reference");

           // get the bookmark from the database using the bookmark reference
           model.getBookmarkForReference(reference)
                .observe(getViewLifecycleOwner(), bookmark -> {

                  final boolean bookmarkExists = bookmark != null;
                  final Bookmark[] bMark = new Bookmark[1];
                  if (bookmarkExists) {
                    bMark[0] = new Bookmark(bookmark, verseList);
                  } else {
                    bMark[0] = new Bookmark(new EntityBookmark(reference, ""), verseList);
                  }
                  model.setCachedBookmark(bMark[0]);
                  refreshContent(bookmarkExists);
                });
         });
  }

  private void handleActionDelete() {
    Log.d(TAG, "handleActionDelete:");

    final Bookmark bookmark = model.getCachedBookmark();
    if (bookmark == null) {
      Log.e(TAG, "handleActionDelete: ",
            new IllegalArgumentException("null bookmark, can not delete"));
      // TODO: 27/5/20 show and error screen here
      return;
    }

    final LoaderManager.LoaderCallbacks<Boolean> taskListener =
      new LoaderManager.LoaderCallbacks<Boolean>() {

        @NonNull
        @Override
        public Loader<Boolean> onCreateLoader(final int id, @Nullable final Bundle args) {
          return new AsyncTaskLoader<Boolean>(requireContext()) {

            @Override
            public Boolean loadInBackground() {
              return model.deleteBookmark(
                new EntityBookmark(bookmark.getReference(), bookmark.getNote()));
            }
          };
        }

        @Override
        public void onLoadFinished(@NonNull final Loader<Boolean> loader, final Boolean deleted) {
          if (deleted) {
            model.clearCache();
            ops.showMessage(getString(R.string.scr_bookmark_msg_deleted), R.id.main_nav_bar);
            NavHostFragment.findNavController(BookmarkScreen.this)
                           .popBackStack();
          }
        }

        @Override
        public void onLoaderReset(@NonNull final Loader<Boolean> loader) {

        }
      };

    LoaderManager.getInstance(requireActivity())
                 .initLoader(new Random().nextInt(), Bundle.EMPTY, taskListener)
                 .forceLoad();
  }

  private void handleActionShare() {
    Log.d(TAG, "handleActionShare:");
    final Bookmark bookmark = model.getCachedBookmark();
    if (bookmark == null) {
      ops.showMessage("This is mot a Saved Bookmark", R.id.scr_bookmark_app_bar);
      return;
    }

    final ArrayList<String> verseTexts = adapter.getVerseTexts();
    final StringBuilder verses = new StringBuilder();
    for (final String text : verseTexts) {
      verses.append(text)
            .append("\n");
    }

    ops.shareText(getString(R.string.scr_bookmark_template_share, // template
                            verses.toString(), // transformed verses
                            (bookmark.getNote()
                                     .isEmpty()) // note text, use placeholder if empty
                            ? getString(R.string.scr_bookmark_note_empty) : bookmark.getNote()));
  }

  @Nullable
  @Override
  public Verse getVerseAtPosition(@IntRange(from = 0) final int position) {
    return model.getVerseAtPosition(position);
  }

  @IntRange(from = 0)
  @Override
  public int getCachedVerseListSize() {
    return model.getCachedVerseListSize();
  }

  private void handleActionEdit() {
    Log.d(TAG, "handleActionEdit:");

    final TextInputEditText noteField = rootView.findViewById(R.id.scr_bookmark_note);
    final Chip title = rootView.findViewById(R.id.scr_bookmark_title);
    final int verseCount = model.getCachedVerseListSize();
    final String titleCount = getResources().getQuantityString(
      R.plurals.scr_bookmark_template_title_verse_count, verseCount, verseCount);
    //noinspection ConstantConditions
    final String note = model.getCachedBookmark()
                             .getNote();
    final boolean emptyNote = note.isEmpty();

    title.setText(getString(R.string.scr_bookmark_template_title,
                            getString(R.string.scr_bookmark_template_title_bookmark_new),
                            titleCount));

    noteField.setHint(emptyNote ? getString(R.string.scr_bookmark_note_empty) : note);
    noteField.setText(emptyNote ? getString(R.string.scr_bookmark_note_empty) : note);

    showActionGroupNew();
    noteField.setEnabled(true);

    // grab focus so that the text-layout hint and text-field hint does not overlay each other
    noteField.requestFocus();
  }

}
