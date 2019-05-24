package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
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
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashSet;
import java.util.List;

import static androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY;

public class SearchScreen
    extends Fragment
    implements SearchScreenOps {

  private static final String TAG = "SearchScreen";

  private SimpleBibleScreenOps mainOps;
  private SearchScreenModel model;
  private SearchScreenAdapter adapter;

  private String contentTemplate;
  private RecyclerView list;
  private TextInputEditText input;
  private ImageView imageView;
  private TextView textView;
  private AppCompatImageButton butShare;
  private AppCompatImageButton butBookmark;
  private AppCompatImageButton butReset;

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
    final View view = inflater.inflate(R.layout.search_screen, container, false);
    view.findViewById(R.id.search_scr_fab_search)
        .setOnClickListener(v -> handleButtonClickSearch());

    imageView = view.findViewById(R.id.search_scr_image);
    textView = view.findViewById(R.id.search_scr_text);
    input = view.findViewById(R.id.search_src_input);
    list = view.findViewById(R.id.search_scr_list);

    list.setAdapter(adapter);
    contentTemplate = getString(R.string.item_search_result_content_template);

    butShare = view.findViewById(R.id.search_scr_butt_share);
    butShare.setOnClickListener(v -> handleButtonClickShare());

    butBookmark = view.findViewById(R.id.search_scr_butt_bmark);
    butBookmark.setOnClickListener(v -> handleButtonClickBookmark());

    butReset = view.findViewById(R.id.search_scr_butt_reset);
    butReset.setOnClickListener(v -> handleButtonClickReset());

    // handle action key event from keyboard on input field
    input.setOnEditorActionListener((v, actionId, event) -> {
      if (event == null) {
        handleButtonClickSearch();
        return true;
      }
      return false;
    });

    if (savedState == null && model.getCachedListSize() == 0) {
      showHelpText(true);
    } else if (savedState != null && model.getCachedListSize() > 0) {
      showHelpText(false);
    }

    return view;
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mainOps = null;
    model = null;
  }

  private void resetScreen() {
    Log.d(TAG, "resetScreen:");
    input.setText(null);
    mainOps.hideKeyboard();
    model.clearCachedList();
    toggleActionButtons();
    adapter.notifyDataSetChanged();
    showHelpText(true);
  }

  private void handleButtonClickSearch() {
    mainOps.hideKeyboard();
    final String text = getInput();

    if (text.isEmpty()) {
      showErrorMessage(getString(R.string.search_scr_error_empty_input));
    } else if (text.trim().length() < 3) {
      showErrorMessage(getString(R.string.search_scr_err_min_count));
    } else if (text.trim().length() > 50) {
      showErrorMessage(getString(R.string.search_scr_err_max_count));
    } else {

      model.searchText(text).observe(this, verses -> {
        if (verses == null || verses.isEmpty()) {
          showHelpText(true);
          final String template = getString(R.string.search_scr_text_empty_results);
          final String htmlText = String.format(template, text);
          textView.setText(HtmlCompat.fromHtml(htmlText, FROM_HTML_MODE_LEGACY));
        } else {
          showHelpText(false);
          adapter.refreshList(verses);
          adapter.notifyDataSetChanged();
        }
      });

    }
  }

  private void showErrorMessage(@NonNull final String message) {
    showHelpText(true);
    mainOps.showErrorMessage(message);
    input.requestFocus();
  }

  private void showHelpText(boolean showHelp) {
    list.setVisibility((!showHelp) ? View.VISIBLE : View.GONE);
    imageView.setVisibility((showHelp) ? View.VISIBLE : View.GONE);
    textView.setVisibility((showHelp) ? View.VISIBLE : View.GONE);
    textView.setText(HtmlCompat.fromHtml(getString(
        R.string.search_scr_text_default), FROM_HTML_MODE_LEGACY));
    toggleActionButtons();
  }

  @NonNull
  private String getInput() {
    final Editable editable = input.getText();
    return (editable != null) ? editable.toString().trim() : "";
  }

  @Override
  public void toggleActionButtons() {
    final int visibilityValue = (model.isSelectionEmpty()) ? View.GONE : View.VISIBLE;
    butShare.setVisibility(visibilityValue);
    butBookmark.setVisibility(visibilityValue);
    butReset.setVisibility(visibilityValue);
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
                                                         verse.getText()), FROM_HTML_MODE_LEGACY));
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
  public void removeSelection(@NonNull final Verse verse) {
    model.removeSelection(verse);
  }

  @Override
  public void addSelection(@NonNull final String text) {
    model.addSelection(text);
  }

  @Override
  public void removeSelection(@NonNull final String text) {
    model.removeSelection(text);
  }

  @Override
  public boolean isSelected(@NonNull final Verse verse) {
    return model.isSelected(verse);
  }

  private void handleButtonClickBookmark() {
    Log.d(TAG, "handleButtonClickBookmark() called");
    mainOps.hideKeyboard();
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
    mainOps.hideKeyboard();
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
        getString(R.string.search_scr_selection_share_template), shareText));
    resetScreen();
  }

  private void handleButtonClickReset() {
    mainOps.hideKeyboard();
    if (model.isSelectionEmpty()) {
      mainOps.showErrorMessage(getString(R.string.search_scr_err_empty_selection));
      return;
    }
    resetScreen();
  }

}
