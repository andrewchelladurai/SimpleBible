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
import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.model.ScreenSearchModel;
import com.andrewchelladurai.simplebible.ui.ops.ScreenSimpleBibleOps;

public class ScreenSearch
    extends Fragment {

  private static final String TAG = "ScreenSearch";

  private ScreenSimpleBibleOps mainOps;
  private View rootView;
  private ScreenSearchModel model;

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
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedState) {
    rootView = inflater.inflate(R.layout.screen_search_fragment, container, false);
    mainOps.showNavigationView();
    mainOps.hideKeyboard();

    if (savedState == null) {
      rootView.findViewById(R.id.scrSearchActionBookmark).setVisibility(View.GONE);
      rootView.findViewById(R.id.scrSearchActionClear).setVisibility(View.GONE);
      rootView.findViewById(R.id.scrSearchActionShare).setVisibility(View.GONE);
      showHelpText();
    }

    final SearchView searchView = rootView.findViewById(R.id.scrSearchInput);
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(final String searchQuery) {
        handleSearchClick(searchQuery);
        return true;
      }

      @Override
      public boolean onQueryTextChange(final String newText) {
        return false;
      }
    });

    return rootView;
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mainOps = null;
  }

  private void handleSearchClick(@NonNull final String searchText) {
    mainOps.hideKeyboard();
    if (searchText.equalsIgnoreCase("")
        || searchText.isEmpty()
        || searchText.length() < 4
        || searchText.length() > 50) {
      mainOps.showMessage(getString(R.string.scrSearchInputErr));
      showHelpText();
      return;
    }
    Log.d(TAG, "handleSearchClick: searchQuery = [" + searchText + "]");
    model.searchTextInVerses(searchText).observe(this, list -> {
      if (list == null || list.isEmpty()) {
        mainOps.showMessage(getString(R.string.scrSearchEmptyResults));
        showHelpText();
        return;
      }
      Log.d(TAG, "handleSearchClick: found [" + list.size() + "] verses");
    });
  }

  private void showHelpText() {
    final Spanned htmlText = HtmlCompat.fromHtml(getString(R.string.scrSearchHelpText),
                                                 HtmlCompat.FROM_HTML_MODE_LEGACY);
    final TextView textView = rootView.findViewById(R.id.scrSearchHelpText);
    textView.setText(htmlText);
    rootView.findViewById(R.id.scrSearchList).setVisibility(View.GONE);
    rootView.findViewById(R.id.scrSearchContainer).setVisibility(View.VISIBLE);
  }

}
