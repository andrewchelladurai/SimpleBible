package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.EntityBook;
import com.andrewchelladurai.simplebible.data.EntityVerse;
import com.andrewchelladurai.simplebible.model.SearchViewModel;
import com.andrewchelladurai.simplebible.ui.adapter.SearchResultAdapter;
import com.andrewchelladurai.simplebible.ui.ops.SearchScreenOps;
import com.andrewchelladurai.simplebible.ui.ops.SimpleBibleOps;
import com.andrewchelladurai.simplebible.utils.Utils;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.chip.Chip;

import java.util.TreeSet;

public class SearchScreen
    extends Fragment
    implements SearchScreenOps {

  private static final String TAG = "SearchScreen";

  private SimpleBibleOps ops;

  private SearchViewModel model;

  private SearchResultAdapter adapter;

  private View rootView;

  @Override
  public void onAttach(@NonNull final Context context) {
    super.onAttach(context);

    if (context instanceof SimpleBibleOps) {
      ops = (SimpleBibleOps) context;
    } else {
      throw new ClassCastException(TAG + " onAttach: [Context] must implement [SimpleBibleOps]");
    }

    model = ViewModelProvider.AndroidViewModelFactory
                .getInstance(requireActivity().getApplication())
                .create(SearchViewModel.class);
    adapter = new SearchResultAdapter(this, getString(R.string.scr_search_result_template));
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedState) {
    rootView = inflater.inflate(R.layout.search_screen, container, false);

    ((TextView) rootView.findViewById(R.id.scr_search_help_text))
        .setText(HtmlCompat.fromHtml(getString(R.string.scr_search_help_text),
                                     HtmlCompat.FROM_HTML_MODE_COMPACT));

    ((RecyclerView) rootView.findViewById(R.id.scr_search_list)).setAdapter(adapter);

    final SearchView searchView = rootView.findViewById(R.id.scr_search_input);
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

      @Override
      public boolean onQueryTextSubmit(final String query) {

        final String text = (query == null) ? "" : query;
        final boolean valid = validateSearchText(text);
        if (valid) {
          handleActionSearch(text);
        }
        return valid;
      }

      @Override
      public boolean onQueryTextChange(final String newText) {
        return false;
      }
    });

    final BottomAppBar appBar = rootView.findViewById(R.id.scr_search_bottom_app_bar);
    appBar.setOnMenuItemClickListener(item -> {
      switch (item.getItemId()) {
        case R.id.scr_search_menu_action_clear:
          handleActionClear();
          return true;
        case R.id.scr_search_menu_action_bookmark:
          handleActionBookmark();
          return true;
        case R.id.scr_search_menu_action_share:
          handleActionShare();
          return true;
        case R.id.scr_search_menu_action_search:
          final String text = searchView.getQuery().toString();
          final boolean valid = validateSearchText(text);
          if (valid) {
            handleActionSearch(text);
          }
          return true;
        case R.id.scr_search_menu_action_reset:
          handleActionReset();
          return true;
        default:
          Log.d(TAG, "onMenuItemClick: unknown menu item " + item.getTitle());
          return false;
      }
    });

    Log.d(TAG, "onCreateView: savedState [" + (savedState == null) + "]\n"
               + "cachedResultCount [" + (model.getResultCount()) + "]");

    if (model.getResultCount() < 1) {
      handleActionReset();
    } else {
      showSearchResults(model.getCachedText(), model.getResultCount());
    }

    return rootView;
  }

  private void handleActionSearch(@NonNull final String text) {
    Log.d(TAG, "handleActionSearch: text = [" + text + "]");

    model.findVersesContainingText(text).observe(getViewLifecycleOwner(), verseList -> {

      if (verseList == null) {
        showHelpText();
        return;
      }

      model.updateContent(verseList, text);
      showSearchResults(text, verseList.size());

    });

  }

  private boolean validateSearchText(@NonNull final String text) {
    ops.hideKeyboard();

    if (text.isEmpty()) {
      showHelpText();
      ops.showMessage(getString(R.string.scr_search_msg_input_length_none),
                      R.id.scr_search_bottom_app_bar);
      return false;
    }

    if (text.length() < 3) {
      showHelpText();
      ops.showMessage(getString(R.string.scr_search_msg_input_length_min),
                      R.id.scr_search_bottom_app_bar);
      return false;
    }

    if (text.length() > 13) {
      showHelpText();
      ops.showMessage(getString(R.string.scr_search_msg_input_length_max),
                      R.id.scr_search_bottom_app_bar);
      return false;
    }

    final SearchView searchView = rootView.findViewById(R.id.scr_search_input);
    searchView.setQuery("", false);

    return true;
  }

  private void handleActionReset() {
    Log.d(TAG, "handleActionReset:");
    showHelpText();
    model.resetContent();
    adapter.notifyDataSetChanged();
    updateSelectionActionsState();
  }

  private void handleActionBookmark() {
    Log.d(TAG, "handleActionBookmark:");
    final TreeSet<EntityVerse> set = model.getSelectedList();
    if (set.size() < 1) {
      ops.showMessage(getString(R.string.scr_search_msg_selection_none),
                      R.id.scr_search_bottom_app_bar);
      return;
    }

    Log.d(TAG, "handleActionBookmark: [" + set.size() + "] verses selected");

    final StringBuilder ref = new StringBuilder();
    final String separator = Utils.SEPARATOR_BOOKMARK_REFERENCE;

    for (final EntityVerse verse : set) {
      ref.append(verse.getReference())
         .append(separator);
    }
    ref.delete(ref.length() - separator.length(), ref.length());

    Log.d(TAG, "handleActionBookmark: created bookmark reference[" + ref + "]");

    final Bundle bundle = new Bundle();
    bundle.putString(BookmarkScreen.ARG_STR_REFERENCE, String.valueOf(ref));

    NavHostFragment.findNavController(this)
                   .navigate(R.id.nav_from_scr_search_to_scr_bookmark, bundle);
  }

  private void handleActionClear() {
    Log.d(TAG, "handleActionClear:");
    model.clearSelectedList();
    adapter.notifyDataSetChanged();
    updateSelectionActionsState();
  }

  private void handleActionShare() {
    Log.d(TAG, "handleActionShare:");
    final TreeSet<EntityVerse> set = model.getSelectedList();
    if (set.size() < 1) {
      ops.showMessage(getString(R.string.scr_search_msg_selection_none),
                      R.id.scr_search_bottom_app_bar);
      return;
    }

    Log.d(TAG, "handleActionShare: [" + set.size() + "] verses selected");

    final String verseTemplate = getString(R.string.scr_search_result_template);
    final StringBuilder verseText = new StringBuilder();

    EntityBook book;
    for (final EntityVerse verse : set) {

      book = Utils.getInstance().getCachedBook(verse.getBook());
      if (book == null) {
        Log.e(TAG, "updateContent: null book returned");
        continue;
      }

      verseText.append(HtmlCompat.fromHtml(String.format(
          verseTemplate, book.getName(), verse.getChapter(), verse.getVerse(), verse.getText()),
                                           HtmlCompat.FROM_HTML_MODE_COMPACT).toString())
               .append("\n");
    }

    ops.shareText(getString(R.string.scr_search_result_share_template,
                            set.size(),
                            model.getCachedText(),
                            verseText.toString()));
  }

  private void showSearchResults(@NonNull final String text,
                                 @IntRange(from = 0) final int count) {
    Log.d(TAG, "showSearchResults: text = [" + text + "], count = [" + count + "]");
    rootView.findViewById(R.id.scr_search_list).setVisibility(View.VISIBLE);
    rootView.findViewById(R.id.scr_search_contain_help_text).setVisibility(View.GONE);

    final Menu menu =
        ((BottomAppBar) rootView.findViewById(R.id.scr_search_bottom_app_bar)).getMenu();
    menu.findItem(R.id.scr_search_menu_action_search).setVisible(false);
    menu.findItem(R.id.scr_search_menu_action_reset).setVisible(true);
    updateSelectionActionsState();

    ((Chip) rootView.findViewById(R.id.scr_search_title))
        .setText(getString(R.string.scr_search_title_template, count, text));
    adapter.notifyDataSetChanged();
  }

  private void showHelpText() {
    Log.d(TAG, "showHelpText:");
    rootView.findViewById(R.id.scr_search_list).setVisibility(View.GONE);
    rootView.findViewById(R.id.scr_search_contain_help_text).setVisibility(View.VISIBLE);

    final Menu menu =
        ((BottomAppBar) rootView.findViewById(R.id.scr_search_bottom_app_bar)).getMenu();
    menu.findItem(R.id.scr_search_menu_action_search).setVisible(true);
    menu.findItem(R.id.scr_search_menu_action_reset).setVisible(false);
    updateSelectionActionsState();

    ((Chip) rootView.findViewById(R.id.scr_search_title))
        .setText(getString(R.string.application_name));
  }

  @Override
  public void updateSelectionActionsState() {
    final boolean showActions = model.getSelectedList().size() > 0;
    final Menu menu =
        ((BottomAppBar) rootView.findViewById(R.id.scr_search_bottom_app_bar)).getMenu();
    menu.setGroupVisible(R.id.scr_search_menu_group_selected, showActions);

    rootView.findViewById(R.id.scr_search_title)
            .setVisibility((showActions) ? View.GONE : View.VISIBLE);
  }

  @Override
  public boolean isSelected(@NonNull final String verseReference) {
    return model.isSelected(verseReference);
  }

  @Override
  public void removeSelection(@NonNull final String verseReference) {
    model.removeSelection(verseReference);
  }

  @Override
  public void addSelection(@NonNull final String verseReference, @NonNull final EntityVerse verse) {
    model.addSelection(verseReference, verse);
  }

  @NonNull
  @Override
  public EntityVerse getVerseAtPosition(@IntRange(from = 0) final int position) {
    return model.getVerseAtPosition(position);
  }

  @Override
  public int getResultCount() {
    return model.getResultCount();
  }

}
