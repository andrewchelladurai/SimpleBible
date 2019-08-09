package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.entity.Verse;
import com.andrewchelladurai.simplebible.model.ScreenSearchModel;
import com.andrewchelladurai.simplebible.ui.adapter.ScreenSearchAdapter;
import com.andrewchelladurai.simplebible.ui.ops.ScreenSearchOps;
import com.andrewchelladurai.simplebible.ui.ops.ScreenSimpleBibleOps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ScreenSearch
    extends Fragment
    implements ScreenSearchOps {

  private static final String TAG = "ScreenSearch";

  private ScreenSimpleBibleOps mainOps;
  private View rootView;
  private ScreenSearchModel model;
  private ScreenSearchAdapter adapter;
  private String searchResultContentTemplate;

  public ScreenSearch() {
  }

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    if (!(context instanceof ScreenSimpleBibleOps)) {
      throw new RuntimeException(context.toString() + " must implement ScreenSimpleBibleOps");
    }
    mainOps = (ScreenSimpleBibleOps) context;
    model = ViewModelProviders.of(this).get(ScreenSearchModel.class);
    adapter = new ScreenSearchAdapter(this);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedState) {
    rootView = inflater.inflate(R.layout.screen_search_fragment, container, false);
    mainOps.showNavigationView();
    mainOps.hideKeyboard();
    searchResultContentTemplate = getString(R.string.itemSearchResultContentTemplate);

    rootView.findViewById(R.id.scrSearchActionBookmark)
            .setOnClickListener(v -> handleClickActionBookmark());

    rootView.findViewById(R.id.scrSearchActionShare)
            .setOnClickListener(v -> handleClickActionShare());

    rootView.findViewById(R.id.scrSearchActionClear)
            .setOnClickListener(v -> handleClickActionClear());

    rootView.findViewById(R.id.scrSearchActionReset)
            .setOnClickListener(v -> handleClickActionReset());

    final SearchView searchView = rootView.findViewById(R.id.scrSearchInput);
    searchView.setQuery("rachel", false);
    searchView.setSubmitButtonEnabled(true);
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

      @Override
      public boolean onQueryTextSubmit(final String searchQuery) {
        handleClickSearchInput(searchQuery);
        return true;
      }

      @Override
      public boolean onQueryTextChange(final String newText) {
        return false;
      }
    });

    ((RecyclerView) rootView.findViewById(R.id.scrSearchList))
        .setAdapter(adapter);

    // first run - since we do not have a previously saved instance
    if (savedState == null) {
      showHelpText();
      showInputField();
    } else {
      if (adapter.getItemCount() > 0) {
        if (adapter.getSelectedItemCount() > 0) {
          enableSelectionActions();
        } else {
          disableSelectionActions();
        }
        showSearchResultsList();
        showActions();
      } else {
        showHelpText();
        showInputField();
      }
    }

    return rootView;
  }

  @Override
  public void onDetach() {
    super.onDetach();
    model = null;
    adapter = null;
    mainOps = null;
  }

  private void handleClickActionReset() {
    ((SearchView) rootView.findViewById(R.id.scrSearchInput)).setQuery("", false);
    adapter.clearList();
    showInputField();
    showHelpText();
  }

  private void handleClickActionClear() {
    adapter.clearSelection();
    disableSelectionActions();
  }

  private void handleClickActionShare() {
    final StringBuilder shareText = new StringBuilder();

    // get the list of all verses that are selected and sort it
    final HashMap<Verse, String> versesMap = adapter.getSelectedVerses();
    final ArrayList<Verse> keySet = new ArrayList<>(versesMap.keySet());
    //noinspection unchecked
    Collections.sort(keySet);

    // now get the text from the selected verses
    for (final Verse verse : keySet) {
      shareText.append(versesMap.get(verse)).append("\n");
    }

    mainOps.shareText(String.format(getString(R.string.scrSearchShareTemplate), shareText));
  }

  private void handleClickActionBookmark() {
    // get the list of all verses that are selected and sort it
    final ArrayList<Verse> list = new ArrayList<>(adapter.getSelectedVerses().keySet());
    //noinspection unchecked
    Collections.sort(list);

    // now clear the selection, since our work is done.
    handleClickActionClear();

    // convert the list into an array
    final Verse[] array = new Verse[list.size()];
    for (int i = 0; i < array.length; i++) {
      array[i] = list.get(i);
    }

    // now create a bundle of the verses to pass to the Bookmark Screen
    final Bundle bundle = new Bundle();
    bundle.putParcelableArray(ScreenBookmark.ARG_VERSE_LIST, array);

    NavHostFragment.findNavController(this)
                   .navigate(R.id.action_screenSearch_to_screenBookmark, bundle);
  }

  private void handleClickSearchInput(@NonNull final String searchText) {
    mainOps.hideKeyboard();
    if (searchText.equalsIgnoreCase("")
        || searchText.isEmpty()
        || searchText.length() < 4
        || searchText.length() > 50) {
      mainOps.showMessage(getString(R.string.scrSearchInputErr));
      showHelpText();
      return;
    }

    Log.d(TAG, "handleClickSearchInput: searchQuery = [" + searchText + "]");
    model.searchTextInVerses(searchText).observe(this, list -> {
      if (list == null || list.isEmpty()) {
        mainOps.showMessage(getString(R.string.scrSearchEmptyResults));
        showHelpText();
        return;
      }

      Log.d(TAG, "handleClickSearchInput: found [" + list.size() + "] verses");
      adapter.updateList(list);
      showSearchResultsList();
      disableSelectionActions();
      showActions();
    });
  }

  private void showActions() {
    rootView.findViewById(R.id.scrSearchInput).setVisibility(View.GONE);
    rootView.findViewById(R.id.scrSearchActionsContainer).setVisibility(View.VISIBLE);
  }

  private void showInputField() {
    rootView.findViewById(R.id.scrSearchInput).setVisibility(View.VISIBLE);
    rootView.findViewById(R.id.scrSearchActionsContainer).setVisibility(View.GONE);
  }

  private void enableSelectionActions() {
    rootView.findViewById(R.id.scrSearchActionBookmark).setVisibility(View.VISIBLE);
    rootView.findViewById(R.id.scrSearchActionShare).setVisibility(View.VISIBLE);
    rootView.findViewById(R.id.scrSearchActionClear).setVisibility(View.VISIBLE);
    rootView.findViewById(R.id.scrSearchActionReset).setEnabled(true);
  }

  private void disableSelectionActions() {
    rootView.findViewById(R.id.scrSearchActionBookmark).setVisibility(View.GONE);
    rootView.findViewById(R.id.scrSearchActionShare).setVisibility(View.GONE);
    rootView.findViewById(R.id.scrSearchActionClear).setVisibility(View.GONE);
    rootView.findViewById(R.id.scrSearchActionReset).setEnabled(true);
  }

  private void showHelpText() {
    adapter.clearList();

    final Spanned htmlText = HtmlCompat.fromHtml(getString(R.string.scrSearchHelpText),
                                                 HtmlCompat.FROM_HTML_MODE_LEGACY);
    final TextView textView = rootView.findViewById(R.id.scrSearchHelpText);
    textView.setText(htmlText);
    rootView.findViewById(R.id.scrSearchList).setVisibility(View.GONE);
    rootView.findViewById(R.id.scrSearchContainer).setVisibility(View.VISIBLE);
  }

  private void showSearchResultsList() {
    rootView.findViewById(R.id.scrSearchList).setVisibility(View.VISIBLE);
    rootView.findViewById(R.id.scrSearchContainer).setVisibility(View.GONE);
  }

  @Override
  public void updateSearchResultView(@NonNull Verse verse, @NonNull TextView textView) {
    model.getBook(verse.getBook()).observe(this, book -> {
      if (book == null) {
        Log.e(TAG, "updateSearchResultView: book not found for verse [" + verse + "]");
        return;
      }

      textView.setText(HtmlCompat.fromHtml(
          String.format(searchResultContentTemplate,
                        book.getName(),
                        verse.getChapter(),
                        verse.getVerse(),
                        verse.getText()),
          HtmlCompat.FROM_HTML_MODE_LEGACY));
    });
  }

  @Override
  public void updateSelectionActionState() {
    if (adapter.getSelectedItemCount() > 0) {
      enableSelectionActions();
    } else {
      disableSelectionActions();
    }
  }

}
