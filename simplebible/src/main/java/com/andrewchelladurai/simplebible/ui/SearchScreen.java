package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.model.SearchViewModel;
import com.andrewchelladurai.simplebible.ui.ops.SearchScreenOps;
import com.andrewchelladurai.simplebible.ui.ops.SimpleBibleOps;
import com.google.android.material.bottomappbar.BottomAppBar;

public class SearchScreen
    extends Fragment
    implements SearchScreenOps {

  private static final String TAG = "SearchScreen";

  private SearchViewModel model;

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
                .create(SearchViewModel.class);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    Log.d(TAG, "onCreateView:");
    rootView = inflater.inflate(R.layout.search_screen, container, false);

    final BottomAppBar appBar = rootView.findViewById(R.id.bottom_app_bar_scr_search);
    appBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

      @Override
      public boolean onMenuItemClick(final MenuItem item) {
        switch (item.getItemId()) {
          case R.id.action_menu_scr_search_clear:
            handleActionClear();
            return true;
          case R.id.action_menu_scr_search_bookmark:
            handleActionBookmark();
            return true;
          case R.id.action_menu_scr_search_share:
            handleActionShare();
            return true;
          case R.id.action_menu_scr_search_search:
            final SearchView searchView = rootView.findViewById(R.id.search_view_scr_search);
            handleActionSearch(searchView.getQuery().toString());
            return true;
          default:
            Log.d(TAG, "onMenuItemClick: unknown menu item " + item.getTitle());
            return false;
        }
      }
    });

    final SearchView searchView = rootView.findViewById(R.id.search_view_scr_search);
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

      @Override
      public boolean onQueryTextSubmit(final String query) {
        handleActionSearch((query == null) ? "" : query);
        return true;
      }

      @Override
      public boolean onQueryTextChange(final String newText) {
        return false;
      }
    });

    if (savedInstanceState == null) {
      showHelpText();
    }

    return rootView;
  }

  private void showHelpText() {
    Log.d(TAG, "showHelpText:");
    rootView.findViewById(R.id.list_view_scr_search).setVisibility(View.GONE);

    setVerseSelectionActionsVisibility(false);

    ((TextView) rootView.findViewById(R.id.help_text_scr_search))
        .setText(HtmlCompat.fromHtml(getString(R.string.help_text_scr_search),
                                     HtmlCompat.FROM_HTML_MODE_COMPACT));
    rootView.findViewById(R.id.contain_help_text_scr_search).setVisibility(View.VISIBLE);
  }

  private void showSearchResults() {
    Log.d(TAG, "showSearchResults:");
    rootView.findViewById(R.id.list_view_scr_search).setVisibility(View.VISIBLE);

    setVerseSelectionActionsVisibility(false);

    rootView.findViewById(R.id.contain_help_text_scr_search).setVisibility(View.GONE);
  }

  private void setVerseSelectionActionsVisibility(final boolean visible) {
    final BottomAppBar appBar = rootView.findViewById(R.id.bottom_app_bar_scr_search);
    appBar.getMenu().setGroupVisible(R.id.menu_group_selection_scr_search, visible);
  }

  private void handleActionShare() {
    Log.d(TAG, "handleActionShare:");
  }

  private void handleActionBookmark() {
    Log.d(TAG, "handleActionBookmark:");
  }

  private void handleActionClear() {
    Log.d(TAG, "handleActionClear:");
  }

  private void handleActionSearch(@NonNull final String text) {
    Log.d(TAG, "handleActionSearch: text = [" + text + "]");
    if (text.isEmpty()) {
      showHelpText();
      return;
    }
  }

}
