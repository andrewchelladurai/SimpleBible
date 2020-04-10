package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.model.SearchViewModel;
import com.andrewchelladurai.simplebible.ui.ops.SearchScreenOps;
import com.andrewchelladurai.simplebible.ui.ops.SimpleBibleOps;

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

    if (savedInstanceState == null) {
      showHelpText();
    }

    return rootView;
  }

  private void showHelpText() {
    Log.d(TAG, "showHelpText:");
    rootView.findViewById(R.id.list_view_scr_search).setVisibility(View.GONE);
    rootView.findViewById(R.id.contain_bottom_app_bar_scr_search).setVisibility(View.GONE);

    ((TextView) rootView.findViewById(R.id.help_text_scr_search))
        .setText(HtmlCompat.fromHtml(getString(R.string.help_text_scr_search),
                                     HtmlCompat.FROM_HTML_MODE_COMPACT));
    rootView.findViewById(R.id.contain_help_text_scr_search).setVisibility(View.VISIBLE);
  }

  private void showSearchResults() {
    Log.d(TAG, "showSearchResults:");
    rootView.findViewById(R.id.list_view_scr_search).setVisibility(View.VISIBLE);
    rootView.findViewById(R.id.contain_bottom_app_bar_scr_search).setVisibility(View.VISIBLE);

    rootView.findViewById(R.id.contain_help_text_scr_search).setVisibility(View.GONE);
  }

}
