package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.entities.Verse;
import com.andrewchelladurai.simplebible.model.SearchScreenModel;
import com.andrewchelladurai.simplebible.ui.adapter.SearchScreenAdapter;
import com.andrewchelladurai.simplebible.ui.ops.SearchScreenOps;
import com.andrewchelladurai.simplebible.ui.ops.SimpleBibleScreenOps;
import java.util.HashSet;
import java.util.List;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class SearchScreen
    extends Fragment
    implements SearchScreenOps {

  private static final String TAG = "SearchScreen";
  private static String contentTemplate;
  private SimpleBibleScreenOps mainOps;
  private SearchScreenModel model;
  private SearchScreenAdapter adapter;

  private View rootView;

  public SearchScreen() {
  }

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    if (context instanceof SimpleBibleScreenOps) {
      mainOps = (SimpleBibleScreenOps) context;
      model = ViewModelProviders.of(this).get(SearchScreenModel.class);
      adapter = new SearchScreenAdapter(this);
    } else {
      throw new RuntimeException(
          context.toString() + " must implement SimpleBibleScreenOps");
    }
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedState) {
    rootView = inflater.inflate(R.layout.search_screen, container, false);

    final SearchView searchView = rootView.findViewById(R.id.search_scr_query_field);
    searchView.setSubmitButtonEnabled(true);
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(final String query) {
        if (query == null || query.isEmpty()) {
          showErrorMessage(getString(R.string.search_scr_err_empty_input));
        } else if (query.trim().length() < 3) {
          showErrorMessage(getString(R.string.search_scr_err_count_min));
        } else if (query.trim().length() > 50) {
          showErrorMessage(getString(R.string.search_scr_err_count_max));
        } else {
          Log.d(TAG, "onQueryTextSubmit:");
          return handleButtonClickSearch(query);
        }
        return false;
      }

      @Override
      public boolean onQueryTextChange(final String query) {
        if (query == null
            || query.isEmpty()
            || query.trim().length() < 3
            || query.trim().length() > 50) {
          model.clearCachedList();
          toggleActionButtons();
          adapter.notifyDataSetChanged();
          showHelpText(true);
          return false;
        }
        return true;
      }
    });

    if (contentTemplate == null || contentTemplate.isEmpty()) {
      contentTemplate = getString(R.string.item_search_result_content_template);
    }

    ((RecyclerView) rootView.findViewById(R.id.search_scr_list)).setAdapter(adapter);

    rootView.findViewById(R.id.search_scr_action_share)
            .setOnClickListener(v -> handleButtonClickShare());

    rootView.findViewById(R.id.search_scr_action_bookmark)
            .setOnClickListener(v -> handleButtonClickBookmark());

    rootView.findViewById(R.id.search_scr_action_clear)
            .setOnClickListener(v -> handleButtonClickClearSelection());

    rootView.findViewById(R.id.search_scr_action_reset)
            .setOnClickListener(v -> resetScreen());

    if (savedState == null) {
      showHelpText(true);
    } else {
      showHelpText(false);
    }

    return rootView;
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mainOps = null;
    model = null;
  }

  private void resetScreen() {
    Log.d(TAG, "resetScreen:");
    rootView.findViewById(R.id.search_scr_action_reset).setVisibility(GONE);
    rootView.findViewById(R.id.search_scr_query_result_count).setVisibility(GONE);

    final SearchView searchView = rootView.findViewById(R.id.search_scr_query_field);
    searchView.setVisibility(VISIBLE);
    searchView.setQuery("", false);

    mainOps.hideKeyboard();
    model.clearCachedList();
    toggleActionButtons();
    adapter.notifyDataSetChanged();
    showHelpText(true);
  }

  private boolean handleButtonClickSearch(@Nullable final String text) {
    Log.d(TAG, "handleButtonClickSearch: text = [" + text + "]");
    mainOps.hideKeyboard();

    if (text == null || text.isEmpty()) {
      showErrorMessage(getString(R.string.search_scr_err_empty_input));
    } else if (text.trim().length() < 3) {
      showErrorMessage(getString(R.string.search_scr_err_count_min));
    } else if (text.trim().length() > 50) {
      showErrorMessage(getString(R.string.search_scr_err_count_max));
    } else {

      model.searchText(text).observe(this, verses -> {
        if (verses == null || verses.isEmpty()) {
          showHelpText(true);
          final String template = getString(R.string.search_scr_text_empty_results);
          final String htmlText = String.format(template, text);
          ((TextView) rootView.findViewById(R.id.search_scr_help_text))
              .setText(HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_LEGACY));
        } else {
          showHelpText(false);
          adapter.refreshList(verses);
          adapter.notifyDataSetChanged();

          final TextView textView = rootView.findViewById(R.id.search_scr_query_result_count);
          textView.setText(String.format(
              getString(R.string.search_scr_query_field_template), adapter.getItemCount()));

          rootView.findViewById(R.id.search_scr_query_field).setVisibility(GONE);
          rootView.findViewById(R.id.search_scr_query_result_count).setVisibility(VISIBLE);
          rootView.findViewById(R.id.search_scr_action_reset).setVisibility(VISIBLE);
          ((RecyclerView) rootView.findViewById(R.id.search_scr_list)).scrollToPosition(0);
        }
      });

    }
    return true;
  }

  private void showErrorMessage(@NonNull final String message) {
    resetScreen();
    showHelpText(true);
    mainOps.showErrorMessage(message);
    rootView.findViewById(R.id.search_scr_query_field).requestFocus();
  }

  private void showHelpText(boolean showHelp) {
    // search input field
    rootView.findViewById(R.id.search_scr_query_field)
            .setVisibility((showHelp) ? VISIBLE : GONE);

    // search reset button
    rootView.findViewById(R.id.search_scr_action_reset)
            .setVisibility((showHelp) ? GONE : VISIBLE);

    // search results count message
    final TextView resultsCountView = rootView.findViewById(R.id.search_scr_query_result_count);
    resultsCountView.setVisibility((showHelp) ? GONE : VISIBLE);
    if (model.getCachedListSize() > 0) {
      resultsCountView.setText(String.format(
          getString(R.string.search_scr_query_field_template), adapter.getItemCount()));
    }

    // hero image
    rootView.findViewById(R.id.search_scr_help_image)
            .setVisibility((showHelp) ? VISIBLE : GONE);

    // help message
    final TextView helpText = rootView.findViewById(R.id.search_scr_help_text);
    helpText.setVisibility((showHelp) ? VISIBLE : GONE);
    helpText.setText(HtmlCompat.fromHtml(getString(
        R.string.search_scr_help_text_default),
                                         HtmlCompat.FROM_HTML_MODE_LEGACY));
    toggleActionButtons();
  }

  @Override
  public void toggleActionButtons() {
    rootView.findViewById(R.id.search_scr_container_action)
            .setVisibility((model.isSelectionEmpty()) ? GONE : VISIBLE);
  }

  @Override
  public void showContent(@NonNull final TextView textView,
                          @NonNull final Verse verse) {
    model.getBook(verse.getBookNumber()).observe(this, book -> {
      if (book == null) {
        Log.e(TAG, "showContent: no book found for this verse [" + verse + "]");
        return;
      }
      textView.setText(HtmlCompat.fromHtml(String.format(contentTemplate,
                                                         book.getName(),
                                                         verse.getChapterNumber(),
                                                         verse.getVerseNumber(),
                                                         verse.getText()),
                                           HtmlCompat.FROM_HTML_MODE_LEGACY));
    });
  }

  @Override
  public void refreshCachedList(@NonNull final List<?> list) {
    model.refreshCachedList(list);
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
  public void addSelection(@NonNull final Verse verse) {
    model.addSelection(verse);
  }

  @Override
  public void addSelection(@NonNull final String text) {
    model.addSelection(text);
  }

  @Override
  public void removeSelection(@NonNull final Verse verse) {
    model.removeSelection(verse);
  }

  @Override
  public void removeSelection(@NonNull final String text) {
    model.removeSelection(text);
  }

  @Override
  public boolean isSelected(@NonNull final Verse verse) {
    return model.isSelected(verse);
  }

  @Override
  public void clearSelection() {
    model.clearSelection();
  }

  private void handleButtonClickBookmark() {
    Log.d(TAG, "handleButtonClickBookmark() called");
    if (model.isSelectionEmpty()) {
      mainOps.showErrorMessage(getString(R.string.search_scr_err_empty_selection));
      return;
    }
    final HashSet<Verse> results = model.getSelectedVerses();
    Verse[] verseArray = new Verse[results.size()];
    results.toArray(verseArray);
    Bundle args = new Bundle();
    args.putParcelableArray(BookmarkScreen.ARG_ARRAY_VERSES, verseArray);
    NavHostFragment.findNavController(this)
                   .navigate(R.id.action_searchScreen_to_bookmarkScreen, args);
    resetScreen();
  }

  private void handleButtonClickShare() {
    if (model.isSelectionEmpty()) {
      mainOps.showErrorMessage(getString(R.string.search_scr_err_empty_selection));
      return;
    }
    final HashSet<String> selectedTextList = model.getSelectedTexts();
    final StringBuilder shareText = new StringBuilder();
    for (final String text : selectedTextList) {
      shareText.append(text);
      shareText.append("\n");
    }
    mainOps.shareText(String.format(
        getString(R.string.search_scr_action_share_template), shareText));
    resetScreen();
  }

  private void handleButtonClickClearSelection() {
    if (model.isSelectionEmpty()) {
      mainOps.showErrorMessage(getString(R.string.search_scr_err_empty_selection));
    }
    adapter.clearSelection();
    adapter.notifyDataSetChanged();
    toggleActionButtons();
  }

}
